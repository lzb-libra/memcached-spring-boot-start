package com.lizhibao.toolbox.memcached;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 服务器集合
 * @author lizhibao
 * @date 2025-02-13
 */
@Data
@Component
@ConfigurationProperties(prefix = "spring.memcached")
public class MemcachedProperties {
    private List<MemcachedConfig> nodes;
}
