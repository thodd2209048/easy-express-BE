package com.example.easyexpressbackend.config;

import com.example.easyexpressbackend.entity.redis.ProvinceCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;


@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, ProvinceCache> redisTemplate(){
        RedisTemplate<String, ProvinceCache> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        return template;
    }

    @Bean
     JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setHostName("localhost");
        jedisConnectionFactory.setPort(6378);
        jedisConnectionFactory.setPassword("eYVX7EwVmmxKPCDmwMtyKVge8oLd2t81");
        return jedisConnectionFactory;
    }
}
