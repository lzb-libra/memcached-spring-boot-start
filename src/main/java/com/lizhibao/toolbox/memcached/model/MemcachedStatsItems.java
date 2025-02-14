package com.lizhibao.toolbox.memcached.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lizhibao
 * @date 2025-02-13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemcachedStatsItems {
    private String server;
    private List<MemcachedStatsItem> items = new ArrayList<>();

    public MemcachedStatsItems(String server) {
        this.server = server;
    }

    public void addItems(MemcachedStatsItem item) {
        this.items.add(item);
    }
}
