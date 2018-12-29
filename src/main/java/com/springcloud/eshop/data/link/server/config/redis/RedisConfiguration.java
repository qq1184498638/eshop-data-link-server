package com.springcloud.eshop.data.link.server.config.redis;

import com.springcloud.eshop.common.server.support.cache.JedisPoolWriper;
import com.springcloud.eshop.common.server.support.cache.JedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfiguration {
    @Value("${spring.redis.host}")
    private String hostname;
    @Value("${spring.redis.port}")
    private int port;
    @Value("${spring.redis.jedis.pool.max-active}")
    private int maxTotal;
    @Value("${spring.redis.jedis.pool.max-idle}")
    private int maxIdle;
    @Value("${spring.redis.jedis.pool.max-wait}")
    private long maxWaitMillis;


    @Autowired
    private JedisPoolConfig jedisPoolConfig;


    @Autowired
    private JedisPoolWriper jedisPoolWriper;

    @Autowired
    private JedisUtil jedisUtil;


    /**
     * 创建 redis 连接池的设置
     */
    @Bean(name = "jedisPoolConfig")
    public JedisPoolConfig getJedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(maxTotal);
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
        return  jedisPoolConfig;
    }

    /**
     * 创建Redis连接池， 并做相关配置
     */

    @Bean(name = "jedisPoolWriper")
    public JedisPoolWriper getJedisPoolWriper() {
        JedisPoolWriper jedisPoolWriper = new JedisPoolWriper(jedisPoolConfig, hostname, port);
        return jedisPoolWriper;
    }

    /**
     * 创建Redis工具类，封装好Redis的连接以进行相关的操作
     */
    @Bean(name = "jedisUtils")
    public JedisUtil getJedisUtil() {
        JedisUtil jedisUtil = new JedisUtil();
        jedisUtil.setJedisPool(jedisPoolWriper);
        return jedisUtil;
    }

    /**
     * Redis 的key操作
     */
    @Bean(name = "jedisKeys")
    public JedisUtil.Keys getJedisKeys() {
        JedisUtil.Keys jedisKeys = jedisUtil.new Keys();
        return jedisKeys;
    }
    /**
     * Redis 的 Strings操作
     */
    @Bean(name = "jedisStrings")
    public JedisUtil.Strings getJedisStrings() {
        JedisUtil.Strings jedisStrings = jedisUtil.new Strings();
        return jedisStrings;
    }
    /**
     * Redis 的Lists操作
     */
    @Bean(name = "jedisLists")
    public JedisUtil.Lists getJedisLists() {
        JedisUtil.Lists jedisLists = jedisUtil.new Lists();
        return jedisLists;
    }
    /**
     * Redis 的Sets操作
     */
    @Bean(name = "jedisSets")
    public JedisUtil.Sets getJedisSets() {
        JedisUtil.Sets jedisSets = jedisUtil.new Sets();
        return jedisSets;
    }
    /**
     * Redis 的Hash操作
     */
    @Bean(name = "jedisHash")
    public JedisUtil.Hash getJedisHash() {
        JedisUtil.Hash jedisHash = jedisUtil.new Hash();
        return jedisHash;
    }
}
