package net.engineeringdigest.journalApp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class RedisService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * Generic GET from Redis.
     * Fetches the JSON string stored at 'key' and converts it to entityClass type.
     *
     * @param key         Redis key (e.g., "weather_of_mumbai")
     * @param entityClass The target class to deserialize into (e.g., WeatherResponse.class)
     */
    public <T> T get(String key, Class<T> entityClass) {
        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            return null;
        }

        // ObjectMapper converts JSON String → Java POJO
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(value.toString(), entityClass);
        } catch (JsonProcessingException e) {
            log.error("Exception while converting JSON -> POJO");
            return null;
        }
    }

    /**
     * Generic SET into Redis with TTL (Time To Live).
     *
     * @param key   Redis key
     * @param value Any Java object (will be serialized to JSON String)
     * @param ttl   How long to cache (in seconds). Pass -1 for no expiry.
     */
    public void set(String key, Object value, long ttl) {
        ObjectMapper mapper = new ObjectMapper();

        // Convert POJO → JSON String (because we use StringRedisSerializer)
        try {
            String jsonValue = mapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, jsonValue, ttl, TimeUnit.SECONDS);
        } catch (JsonProcessingException e) {
            log.error("Exception while converting POJO -> JSON");
            throw new RuntimeException(e);
        }
    }

}
