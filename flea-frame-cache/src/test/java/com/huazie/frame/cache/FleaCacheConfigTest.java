package com.huazie.frame.cache;

import com.huazie.frame.cache.common.CacheConfigManager;
import com.huazie.frame.cache.common.CacheConstants;
import com.huazie.frame.cache.common.CacheXmlDigesterHelper;
import com.huazie.frame.cache.config.Cache;
import com.huazie.frame.cache.config.CacheData;
import com.huazie.frame.cache.config.CacheGroup;
import com.huazie.frame.cache.config.CacheItem;
import com.huazie.frame.cache.config.CacheParams;
import com.huazie.frame.cache.config.CacheServer;
import com.huazie.frame.cache.config.FleaCache;
import com.huazie.frame.common.slf4j.FleaLogger;
import com.huazie.frame.common.slf4j.impl.FleaLoggerProxy;
import org.junit.Test;

import java.util.List;

/**
 * <p>  </p>
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public class FleaCacheConfigTest {

    private static final FleaLogger LOGGER = FleaLoggerProxy.getProxyInstance(FleaCacheConfigTest.class);

    @Test
    public void testGetFleaCache() {
        Cache cache = CacheConfigManager.getCache("fleaparadetail");
        LOGGER.debug("Cache={}", cache);
        CacheData cacheData = CacheConfigManager.getCacheData(cache.getType());
        LOGGER.debug("CacheData={}", cacheData);
        CacheGroup cacheGroup = CacheConfigManager.getCacheGroup(cacheData.getGroup());
        LOGGER.debug("CacheGroup={}", cacheGroup);
        CacheParams cacheParams = CacheConfigManager.getCacheParams();
        LOGGER.debug("CacheParams={}", cacheParams);
        List<CacheServer> cacheServers = CacheConfigManager.getCacheServer(cacheGroup.getGroup());
        LOGGER.debug("CacheServers={}", cacheServers);
    }

    @Test
    public void testGetCacheItem() {
        CacheItem cacheItem = CacheConfigManager.getCacheItem(CacheConstants.FleaCacheConfigConstants.FLEA_CACHE_INIT, CacheConstants.FleaCacheConfigConstants.SYSTEM_NAME);
        LOGGER.debug("CacheItem={}", cacheItem);
    }

    @Test
    public void testCacheFiles() {
        FleaCache fleaCache = CacheXmlDigesterHelper.getInstance().getFleaCache();
        LOGGER.debug("CacheFiles={}", fleaCache.getCacheFiles());
    }
}
