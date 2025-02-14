package com.lizhibao.toolbox.memcached.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * @author lizhibao
 * @date 2025-02-13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemcachedCacheDump {
    private String server;
    private Set<String> keys = new HashSet<>(20);

    public MemcachedCacheDump(String server) {
        this.server = server;
    }

    public void addKey(String key) {
        this.keys.add(key);
    }
}
