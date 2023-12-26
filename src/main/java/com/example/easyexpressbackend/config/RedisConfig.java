package com.example.easyexpressbackend.config;

import com.example.easyexpressbackend.response.region.ProvinceResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;


@Configuration
public class RedisConfig {
    @Value("REDIS_HOST")
    private String redisHost;
    @Value("REDIS_PORT")
    private int redisPort;
    @Value("REDIS_PASSWORD")
    private String redisPassword;

    @Bean
    public RedisTemplate<String, ProvinceResponse> redisTemplate(){
        RedisTemplate<String, ProvinceResponse> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        return template;
    }

    @Bean

     JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setHostName(redisHost);
        jedisConnectionFactory.setPort(redisPort);
        jedisConnectionFactory.setPassword(redisPassword);
        return jedisConnectionFactory;
    }
}
