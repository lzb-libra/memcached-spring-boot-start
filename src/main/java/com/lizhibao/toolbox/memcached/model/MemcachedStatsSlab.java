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
public class MemcachedStatsSlab {
    private String slabId;
    /**
     * 每个 chunk 的大小，即缓存块的大小。单位通常是字节（bytes）
     */
    private String chunk_size;
    /**
     * 每页的 chunk 数量。通常是一个常数，表示内存分配时的单位。
     */
    private String chunks_per_page;
    /**
     * 总页数，即分配给此 slab class 的页数
     */
    private String total_pages;
    /**
     * 总的 chunk 数量，即分配给此 slab class 的 chunk 总数
     */
    private String total_chunks;
    /**
     * 已使用的 chunk 数量
     */
    private String used_chunks;
    /**
     * 空闲的 chunk 数量
     */
    private String free_chunks;
    /**
     * 位于末尾的空闲 chunk 数量
     */
    private String free_chunks_end;
    /**
     * 请求的内存总量。单位通常是字节（bytes）
     */
    private String mem_requested;
    /**
     * 缓存命中的次数
     */
    private String get_hits;
    /**
     * set 命令执行的次数
     */
    private String cmd_set;
    /**
     * delete 命令执行时缓存项存在且删除成功的次数
     */
    private String delete_hits;
    /**
     * incr 命令执行时缓存项存在且自增成功的次数
     */
    private String incr_hits;
    /**
     * decr 命令执行时缓存项存在且自减成功的次数
     */
    private String decr_hits;
    /**
     * cas 命令执行时缓存项存在且 CAS 操作成功的次数
     */
    private String cas_hits;
    /**
     * cas 命令执行时缓存项存在但 CAS 操作失败的次数
     */
    private String cas_badval;
    /**
     * touch 命令执行时缓存项存在且更新成功的次数
     */
    private String touch_hits;

    public MemcachedStatsSlab(String slabId) {
        this.slabId = slabId;
    }
}
