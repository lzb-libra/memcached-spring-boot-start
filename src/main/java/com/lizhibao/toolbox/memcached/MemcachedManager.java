package com.lizhibao.toolbox.memcached;

import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;
import com.schooner.MemCached.TransCoder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 管理多个 Memcached 客户端
 * @author lizhibao
 * @date 2025-02-13
 */
@Slf4j
@Component
public class MemcachedManager {
    private final MemcachedProperties properties;

    @Getter
    private final Map<String, MemCachedClient> clients = new HashMap<>();

    public MemcachedManager(MemcachedProperties properties) {
        this.properties = properties;
        init();
    }

    private void init() {
        if (properties.getNodes() == null || properties.getNodes().isEmpty()) {
            throw new RuntimeException("Please configure the cache connection address");
        }

        for (MemcachedConfig node : properties.getNodes()) {
            if(!StringUtils.hasText(node.getName())) node.setName(node.getServers());
            log.info("{}", node);

            SockIOPool pool = SockIOPool.getInstance(node.getName());
            // memcached服务器地址配置
            pool.setServers(node.getServers().split(","));
            // 设置连接池可用cache服务器的权重，和server数组的位置一一对应
            pool.setWeights(Arrays.stream(node.getWeights().split(",")).map(Integer::parseInt).toArray(Integer[]::new));
            // 初始化时对每个服务器建立的连接数目
            pool.setInitConn(node.getInitConn());
            // 每个服务器建立最小的连接数，当维护线程发现与某个服务器建立连接数目小于这个数目时会弥补剩下的连接
            pool.setMinConn(node.getMinConn());
            // 每个服务器建立最大的连接数，当维护线程发现与某个服务器建立连接数目大于这个数目时就会逐个检查这些连接的空闲时间是否大于maxConn，如果大于会关闭这些连接，直到连接数等于maxConn
            pool.setMaxConn(node.getMaxConn());
            // 最大空闲时间
            pool.setMaxIdle(node.getMaxIdle());
            // 最长租用时间，其使用主要有两点，一是维护线程会检查正在被租用的连接，如果发现已经被租用的时间超过这个值得，
            // 会将其从被租用的记录里剔除，并关闭这个连接；另一个应用是上层进行MUTIL操作时，读取所有的数据的时间不能超过这个时间。
            pool.setMaxBusyTime(node.getMaxBusyTime());
            // 维护线程周期进行工作，其每次休眠时间。设置为0，维护线程不启动。维护线程主要通过log输出socket的运行状况，监测连接数目及空闲等待时间等参数以控制连接创建和关闭。
            pool.setMaintSleep(node.getMaintSleep());
            // Socket的参数，设置是否使用Nagle算法，因为我们的通讯数据量通常都比较大（相对TCP控制数据）而且要求响应及时，因此该值需要设置为false（默认是true）如果是true在写数据时不缓冲，立即发送出去
            pool.setNagle(node.getNagle());
            // Socket阻塞读取数据的超时时间
            pool.setSocketTO(node.getSocketTimeout());
            // Socket阻塞建立连接的等待时间
            pool.setSocketConnectTO(node.getSocketConnectTimeout());
            // 设置连接心跳监测开关
            // 根据key&hashCode获取SockIO时，通过hash bucket得到SockIO后，如果这个值是true会检查Socket是否已经连接，
            // 如果连接建立正常还会向服务器发送“version\r\n”的指令，并读取数据，这个过程没有出错才会返回SockIO给上层用，否则返回NULL。所以一般设置为false。
            // 设为true则每次通信都要进行连接是否有效的监测，造成通信次数倍增，加大网络负载，因此该参数应该在对HA要求比较高的场合设为TRUE，默认状态是false。
            pool.setAliveCheck(node.getAliveCheck());
            // 设置连接失败恢复开关，设置为TRUE，当宕机的服务器启动或中断的网络连接后，这个socket连接还可继续使用，否则将不再使用，默认状态是true，建议保持默认。
            pool.setFailback(node.getFailBack());
            // 设置容错开关，设置为TRUE，当当前socket不可用时，程序会自动查找可用连接并返回，否则返回NULL，默认状态是true，建议保持默认。
            pool.setFailover(node.getFailOver());
            // 设置hash算法，采用前三种hash算法的时候，查找cache服务器使用余数方法。采用最后一种hash算法查找cache服务时使用consistent方法
            //   0 使用String.hashCode()获得hash code,该方法依赖JDK，可能和其他客户端不兼容，建议不使用
            //   1 使用original 兼容hash算法，兼容其他客户端
            //   2 使用CRC32兼容hash算法，兼容其他客户端，性能优于original算法
            //   3 使用MD5 hash算法
            pool.setHashingAlg(node.getHashingAlg());
            pool.initialize();

            MemCachedClient client = getMemCachedClient(node);
            if(client != null) clients.put(node.getName(), client);
        }
    }

    private static MemCachedClient getMemCachedClient(MemcachedConfig node) {
        try {
            MemCachedClient client = new MemCachedClient(node.getName());
            if(node.getEnableKeyStrictMode() != null) client.setSanitizeKeys(node.getEnableKeyStrictMode());
            if(node.getIsPrimitiveAsString() != null) client.setPrimitiveAsString(node.getIsPrimitiveAsString());
            if(StringUtils.hasText(node.getDefaultEncoding())) client.setDefaultEncoding(node.getDefaultEncoding());
            if(StringUtils.hasText(node.getTransCoderClass())) {
                Class<?> clazz = Class.forName(node.getTransCoderClass());
                if(TransCoder.class.isAssignableFrom(clazz)) {
                    TransCoder transCoder = (TransCoder) clazz.getDeclaredConstructor().newInstance();
                    client.setTransCoder(transCoder);
                }
            }
            return client;
        } catch (Exception e) {
            log.error("", e);
        }

        return null;
    }

}
