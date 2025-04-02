基于`com.danga:java_memcached:release_2.6.6`客户端的实现，可多实例。

```yml
spring:
    memcached:
        nodes:
            - name: simGroup
              servers: 172.0.0.1:1234,172.0.0.2:1234
            - name: simInfo
              servers: 172.0.0.1:1235,172.0.0.2:1235
            - name: scInfo
              servers: 172.0.0.1:1236,172.0.0.2:1236
```