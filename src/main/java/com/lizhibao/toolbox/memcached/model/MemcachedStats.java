package com.lizhibao.toolbox.memcached.model;

import com.lizhibao.toolbox.memcached.MemcachedUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author lizhibao
 * @date 2025-02-13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemcachedStats {
    private String server;
    /**
     * 进程ID
     */
    private String pid;
    /**
     * Repcached 内存管理器中空闲内存的情况，单位 bytes
     */
    private String repcached_qi_free;
    /**
     *  Repcached 写入队列（Write Queue）的数量
     */
    private String repcached_wsize;
    /**
     * Repcached 写入队列（Write Queue）中待复制的数据大小，单位 bytes
     */
    private String repcached_wdata;
    /**
     * Repcached 的版本号
     */
    private String repcached_version;
    /**
     * 将主节点（Master）上的数据复制到一个或多个从节点（Slave）的过程
     */
    private String replication;
    /**
     * 服务器运行秒数
     */
    private String uptime;
    /**
     * 服务器当前unix时间戳
     */
    private String time;
    /**
     * 服务器版本
     */
    private String version;
    /**
     * libevent版本号
     */
    private String libevent;
    /**
     * 操作系统指针大小(这台服务器是64位的)
     */
    private String pointer_size;
    /**
     * 进程累计用户时间，单位 ms
     */
    private String rusage_user;
    /**
     * 进程所消耗的系统级 CPU 时间，单位 ms
     */
    private String rusage_system;
    /**
     * 当前打开连接数
     */
    private String curr_connections;
    /**
     * 曾打开的连接总数
     */
    private String total_connections;
    /**
     * 服务器分配的连接结构数
     */
    private String connection_structures;
    /**
     * 内部使用的FD数
     */
    private String reserved_fds;
    /**
     * 执行get命令总数
     */
    private String cmd_get;
    /**
     * 执行set命令总数
     */
    private String cmd_set;
    /**
     * 执行flush命令总数
     */
    private String cmd_flush;
    /**
     * 执行touch命令总数
     */
    private String cmd_touch;
    /**
     * get命中次数
     */
    private String get_hits;
    /**
     * get未命中次数
     */
    private String get_misses;
    /**
     * delete未命中次数
     */
    private String delete_misses;
    /**
     * delete命中次数
     */
    private String delete_hits;
    /**
     * incr未命中次数
     */
    private String incr_misses;
    /**
     * incr命中次数
     */
    private String incr_hits;
    /**
     * decr未命中次数
     */
    private String decr_misses;
    /**
     * decr命中次数
     */
    private String decr_hits;
    /**
     * cas未命中次数
     */
    private String cas_misses;
    /**
     * cas命中次数
     */
    private String cas_hits;
    /**
     * 使用擦拭次数
     */
    private String cas_badval;
    /**
     * touch命中次数
     */
    private String touch_hits;
    /**
     * touch未命中次数
     */
    private String touch_misses;
    /**
     * 认证处理的次数
     */
    private String auth_cmds;
    /**
     * 认证失败次数
     */
    private String auth_errors;
    /**
     * 读取字节总数
     */
    private String bytes_read;
    /**
     * 写入字节总数
     */
    private String bytes_written;
    /**
     * 最大内存容量，单位 bytes
     */
    private String limit_maxbytes;
    /**
     * 目前接受的新接数
     */
    private String accepting_conns;
    /**
     * 失效的监听数
     */
    private String listen_disabled_num;
    /**
     * 当前线程数
     */
    private String threads;
    /**
     * 连接操作主支放弃数目
     */
    private String conn_yields;
    /**
     * hash等级
     */
    private String hash_power_level;
    /**
     * 当前hash表等级
     */
    private String hash_bytes;
    /**
     * 当前哈希表是否正在扩展
     */
    private String hash_is_expanding;
    /**
     * 当前存储占用的字节数
     */
    private String bytes;
    /**
     * 当前存储数据总数
     */
    private String curr_items;
    /**
     * 启动以来存储的数据总数
     */
    private String total_items;
    /**
     * 已过期但未获取的对象数目
     */
    private String expired_unfetched;
    /**
     * 已驱逐但未获取的对象数目
     */
    private String evicted_unfetched;
    /**
     * LRU释放的对象数目
     */
    private String evictions;
    /**
     * 用已过期的数据条目来存储新数据的数目
     */
    private String reclaimed;

    public MemcachedStats(String server) {
        this.server = server;
    }
}
