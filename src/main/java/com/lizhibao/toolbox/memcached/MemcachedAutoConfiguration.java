package com.lizhibao.toolbox.memcached;

import com.danga.MemCached.MemCachedClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自动配置类
 * @author lizhibao
 * @date 2025-02-13
 */
@Configuration
@ConditionalOnClass(MemCachedClient.class)
@EnableConfigurationProperties(MemcachedProperties.class)
public class MemcachedAutoConfiguration {

    @Bean
    public MemcachedManager memcachedManager(MemcachedProperties properties) {
        return new MemcachedManager(properties);
    }

    @Bean
    public MemcachedTemplate memcachedTemplate(MemcachedManager manager) {
        return new MemcachedTemplate(manager);
    }
}
