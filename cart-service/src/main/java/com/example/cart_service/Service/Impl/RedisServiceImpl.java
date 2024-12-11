package com.example.cart_service.Service.Impl;

import com.example.cart_service.DTO.Response.CartResponse;
import com.example.cart_service.Exception.CartException;
import com.example.cart_service.Service.RedisService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements RedisService {

    private RedisTemplate redisTemplate;

    @Autowired
    public RedisServiceImpl(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private static ObjectMapper objectMapperInstance;

    public ObjectMapper getObjectMapperInstance() {
        if (objectMapperInstance == null) {
            objectMapperInstance = new ObjectMapper();
        }
        return objectMapperInstance;
    }

    @Override
    public <T> T getData(String key, Class<T> responseClass) {
        try {
            Object data = redisTemplate.opsForValue().get(key);
            ObjectMapper objectMapper = getObjectMapperInstance();
            if (data != null) {
                if (responseClass.equals(List.class)) {
                    return (T) objectMapper.readValue(data.toString(), new TypeReference<List<CartResponse>>() {
                    });
                } else {
                    return objectMapper.readValue(data.toString(), responseClass);
                }
            }
        } catch (Exception e) {
            throw new CartException(e.getMessage());
        }
        return null;
    }

    @Override
    public void setData(String key, Object object, Long ttl) {
        try {
            ObjectMapper objectMapper = getObjectMapperInstance();
            String jsonValue = objectMapper.writeValueAsString(object);
            redisTemplate.opsForValue().set(key, jsonValue, ttl, TimeUnit.SECONDS);
        } catch (Exception exception) {
            throw new CartException(exception.getMessage());
        }

    }

    @Override
    public void deleteData(String key) {
        try{
            redisTemplate.opsForValue().getAndDelete(key);
        }catch (Exception exception){
            throw new CartException(exception.getMessage());
        }
    }
}
