package com.lizhibao.toolbox.memcached;

import com.danga.MemCached.MemCachedClient;
import com.lizhibao.toolbox.memcached.model.*;
import com.schooner.MemCached.MemcachedItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.*;

/**
 * 统一封装操作，当调用的方法没有传递缓存名称(name)时，使用的是 clients 中的第一个缓存对象来进行操作的。
 * @author lizhibao
 * @date 2025-02-13
 */
@Slf4j
@Component
public class MemcachedTemplate {
    private final String defaultName;
    private final Map<String, MemCachedClient> clients;

    public MemcachedTemplate(MemcachedManager manager) {
        this.clients = manager.getClients();
        this.defaultName = clients.keySet().iterator().next();
    }

    /**
     * 获取原始缓存客户端
     * @return MemCachedClient
     */
    public MemCachedClient getClient() {
        return this.getClient(this.defaultName);
    }

    /**
     * 获取原始缓存客户端
     * @param name 缓存名称
     * @return MemCachedClient
     */
    public MemCachedClient getClient(String name) {
        if(log.isDebugEnabled()) log.debug("{}", name);
        return clients.get(name);
    }

    /**
     * 判断缓存中是否存在指定的key
     * @param key 缓存key
     * @return true / false
     */
    public boolean keyExists(String key) {
        return this.keyExists(this.defaultName, key);
    }

    /**
     * 判断缓存中是否存在指定的key
     * @param name 缓存名称
     * @param key 缓存key
     * @return true / false
     */
    public boolean keyExists(String name, String key) {
        if(log.isDebugEnabled()) log.debug("{} => {}", name, key);
        MemCachedClient client = clients.get(name);
        return client != null && client.keyExists(key);
    }

    /**
     * 删除指定key对应的数据
     * @param key 缓存key
     * @return true / false
     */
    public boolean delete(String key) {
        return this.delete(this.defaultName, key);
    }

    /**
     * 删除指定key对应的数据
     * @param name 缓存名称
     * @param key 缓存key
     * @return true / false
     */
    public boolean delete(String name, String key) {
        if(log.isDebugEnabled()) log.debug("{} => {}", name, key);
        MemCachedClient client = clients.get(name);
        return client != null && client.delete(key);
    }

    /**
     * 存储数据，当 key 存在时会覆盖旧值
     * @param key 缓存key
     * @param value 缓存数据
     * @return true / false
     */
    public boolean set(String key, Object value) {
        return this.set(this.defaultName, key, value);
    }

    /**
     * 存储数据，当 key 存在时会覆盖旧值
     * @param name 缓存名称
     * @param key 缓存key
     * @param value 缓存数据
     * @return true / false
     */
    public boolean set(String name, String key, Object value) {
        return this.set(name, key, value, 0L);
    }

    /**
     * 存储数据，当 key 存在时会覆盖旧值
     * @param key 缓存key
     * @param value 缓存数据
     * @param expired 过期时间
     * @return true / false
     */
    public boolean set(String key, Object value, long expired) {
        return this.set(this.defaultName, key, value, expired);
    }

    /**
     * 存储数据，当 key 存在时会覆盖旧值
     * @param name 缓存名称
     * @param key 缓存key
     * @param value 缓存数据
     * @param expired 过期时间
     * @return true / false
     */
    public boolean set(String name, String key, Object value, long expired) {
        if(log.isDebugEnabled()) log.debug("{} => {} / {} / {}", name, key, value, expired);
        MemCachedClient client = clients.get(name);
        return client != null && client.set(key, value, new Date(expired));
    }

    /**
     * 存储数据，当 key 存在时会存储失败
     * @param key 缓存key
     * @param value 缓存数据
     * @return true / false
     */
    public boolean add(String key, Object value) {
        return this.add(this.defaultName, key, value);
    }

    /**
     * 存储数据，当 key 存在时会存储失败
     * @param name 缓存名称
     * @param key 缓存key
     * @param value 缓存数据
     * @return true / false
     */
    public boolean add(String name, String key, Object value) {
        return this.add(name, key, value, 0L);
    }

    /**
     * 存储数据，当 key 存在时会存储失败
     * @param key 缓存key
     * @param value 缓存数据
     * @param expired 过期时间
     * @return true / false
     */
    public boolean add(String key, Object value, long expired) {
        return this.add(this.defaultName, key, value, expired);
    }

    /**
     * 存储数据，当 key 存在时会存储失败
     * @param name 缓存名称
     * @param key 缓存key
     * @param value 缓存数据
     * @param expired 过期时间
     * @return true / false
     */
    public boolean add(String name, String key, Object value, long expired) {
        if(log.isDebugEnabled()) log.debug("{} => {} / {} / {}", name, key, value, expired);
        MemCachedClient client = clients.get(name);
        return client != null && client.add(key, value, new Date(expired));
    }

    /**
     * 更新数据，如果 key 不存在更新失败
     * @param key 缓存key
     * @param value 缓存数据
     * @return true / false
     */
    public boolean replace(String key, Object value) {
        return this.replace(this.defaultName, key, value);
    }

    /**
     * 更新数据，如果 key 不存在更新失败
     * @param name 缓存名称
     * @param key 缓存key
     * @param value 缓存数据
     * @return true / false
     */
    public boolean replace(String name, String key, Object value) {
        return this.replace(name, key, value, 0L);
    }

    /**
     * 更新数据，如果 key 不存在更新失败
     * @param key 缓存key
     * @param value 缓存数据
     * @param expired 过期时间
     * @return true / false
     */
    public boolean replace(String key, Object value, long expired) {
        return this.replace(this.defaultName, key, value, expired);
    }

    /**
     * 更新数据，如果 key 不存在更新失败
     * @param name 缓存名称
     * @param key 缓存key
     * @param value 缓存数据
     * @param expired 过期时间
     * @return true / false
     */
    public boolean replace(String name, String key, Object value, long expired) {
        if(log.isDebugEnabled()) log.debug("{} => {} / {} / {}", name, key, value, expired);
        MemCachedClient client = clients.get(name);
        return client != null && client.replace(key, value, new Date(expired));
    }

    /**
     * 在 key 现有数据后追加内容
     * @param key 缓存key
     * @param value 缓存数据
     * @return true / false
     */
    public boolean append(String key, Object value) {
        return this.append(this.defaultName, key, value);
    }

    /**
     * 在 key 现有数据后追加内容
     * @param name 缓存名称
     * @param key 缓存key
     * @param value 缓存数据
     * @return true / false
     */
    public boolean append(String name, String key, Object value) {
        if(log.isDebugEnabled()) log.debug("{} => {} / {}", name, key, value);
        MemCachedClient client = clients.get(name);
        return client != null && client.append(key, value);
    }

    /**
     * 在 key 现有数据前追加内容
     * @param key 缓存key
     * @param value 缓存数据
     * @return true / false
     */
    public boolean prepend(String key, Object value) {
        return this.prepend(this.defaultName, key, value);
    }

    /**
     * 在 key 现有数据前追加内容
     * @param name 缓存名称
     * @param key 缓存key
     * @param value 缓存数据
     * @return true / false
     */
    public boolean prepend(String name, String key, Object value) {
        if(log.isDebugEnabled()) log.debug("{} => {} / {}", name, key, value);
        MemCachedClient client = clients.get(name);
        return client != null && client.prepend(key, value);
    }

    /**
     * 存储一个计数器，如何存在则会覆盖旧值
     * @param key 缓存key
     * @param value 缓存数据
     * @return true / false
     */
    public boolean storeCounter(String key, Long value) {
        return this.storeCounter(this.defaultName, key, value);
    }

    /**
     * 存储一个计数器，如何存在则会覆盖旧值
     * @param name 缓存名称
     * @param key 缓存key
     * @param value 缓存数据
     * @return true / false
     */
    public boolean storeCounter(String name, String key, Long value) {
        return this.storeCounter(name, key, value, 0L);
    }

    /**
     * 存储一个计数器，如何存在则会覆盖旧值
     * @param key 缓存key
     * @param value 缓存数据
     * @param expired 过期时间
     * @return true / false
     */
    public boolean storeCounter(String key, Long value, long expired) {
        return this.storeCounter(this.defaultName, key, value, expired);
    }

    /**
     * 存储一个计数器，如何存在则会覆盖旧值
     * @param name 缓存名称
     * @param key 缓存key
     * @param value 缓存数据
     * @param expired 过期时间
     * @return true / false
     */
    public boolean storeCounter(String name, String key, Long value, long expired) {
        if(log.isDebugEnabled()) log.debug("{} => {} / {} / {}", name, key, value, expired);
        MemCachedClient client = clients.get(name);
        return client != null && client.storeCounter(key, value, new Date(expired));
    }

    /**
     * 获取存储的计数器
     * @param key 缓存key
     * @return long
     */
    public long getCounter(String key) {
        return this.getCounter(this.defaultName, key);
    }

    /**
     * 获取存储的计数器
     * @param name 缓存名称
     * @param key 缓存key
     * @return long
     */
    public long getCounter(String name, String key) {
        if(log.isDebugEnabled()) log.debug("{} => {}", name, key);
        MemCachedClient client = clients.get(name);
        if(client == null) return -1L;
        return client.getCounter(key);
    }

    /**
     * 存储一个数值并立即自增
     * @param key 缓存key
     * @return long
     */
    public long addOrIncr(String key) {
        return this.addOrIncr(this.defaultName, key);
    }

    /**
     * 存储一个数值并立即自增
     * @param name 缓存名称
     * @param key 缓存key
     * @return long
     */
    public long addOrIncr(String name, String key) {
        return this.addOrIncr(name, key, 0L);
    }

    /**
     * 存储一个数值并立即自增指定大小
     * @param key 缓存key
     * @param incr 自增大小
     * @return long
     */
    public long addOrIncr(String key, long incr) {
        return this.addOrIncr(this.defaultName, key, incr);
    }

    /**
     * 存储一个数值并立即自增
     * @param name 缓存名称
     * @param key 缓存key
     * @param incr 自增大小
     * @return long
     */
    public long addOrIncr(String name, String key, long incr) {
        if(log.isDebugEnabled()) log.debug("{} => {} / {}", name, key, incr);
        MemCachedClient client = clients.get(name);
        if(client == null) return -1L;
        return client.addOrIncr(key, incr);
    }

    /**
     * 存储一个数值并立即自减
     * @param key 缓存key
     * @return long
     */
    public long addOrDecr(String key) {
        return this.addOrDecr(this.defaultName, key);
    }

    /**
     * 存储一个数值并立即自减
     * @param name 缓存名称
     * @param key 缓存key
     * @return long
     */
    public long addOrDecr(String name, String key) {
        return this.addOrDecr(name, key, 0L);
    }

    /**
     * 存储一个数值并立即自减
     * @param key 缓存key
     * @param decr 自减大小
     * @return long
     */
    public long addOrDecr(String key, long decr) {
        return this.addOrDecr(this.defaultName, key, decr);
    }

    /**
     * 存储一个数值并立即自减
     * @param name 缓存名称
     * @param key 缓存key
     * @param decr 自减大小
     * @return long
     */
    public long addOrDecr(String name, String key, long decr) {
        if(log.isDebugEnabled()) log.debug("{} => {} / {}", name, key, decr);
        MemCachedClient client = clients.get(name);
        if(client == null) return -1L;
        return client.addOrDecr(key, decr);
    }

    /**
     * 自增
     * @param key 缓存key
     * @return long
     */
    public long incr(String key) {
        return this.incr(this.defaultName, key);
    }

    /**
     * 自增
     * @param name 缓存名称
     * @param key 缓存key
     * @return long
     */
    public long incr(String name, String key) {
        if(log.isDebugEnabled()) log.debug("{} => {}", name, key);
        MemCachedClient client = clients.get(name);
        if(client == null) return -1L;
        return client.incr(key);
    }

    /**
     * 自增
     * @param key 缓存key
     * @param value 自增大小
     * @return long
     */
    public long incr(String key, long value) {
        return this.incr(this.defaultName, key, value);
    }

    /**
     * 自增
     * @param name 缓存名称
     * @param key 缓存key
     * @param value 自增大小
     * @return long
     */
    public long incr(String name, String key, long value) {
        if(log.isDebugEnabled()) log.debug("{} => {} / {}", name, key, value);
        MemCachedClient client = clients.get(name);
        if(client == null) return -1L;
        return client.incr(key, value);
    }

    /**
     * 自减
     * @param key 缓存key
     * @return long
     */
    public long decr(String key) {
        return this.decr(this.defaultName, key);
    }

    /**
     * 自减
     * @param name 缓存名称
     * @param key 缓存key
     * @return long
     */
    public long decr(String name, String key) {
        if(log.isDebugEnabled()) log.debug("{} => {}", name, key);
        MemCachedClient client = clients.get(name);
        if(client == null) return -1L;
        return client.decr(key);
    }

    /**
     * 自减
     * @param key 缓存key
     * @param value 自减大小
     * @return long
     */
    public long decr(String key, long value) {
        return this.decr(this.defaultName, key, value);
    }

    /**
     * 自减
     * @param name 缓存名称
     * @param key 缓存key
     * @param value 自减大小
     * @return long
     */
    public long decr(String name, String key, long value) {
        if(log.isDebugEnabled()) log.debug("{} => {} / {}", name, key, value);
        MemCachedClient client = clients.get(name);
        if(client == null) return -1L;
        return client.decr(key, value);
    }

    /**
     * 获取一个数据
     * @param key 缓存key
     * @return Object
     */
    public Object get(String key) {
        return this.get(this.defaultName, key);
    }

    /**
     * 获取一个数据
     * @param name 缓存名称
     * @param key 缓存key
     * @return Object
     */
    public Object get(String name, String key) {
        if(log.isDebugEnabled()) log.debug("{} => {}", name, key);
        MemCachedClient client = clients.get(name);
        if(client == null) return null;
        return client.get(key);
    }

    /**
     * 获取一个带CAS令牌的数据
     * @param key 缓存key
     * @return MemcachedItem
     */
    public MemcachedItem gets(String key) {
        return this.gets(this.defaultName, key);
    }

    /**
     * 获取一个带CAS令牌的数据
     * @param name 缓存名称
     * @param key 缓存key
     * @return MemcachedItem
     */
    public MemcachedItem gets(String name, String key) {
        if(log.isDebugEnabled()) log.debug("{} => {}", name, key);
        MemCachedClient client = clients.get(name);
        if(client == null) return null;
        return client.gets(key);
    }

    /**
     * 通过CAS令牌检查并存储数据
     * @param key 缓存key
     * @param value 缓存数据
     * @param casUnique 令牌
     * @return true / false
     */
    public boolean cas(String key, Object value, long casUnique) {
        return this.cas(this.defaultName, key, value, casUnique);
    }

    /**
     * 通过CAS令牌检查并存储数据
     * @param name 缓存名称
     * @param key 缓存key
     * @param value 缓存数据
     * @param casUnique 令牌
     * @return true / false
     */
    public boolean cas(String name, String key, Object value, long casUnique) {
        if(log.isDebugEnabled()) log.debug("{} => {} / {} / {}", name, key, value, casUnique);
        MemCachedClient client = clients.get(name);
        return client != null && client.cas(key, value, casUnique);
    }

    /**
     * 通过CAS令牌检查并存储数据
     * @param key 缓存key
     * @param value 缓存数据
     * @param expired 过期时间
     * @param casUnique 令牌
     * @return true / false
     */
    public boolean cas(String key, Object value, long expired, long casUnique) {
        return this.cas(this.defaultName, key, value, casUnique, expired);
    }

    /**
     * 通过CAS令牌检查并存储数据
     * @param name 缓存名称
     * @param key 缓存key
     * @param value 缓存数据
     * @param expired 过期时间
     * @param casUnique 令牌
     * @return true / false
     */
    public boolean cas(String name, String key, Object value, long expired, long casUnique) {
        if(log.isDebugEnabled()) log.debug("{} => {} / {} / {} / {}", name, key, value, expired, casUnique);
        MemCachedClient client = clients.get(name);
        return client != null && client.cas(key, value, new Date(expired), casUnique);
    }

    /**
     * 批量获取数据
     * @param keys 缓存keys
     * @return Object[]
     */
    public Object[] getMultiArray(String[] keys) {
        return this.getMultiArray(this.defaultName, keys);
    }

    /**
     * 批量获取数据
     * @param name 缓存名称
     * @param keys 缓存keys
     * @return Object[]
     */
    public Object[] getMultiArray(String name, String[] keys) {
        if(log.isDebugEnabled()) log.debug("{} => {}", name, Arrays.toString(keys));
        MemCachedClient client = clients.get(name);
        if(client == null) return null;
        return client.getMultiArray(keys);
    }

    /**
     * 批量获取数据
     * @param keys 缓存keys
     * @return Map<String, Object>
     */
    public Map<String, Object> getMulti(String[] keys) {
        return this.getMulti(this.defaultName, keys);
    }

    /**
     * 批量获取数据
     * @param name 缓存名称
     * @param keys 缓存keys
     * @return Map<String, Object>
     */
    public Map<String, Object> getMulti(String name, String[] keys) {
        if(log.isDebugEnabled()) log.debug("{} => {}", name, Arrays.toString(keys));
        MemCachedClient client = clients.get(name);
        if(client == null) return null;
        return client.getMulti(keys);
    }

    /**
     * 清理缓存中的所有键值对
     * @return true / false
     */
    public boolean flushAll() {
        return this.flushAll(this.defaultName);
    }

    /**
     * 清理缓存中的所有键值对
     * @param name 缓存名称
     * @return true / false
     */
    public boolean flushAll(String name) {
        if(log.isDebugEnabled()) log.debug("{}", name);
        MemCachedClient client = clients.get(name);
        return client != null && client.flushAll();
    }

    /**
     * 获取统计信息
     * @return Map<String, Map<String, String>>
     */
    public Map<String, Map<String, String>> stats() {
        return this.stats(this.defaultName);
    }

    /**
     * 获取统计信息
     * @param name 缓存名称
     * @return Map<String, Map<String, String>>
     */
    public Map<String, Map<String, String>> stats(String name) {
        if(log.isDebugEnabled()) log.debug("{}", name);
        MemCachedClient client = clients.get(name);
        if(client == null) return new HashMap<>();
        return client.stats();
    }

    /**
     * 获取各个 slab 中 item 的数目和存储时长(最后一次访问距离现在的秒数)
     * @return Map<String, Map<String, String>>
     */
    public Map<String, Map<String, String>> statsItems() {
        return this.statsItems(this.defaultName);
    }

    /**
     * 获取各个 slab 中 item 的数目和存储时长(最后一次访问距离现在的秒数)
     * @param name 缓存名称
     * @return Map<String, Map<String, String>>
     */
    public Map<String, Map<String, String>> statsItems(String name) {
        if(log.isDebugEnabled()) log.debug("{}", name);
        MemCachedClient client = clients.get(name);
        if(client == null) return new HashMap<>();
        return client.statsItems();
    }

    /**
     * 获取各个slab的信息，包括chunk的大小、数目、使用情况等
     * @return Map<String, Map<String, String>>
     */
    public Map<String, Map<String, String>> statsSlabs() {
        return this.statsSlabs(this.defaultName);
    }

    /**
     * 获取各个slab的信息，包括chunk的大小、数目、使用情况等
     * @param name 缓存名称
     * @return Map<String, Map<String, String>>
     */
    public Map<String, Map<String, String>> statsSlabs(String name) {
        if(log.isDebugEnabled()) log.debug("{}", name);
        MemCachedClient client = clients.get(name);
        if(client == null) return new HashMap<>();
        return client.statsSlabs();
    }

    /**
     * 获取 Memcached 内存中的缓存项，只能列出最近存入的 Key，并且无法保证完整性（某些 Key 可能不会显示）
     * @param slabId 插槽ID
     * @param limit 返回数据的数量
     * @return Map<String, Map<String, String>>
     */
    public Map<String, Map<String, String>> statsCacheDump(int slabId, int limit) {
        return this.statsCacheDump(this.defaultName, slabId, limit);
    }

    /**
     * 获取 Memcached 内存中的缓存项，只能列出最近存入的 Key，并且无法保证完整性（某些 Key 可能不会显示）
     * @param name 缓存名称
     * @param slabId 插槽ID
     * @param limit 返回数据的数量
     * @return Map<String, Map<String, String>>
     */
    public Map<String, Map<String, String>> statsCacheDump(String name, int slabId, int limit) {
        if(log.isDebugEnabled()) log.debug("{} => {} / {}", name, slabId, limit);
        MemCachedClient client = clients.get(name);
        if(client == null) return new HashMap<>();
        return client.statsCacheDump(slabId, limit);
    }

    /**
     * 获取统计信息
     * @return List<MemcachedStats>
     */
    public List<MemcachedStats> statsToModel() {
        return this.statsToModel(this.defaultName);
    }

    /**
     * 获取各个 slab 中 item 的数目和存储时长(最后一次访问距离现在的秒数)
     * @return List<MemcachedStatsItems>
     */
    public List<MemcachedStatsItems> statsItemsToModel() {
        return this.statsItemsToModel(this.defaultName);
    }

    /**
     * 获取各个slab的信息，包括chunk的大小、数目、使用情况等
     * @return List<MemcachedStatsSlabs>
     */
    public List<MemcachedStatsSlabs> statsSlabsToModel() {
        return this.statsSlabsToModel(this.defaultName);
    }

    /**
     * 获取 Memcached 内存中的缓存项，只能列出最近存入的 Key，并且无法保证完整性（某些 Key 可能不会显示）
     * @param slabId 插槽ID
     * @param limit 返回数据的数量
     * @return List<MemcachedCacheDump>
     */
    public List<MemcachedCacheDump> statsSlabsKeyToModel(int slabId, int limit) {
        return this.statsSlabsKeyToModel(this.defaultName, slabId, limit);
    }

    /**
     * 获取统计信息
     * @param name 缓存名称
     * @return List<MemcachedStats>
     */
    public List<MemcachedStats> statsToModel(String name) {
        if(log.isDebugEnabled()) log.debug("{}", name);
        Map<String, Map<String, String>> statsInfo = this.stats(name);
        if(statsInfo.isEmpty()) return new ArrayList<>();

        List<MemcachedStats> results = new ArrayList<>();
        for (Map.Entry<String, Map<String, String>> entry : statsInfo.entrySet()) {
            MemcachedStats stats = new MemcachedStats(entry.getKey());
            for (Map.Entry<String, String> item : entry.getValue().entrySet()) {
                setFieldValue(item.getKey(), item.getValue(), stats);
            }
            results.add(stats);
        }

        return results;
    }

    /**
     * 获取各个 slab 中 item 的数目和存储时长(最后一次访问距离现在的秒数)
     * @param name 缓存名称
     * @return List<MemcachedStatsItems>
     */
    public List<MemcachedStatsItems> statsItemsToModel(String name) {
        if(log.isDebugEnabled()) log.debug("{}", name);
        Map<String, Map<String, String>> statsItemInfo = this.statsItems(name);
        if(statsItemInfo.isEmpty()) return new ArrayList<>();

        List<MemcachedStatsItems> results = new ArrayList<>();
        for (Map.Entry<String, Map<String, String>> entry : statsItemInfo.entrySet()) {
            MemcachedStatsItems status = new MemcachedStatsItems(entry.getKey());

            Map<String, MemcachedStatsItem> items = new HashMap<>();
            for (Map.Entry<String, String> item : entry.getValue().entrySet()) {
                String key = item.getKey();
                key = key.replace("items:", "");

                String[] keys = key.split(":");
                String slabId = keys[0];
                String field = keys[1];

                MemcachedStatsItem statsItem = items.get(slabId);
                if(statsItem == null) statsItem = new MemcachedStatsItem(slabId);

                setFieldValue(field, item.getValue(), statsItem);

                items.put(slabId, statsItem);
            }

            items.values().forEach(status::addItems);
            results.add(status);
        }

        return results;
    }

    /**
     * 获取各个slab的信息，包括chunk的大小、数目、使用情况等
     * @param name 缓存名称
     * @return List<MemcachedStatsSlabs>
     */
    public List<MemcachedStatsSlabs> statsSlabsToModel(String name) {
        if(log.isDebugEnabled()) log.debug("{}", name);
        Map<String, Map<String, String>> statsSlabInfo = this.statsSlabs(name);
        if(statsSlabInfo.isEmpty()) return new ArrayList<>();

        List<MemcachedStatsSlabs> results = new ArrayList<>();
        for (Map.Entry<String, Map<String, String>> entry : statsSlabInfo.entrySet()) {
            MemcachedStatsSlabs slabs = new MemcachedStatsSlabs(entry.getKey());

            Map<String, MemcachedStatsSlab> items = new HashMap<>();
            for (Map.Entry<String, String> item : entry.getValue().entrySet()) {
                String key = item.getKey();

                if(!key.contains(":")) {
                    setFieldValue(key, item.getValue(), slabs);
                    continue;
                }

                String[] keys = key.split(":");
                String slabId = keys[0];
                String field = keys[1];

                MemcachedStatsSlab statsItem = items.get(slabId);
                if(statsItem == null) statsItem = new MemcachedStatsSlab(slabId);

                setFieldValue(field, item.getValue(), statsItem);

                items.put(slabId, statsItem);
            }

            items.values().forEach(slabs::addItems);
            results.add(slabs);
        }

        return results;
    }

    /**
     * 获取 Memcached 内存中的缓存项，只能列出最近存入的 Key，并且无法保证完整性（某些 Key 可能不会显示）
     * @param name 缓存名称
     * @param slabId 插槽ID
     * @param limit 返回数据的数量
     * @return List<MemcachedCacheDump>
     */
    public List<MemcachedCacheDump> statsSlabsKeyToModel(String name, int slabId, int limit) {
        if(log.isDebugEnabled()) log.debug("{} => {} / {}", name, slabId, limit);
        Map<String, Map<String, String>> statsCacheDumpInfo = this.statsCacheDump(name, slabId, limit);

        List<MemcachedCacheDump> result = new ArrayList<>();
        for (Map.Entry<String, Map<String, String>> entry : statsCacheDumpInfo.entrySet()) {
            MemcachedCacheDump cacheDump = new MemcachedCacheDump(entry.getKey());

            for (Map.Entry<String, String> item : entry.getValue().entrySet()) cacheDump.addKey(item.getKey());

            result.add(cacheDump);
        }

        return result;
    }

    // public Map<String, Object> getMulti(String name, String[] keys, Integer[] hash) {
    //     MemCachedClient client = clients.get(name);
    //     if(client == null) return null;
    //     return client.getMulti(keys, hash);
    // }
    //
    // public Map<String, Object> getMulti(String name, String[] keys, Integer[] hash, boolean isStr) {
    //     MemCachedClient client = clients.get(name);
    //     if(client == null) return null;
    //     return client.getMulti(keys, hash, isStr);
    // }
    //
    // public Object[] getMultiArray(String name, String[] keys, Integer[] hash) {
    //     MemCachedClient client = clients.get(name);
    //     if(client == null) return null;
    //     return client.getMultiArray(keys, hash);
    // }
    //
    // public Object[] getMultiArray(String name, String[] keys, Integer[] hash, boolean isStr) {
    //     MemCachedClient client = clients.get(name);
    //     if(client == null) return null;
    //     return client.getMultiArray(keys, hash, isStr);
    // }
    //
    // private boolean set(String name, String key, Object value, Integer hash) {
    //     return this.set(name, key, value, 0L, hash);
    // }
    //
    // private boolean set(String name, String key, Object value, long expired, Integer hash) {
    //     MemCachedClient client = clients.get(name);
    //     return client != null && client.set(key, value, new Date(expired), hash);
    // }
    //
    // private boolean add(String name, String key, Object value, Integer hash) {
    //     return this.add(name, key, value, 0L, hash);
    // }
    //
    // private boolean add(String name, String key, Object value, long expired, Integer hash) {
    //     MemCachedClient client = clients.get(name);
    //     return client != null && client.add(key, value, new Date(expired), hash);
    // }
    //
    // private boolean replace(String name, String key, Object value, Integer hash) {
    //     return this.replace(name, key, value, 0L, hash);
    // }
    //
    // private boolean replace(String name, String key, Object value, long expired, Integer hash) {
    //     MemCachedClient client = clients.get(name);
    //     return client != null && client.replace(key, value, new Date(expired), hash);
    // }
    //
    // private boolean storeCounter(String name, String key, Long value, Integer hash) {
    //     return this.storeCounter(name, key, value, 0L, hash);
    // }
    //
    // private boolean storeCounter(String name, String key, Long value, long expired, Integer hash) {
    //     MemCachedClient client = clients.get(name);
    //     return client != null && client.storeCounter(key, value, new Date(expired), hash);
    // }
    //
    // private long getCounter(String name, String key, Integer hash) {
    //     MemCachedClient client = clients.get(name);
    //     if(client == null) return -1L;
    //     return client.getCounter(key, hash);
    // }
    //
    // private long addOrIncr(String name, String key, long incr, Integer hash) {
    //     MemCachedClient client = clients.get(name);
    //     if(client == null) return -1L;
    //     return client.addOrIncr(key, incr, hash);
    // }
    //
    // private long addOrDecr(String name, String key, long decr, Integer hash) {
    //     MemCachedClient client = clients.get(name);
    //     if(client == null) return -1L;
    //     return client.addOrDecr(key, decr, hash);
    // }
    //
    // private long incr(String name, String key, long value, Integer hash) {
    //     MemCachedClient client = clients.get(name);
    //     if(client == null) return -1L;
    //     return client.incr(key, value, hash);
    // }
    //
    // private long decr(String name, String key, long value, Integer hash) {
    //     MemCachedClient client = clients.get(name);
    //     if(client == null) return -1L;
    //     return client.decr(key, value, hash);
    // }
    //
    // private Object get(String name, String key, Integer hash) {
    //     MemCachedClient client = clients.get(name);
    //     if(client == null) return null;
    //     return client.get(key, hash);
    // }
    //
    // private Object get(String name, String key, Integer hash, boolean isStr) {
    //     MemCachedClient client = clients.get(name);
    //     if(client == null) return null;
    //     return client.get(key, hash, isStr);
    // }
    //
    // private MemcachedItem gets(String name, String key, Integer hash) {
    //     MemCachedClient client = clients.get(name);
    //     if(client == null) return null;
    //     return client.gets(key, hash);
    // }
    //
    // private boolean cas(String name, String key, Object value, Integer hash, long casUnique) {
    //     MemCachedClient client = clients.get(name);
    //     return client != null && client.cas(key, value, hash, casUnique);
    // }
    //
    // private boolean cas(String name, String key, Object value, long expires, Integer hash, long casUnique) {
    //     MemCachedClient client = clients.get(name);
    //     return client != null && client.cas(key, value, new Date(expires), hash, casUnique);
    // }
    //
    // private boolean append(String name, String key, Object value, Integer hash) {
    //     MemCachedClient client = clients.get(name);
    //     return client != null && client.append(key, value, hash);
    // }
    //
    // private boolean prepend(String name, String key, Object value, Integer hash) {
    //     MemCachedClient client = clients.get(name);
    //     return client != null && client.prepend(key, value, hash);
    // }
    //
    // public boolean flushAll(String[] servers) {
    //     return this.flushAll(this.defaultName, servers);
    // }
    //
    // public boolean flushAll(String name, String[] servers) {
    //     MemCachedClient client = clients.get(name);
    //     return client != null && client.flushAll(servers);
    // }
    //
    // public Map<String, Map<String, String>> stats(String[] servers) {
    //     return this.stats(this.defaultName, servers);
    // }
    //
    // public Map<String, Map<String, String>> stats(String name, String[] servers) {
    //     MemCachedClient client = clients.get(name);
    //     if(client == null) return new HashMap<>();
    //     return client.stats(servers);
    // }
    //
    // public Map<String, Map<String, String>> statsItems(String[] servers) {
    //     return this.statsItems(this.defaultName, servers);
    // }
    //
    // public Map<String, Map<String, String>> statsItems(String name, String[] servers) {
    //     MemCachedClient client = clients.get(name);
    //     if(client == null) return new HashMap<>();
    //     return client.statsItems(servers);
    // }
    //
    // public Map<String, Map<String, String>> statsSlabs(String[] servers) {
    //     return this.statsSlabs(this.defaultName, servers);
    // }
    //
    // public Map<String, Map<String, String>> statsSlabs(String name, String[] servers) {
    //     MemCachedClient client = clients.get(name);
    //     if(client == null) return new HashMap<>();
    //     return client.statsSlabs(servers);
    // }
    //
    // public Map<String, Map<String, String>> statsCacheDump(String[] servers, int slabId, int limit) {
    //     return this.statsCacheDump(this.defaultName, servers, slabId, limit);
    // }
    //
    // public Map<String, Map<String, String>> statsCacheDump(String name, String[] servers, int slabId, int limit) {
    //     MemCachedClient client = clients.get(name);
    //     if(client == null) return new HashMap<>();
    //     return client.statsCacheDump(servers, slabId, limit);
    // }
    //
    // public boolean sync(String name, String var1, Integer var2) {
    //     MemCachedClient client = clients.get(name);
    //     if(client == null) return false;
    //     return client.sync(var1, var2);
    // }
    //
    // public boolean sync(String name, String var1) {
    //     MemCachedClient client = clients.get(name);
    //     if(client == null) return false;
    //     return client.sync(var1);
    // }
    //
    // public boolean syncAll(String name, ) {
    //     MemCachedClient client = clients.get(name);
    //     if(client == null) return false;
    //     return client.syncAll();
    // }
    //
    // public boolean syncAll(String name, String[] var1) {
    //     MemCachedClient client = clients.get(name);
    //     if(client == null) return false;
    //     return client.syncAll(var1);
    // }

    private void setFieldValue(String field, String value, Object clazz) {
        try {
            String setMethodName = "set" + Character.toUpperCase(field.charAt(0)) + field.substring(1);
            Method setMethod = clazz.getClass().getMethod(setMethodName, String.class);
            setMethod.invoke(clazz, value.replace("\r\n", ""));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
