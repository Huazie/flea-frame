package com.huazie.frame.cache.config;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * <p> Flea缓存根节点 </p>
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public class FleaCache {

    private Caches caches; // Flea缓存集

    private CacheFiles cacheFiles; // 其他缓存定义配置文件集合

    public Caches getCaches() {
        return caches;
    }

    public void setCaches(Caches caches) {
        this.caches = caches;
    }

    public CacheFiles getCacheFiles() {
        return cacheFiles;
    }

    public void setCacheFiles(CacheFiles cacheFiles) {
        this.cacheFiles = cacheFiles;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
