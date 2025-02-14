package com.lizhibao.toolbox.memcached.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lizhibao
 * @date 2025-02-13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemcachedStatsItem {
    private String slabId;
    /**
     * 当前存储的缓存项数量
     */
    private String number;
    /**
     * 缓存项的年龄，表示该缓存项自创建以来的时间。单位通常是秒（seconds）
     */
    private String age;
    /**
     * 表示被驱逐（evicted）的缓存项数量
     */
    private String evicted;
    /**
     * 表示被驱逐的非零值缓存项数量
     */
    private String evicted_nonzero;
    /**
     * 表示缓存项被驱逐的次数
     */
    private String evicted_time;
    /**
     * 表示由于内存不足而导致的缓存项未能存储的次数
     */
    private String outofmemory;
    /**
     * 表示因为缓存空间不足而执行的缓存尾修复（tail repairs）的次数
     */
    private String tailrepairs;
    /**
     * 表示已回收（reclaimed）的缓存项数量
     */
    private String reclaimed;
    /**
     * 表示未获取的已过期（expired）缓存项数量
     */
    private String expired_unfetched;
    /**
     * 表示未获取的被驱逐缓存项数量
     */
    private String evicted_unfetched;
    /**
     * 表示由缓存项回收器（crawler）回收的缓存项数量
     */
    private String crawler_reclaimed;
    /**
     * 表示缓存项回收器检查的缓存项数量
     */
    private String crawler_items_checked;
    /**
     * 表示因为缓存空间不足而被 LRU 尾部锁定的缓存项数量
     */
    private String lrutail_reflocked;

    public MemcachedStatsItem(String slabId) {
        this.slabId = slabId;
    }
}
