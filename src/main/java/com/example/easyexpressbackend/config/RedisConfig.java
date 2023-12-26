package com.example.easyexpressbackend.config;

import com.example.easyexpressbackend.response.region.ProvinceResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;


@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, ProvinceResponse> redisTemplate(){
        RedisTemplate<String, ProvinceResponse> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        return template;
    }

    @Bean
    
     JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setHostName("35.240.181.135");
        jedisConnectionFactory.setPort(6379);
        jedisConnectionFactory.setPassword("redis");
        return jedisConnectionFactory;
    }
}
