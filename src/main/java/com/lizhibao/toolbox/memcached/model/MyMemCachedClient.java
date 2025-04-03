package com.lizhibao.toolbox.memcached.model;

import com.danga.MemCached.MemCachedClient;
import com.lizhibao.toolbox.memcached.MemcachedConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author lizhibao
 * @date 2025-04-03
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class MyMemCachedClient {
    private MemcachedConfig config;
    private MemCachedClient client;
}
