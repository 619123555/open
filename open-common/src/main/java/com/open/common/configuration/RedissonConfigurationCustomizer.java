package com.open.common.configuration;

import org.redisson.config.Config;

@FunctionalInterface
public interface RedissonConfigurationCustomizer {

    /**
     * Customize the RedissonClient configuration.
     * @param configuration the {@link Config} to customize
     */
    void customize(final Config configuration);
}
