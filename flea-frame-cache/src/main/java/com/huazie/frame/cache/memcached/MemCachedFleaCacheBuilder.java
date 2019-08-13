package com.huazie.frame.cache.memcached;

import com.huazie.frame.cache.AbstractFleaCache;
import com.huazie.frame.cache.IFleaCacheBuilder;
import com.huazie.frame.cache.config.CacheParams;
import com.huazie.frame.cache.config.CacheServer;

import java.util.List;

/**
 * <p> MemCached的Flea缓存建造者实现 </p>
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public class MemCachedFleaCacheBuilder implements IFleaCacheBuilder {

    @Override
    public AbstractFleaCache build(String name, List<CacheServer> cacheServerList, CacheParams cacheParams) {
        return null;
    }
}
