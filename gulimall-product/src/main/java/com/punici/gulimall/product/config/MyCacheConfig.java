package com.punici.gulimall.product.config;

import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
@EnableConfigurationProperties(CacheProperties.class)
public class MyCacheConfig
{
    /**
     * 配置文件中的设置未应用 原来和配置文件绑定的配置类：
     * 
     * @ConfigurationProperties(prefix = "spring.cache") public class CacheProperties
     *                                 要让它生效，使用注解@EnableConfigurationProperties(CacheProperties.class)
     */
    @Bean
    RedisCacheConfiguration redisCacheConfiguration(CacheProperties cacheProperties)
    {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
        config = config
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()));
        // 指定缓存序列化方式为json
        config = config.serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
        // 将配置文件中的配置生效
        CacheProperties.Redis redisProperties = cacheProperties.getRedis();
        if(redisProperties.getTimeToLive() != null)
        {
            config = config.entryTtl(redisProperties.getTimeToLive());
        }
        
        if(redisProperties.getKeyPrefix() != null)
        {
            config = config.prefixKeysWith(redisProperties.getKeyPrefix());
        }
        
        if(!redisProperties.isCacheNullValues())
        {
            config = config.disableCachingNullValues();
        }
        
        if(!redisProperties.isUseKeyPrefix())
        {
            config = config.disableKeyPrefix();
        }
        return config;
    }
}
