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
public class MemcachedStatsSlabs {
    private String server;
    /**
     * 当前处于活动状态的 slab 数量
     */
    private String active_slabs;
    /**
     * 已分配的总内存量。单位通常是字节（bytes）
     */
    private String total_malloced;

    private List<MemcachedStatsSlab> slabs = new ArrayList<>();

    public MemcachedStatsSlabs(String server) {
        this.server = server;
    }

    public void addItems(MemcachedStatsSlab item) {
        this.slabs.add(item);
    }
}
