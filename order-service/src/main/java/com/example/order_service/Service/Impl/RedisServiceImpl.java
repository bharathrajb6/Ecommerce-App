package com.example.order_service.Service.Impl;

import com.example.order_service.DTO.Response.OrderResponse;
import com.example.order_service.Exception.OrderException;
import com.example.order_service.Service.RedisService;
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

    private static final ObjectMapper objectMapper = null;

    @Autowired
    private RedisTemplate redisTemplate;

    private ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            return new ObjectMapper();
        }
        return objectMapper;
    }

    @Override
    public <T> T getData(String key, Class<T> responseClass) {
        try {
            Object data = redisTemplate.opsForValue().get(key);
            ObjectMapper objectMapper = getObjectMapper();
            if (data != null) {
                if (responseClass.equals(List.class)) {
                    return (T) objectMapper.readValue(data.toString(), new TypeReference<List<OrderResponse>>() {
                    });
                } else {
                    return objectMapper.readValue(data.toString(), responseClass);
                }
            }
        } catch (Exception e) {
            throw new OrderException(e.getMessage());
        }
        return null;
    }

    @Override
    public void setData(String key, Object o, Long ttl) {
        try {
            ObjectMapper objectMapper = getObjectMapper();
            String jsonValue = objectMapper.writeValueAsString(o);
            redisTemplate.opsForValue().set(key, jsonValue, ttl, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new OrderException(e.getMessage());
        }
    }

    @Override
    public void deleteData(String key) {
        try {
            redisTemplate.opsForValue().getAndDelete(key);
        } catch (Exception e) {
            throw new OrderException(e.getMessage());
        }
    }
}
