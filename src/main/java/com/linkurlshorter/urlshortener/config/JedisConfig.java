package com.linkurlshorter.urlshortener.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

/**
 * Configuration class for setting up Jedis, a Java Redis client.
 *
 * <p>This class configures a JedisPool bean, which serves as a connection pool to manage connections
 * to the Redis server. It specifies the Redis server host and port using properties defined in
 * the application.properties file. Additionally, it configures various connection pool parameters
 * such as maximum total connections, maximum idle connections, and eviction settings to optimize
 * connection management and performance.
 *
 * @author Egor Sivenko
 * @see redis.clients.jedis.JedisPool
 * @see redis.clients.jedis.JedisPoolConfig
 */
@Configuration
public class JedisConfig {

    @Value("${REDIS_HOST:localhost}")
    private String host;

    @Value("${REDIS_PORT:6379}")
    private int port;

    /**
     * Creates and configures a JedisPool bean for managing Redis connections.
     *
     * @return the configured JedisPool bean
     */
    @Bean
    public JedisPool jedisPool() {
        return new JedisPool(buildPoolConfig(), host, port);
    }

    /**
     * Builds and configures the JedisPoolConfig for the JedisPool bean.
     *
     * @return the configured JedisPoolConfig object
     */
    private JedisPoolConfig buildPoolConfig() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(128);
        poolConfig.setMaxIdle(128);
        poolConfig.setMinIdle(16);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);
        poolConfig.setMinEvictableIdleDuration(Duration.ofSeconds(60));
        poolConfig.setTimeBetweenEvictionRuns(Duration.ofSeconds(30));
        poolConfig.setNumTestsPerEvictionRun(3);
        poolConfig.setBlockWhenExhausted(true);
        poolConfig.setJmxEnabled(false);
        return poolConfig;
    }
}
