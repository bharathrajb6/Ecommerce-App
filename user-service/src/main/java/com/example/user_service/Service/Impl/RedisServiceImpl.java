package com.example.user_service.Service.Impl;

import com.example.user_service.Exceptions.UserException;
import com.example.user_service.Service.RedisService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedisServiceImpl implements RedisService {

    @Autowired
    private RedisTemplate redisTemplate;



    @Override
    public <T> T getData(String key, Class<T> responseClass) {

        try {
            Object data = redisTemplate.opsForValue().get(key);
            ObjectMapper objectMapper = new ObjectMapper();
            if (data != null) {
                if (responseClass.equals(List.class)) {
                    // For collections, use TypeReference
                    return (T) objectMapper.readValue(data.toString(), new TypeReference<List<T>>() {
                    });
                } else {
                    // For single object
                    return objectMapper.readValue(data.toString(), responseClass);
                }
            }
        } catch (Exception exception) {
            log.error("Unable to convert");
            throw new UserException(exception.getMessage());
        }
        return null;
    }


    @Override
    public void setData(String key, Object object, Long ttl) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonValue = objectMapper.writeValueAsString(object);
            redisTemplate.opsForValue().set(key, jsonValue, ttl, TimeUnit.SECONDS);
        } catch (Exception exception) {
            log.error("Unable to convert");
            throw new UserException(exception.getMessage());
        }
    }

    @Override
    public void deleteData(String key) {
        try {
            redisTemplate.opsForValue().getAndDelete(key);
        } catch (Exception exception) {
            log.error("Unable to delete");
            throw new UserException(exception.getMessage());
        }
    }
}
