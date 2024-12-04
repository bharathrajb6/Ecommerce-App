package com.example.product_service.Service.Impl;

import com.example.product_service.Exceptions.ProductExceptions;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


@Service
@Slf4j
public class RedisServiceImpl {

    @Autowired
    private RedisTemplate redisTemplate;

    public <T> T getData(String key, Class<T> responseClass) {

        try {
            Object data = redisTemplate.opsForValue().get(key);
            ObjectMapper objectMapper = new ObjectMapper();
            if(data!=null){
                return objectMapper.readValue(data.toString(), responseClass);
            }
        } catch (Exception exception) {
            log.error("Unable to convert");
            throw new ProductExceptions(exception.getMessage());
        }
        return null;
    }


    public void setData(String key, Object object, Long ttl) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonValue = objectMapper.writeValueAsString(object);
            redisTemplate.opsForValue().set(key, jsonValue, ttl, TimeUnit.SECONDS);
        } catch (Exception exception) {
            log.error("Unable to convert");
            throw new ProductExceptions(exception.getMessage());
        }
    }
}