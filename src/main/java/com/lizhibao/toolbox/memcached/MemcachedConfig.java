package com.lizhibao.toolbox.memcached;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 配置属性类
 * @author lizhibao
 * @date 2025-02-13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemcachedConfig {
    /**
     * memcached 别名
     */
    private String name;
    /**
     * memcached 服务器地址。
     */
    private String servers;
    /**
     * 连接池可用cache服务器的权重，和server数组的位置一一对应。
     * 其实现方法是通过根据每个权重在连接池的bucket中放置同样数目的server，因此所有权重的最大公约数应该是1，不然会引起bucket资源的浪费。
     */
    private String weights = "1,1";
    /**
     * 初始化时对每个服务器建立的连接数目。
     */
    private Integer initConn = 10;
    /**
     * 每个服务器建立最小的连接数，当维护线程发现与某个服务器建立连接数目小于这个数目时会弥补剩下的连接。
     */
    private Integer minConn = 10;
    /**
     * 每个服务器建立最大的连接数，当维护线程发现与某个服务器建立连接数目大于这个数目时就会逐个检查这些连接的空闲时间是否大于maxConn，如果大于会关闭这些连接，直到连接数等于maxConn。
     */
    private Integer maxConn = 50;
    /**
     * 可用连接池的最长等待时间。
     */
    private Integer maxIdle = 1000;
    /**
     * 最长租用时间，其使用主要有两点，一是维护线程会检查正在被租用的连接，如果发现已经被租用的时间超过这个值得，会将其从被租用的记录里剔除，并关闭这个连接；另一个应用是上层进行MUTIL操作时，读取所有的数据的时间不能超过这个时间。
     */
    private Integer maxBusyTime = 1000;
    /**
     * 维护线程周期进行工作，其每次休眠时间。
     * 设置为0，维护线程不启动。
     * 维护线程主要通过log输出socket的运行状况，监测连接数目及空闲等待时间等参数以控制连接创建和关闭。
     */
    private Integer maintSleep = 30;
    /**
     * Socket阻塞读取数据的超时时间
     */
    private Integer socketTimeout = 3000;
    /**
     * Socket阻塞建立连接的等待时间
     */
    private Integer socketConnectTimeout = 3000;
    /**
     * 设置hash算法，采用前三种hash算法的时候，查找cache服务器使用余数方法。采用最后一种hash算法查找cache服务时使用consistent方法
     *   0 使用String.hashCode()获得hash code,该方法依赖JDK，可能和其他客户端不兼容，建议不使用
     *   1 使用original 兼容hash算法，兼容其他客户端
     *   2 使用CRC32兼容hash算法，兼容其他客户端，性能优于original算法
     *   3 使用MD5 hash算法
     */
    private Integer hashingAlg = 2;
    /**
     * Socket的参数，设置是否使用Nagle算法，因为我们的通讯数据量通常都比较大（相对TCP控制数据）而且要求响应及时，因此该值需要设置为false（默认是true）如果是true在写数据时不缓冲，立即发送出去
     */
    private Boolean nagle = true;
    /**
     * 设置连接心跳监测开关
     * 根据key&hashCode获取SockIO时，通过hash bucket得到SockIO后，如果这个值是true会检查Socket是否已经连接，
     * 如果连接建立正常还会向服务器发送“version\r\n”的指令，并读取数据，这个过程没有出错才会返回SockIO给上层用，否则返回NULL。所以一般设置为false。
     * 设为true则每次通信都要进行连接是否有效的监测，造成通信次数倍增，加大网络负载，因此该参数应该在对HA要求比较高的场合设为TRUE，默认状态是false。
     */
    private Boolean aliveCheck = false;
    /**
     * 设置连接失败恢复开关，设置为TRUE，当宕机的服务器启动或中断的网络连接后，这个socket连接还可继续使用，否则将不再使用，默认状态是true，建议保持默认。
     */
    private Boolean failBack = true;
    /**
     * 设置容错开关，设置为true，当当前socket不可用时，程序会自动查找可用连接并返回，否则返回NULL，默认状态是true，建议保持默认。
     */
    private Boolean failOver = true;
    /**
     * 控制是否对存储的键（key）进行“键清理”操作
     * true: 启用键清理，这意味着键会经过过滤，移除掉不符合规则的字符（例如非法字符、空格等）。
     * false: 禁用键清理，允许存储原始键，即使它们包含某些非法或不符合规范的字符。
     */
    private Boolean enableKeyStrictMode;
    /**
     * 设置缓存的默认编码方式，如 "UTF-8"、"ISO-8859-1" 等。
     */
    private String defaultEncoding;
    /**
     * 控制是否将基本数据类型（如 int、long、boolean 等）作为字符串存储在 Memcached 中。
     * true: 启用将基本类型转换为字符串存储，意味着 int 会被存储为 "123" 等字符串形式。
     * false: 禁用将基本类型转换为字符串存储，数据会以原始类型（例如 int）直接存储。
     */
    private Boolean isPrimitiveAsString;
    /**
     * 设置一个自定义的 TransCoder，它用于编码和解码存储到缓存中的数据。
     */
    private String transCoderClass;
}
