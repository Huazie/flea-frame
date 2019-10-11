package com.huazie.frame.common.pool;

import com.huazie.frame.common.CommonConstants;
import com.huazie.frame.common.FleaConfigManager;
import com.huazie.frame.common.config.ConfigItem;
import com.huazie.frame.common.util.ObjectUtils;
import com.huazie.frame.common.util.ReflectUtils;
import com.huazie.frame.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * <p> Flea对象池工厂 </p>
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public class FleaObjectPoolFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(FleaObjectPoolFactory.class);

    private static final ConcurrentMap<String, FleaObjectPool> fleaObjectPools = new ConcurrentHashMap<String, FleaObjectPool>();

    /**
     * <p> 默认Flea对象池（指定对象Class） </p>
     *
     * @param objClazz     对象Class
     * @param objPoolClazz 对象池Class
     * @return 默认对象池
     * @since 1.0.0
     */
    public static <T extends FleaObjectPool> T getFleaObjectPool(Class<?> objClazz, Class<T> objPoolClazz) {
        return getFleaObjectPool(CommonConstants.FleaPoolConstants.DEFAUTL_POOL_NAME, objClazz, objPoolClazz);
    }

    /**
     * <p> 指定对象池名的Flea对象池（指定对象Class）</p>
     *
     * @param poolName     对象池名
     * @param objClazz     对象Class
     * @param objPoolClazz 对象池Class
     * @return 指定对象池名的Flea对象池
     * @since 1.0.0
     */
    public static <T extends FleaObjectPool> T getFleaObjectPool(String poolName, Class<?> objClazz, Class<T> objPoolClazz) {
        if (StringUtils.isBlank(poolName) || ObjectUtils.isEmpty(objClazz)) {
            return null;
        }
        String poolNameKey = poolName + CommonConstants.SymbolConstants.UNDERLINE + objClazz.getName();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("FleaObjectPoolFactory##getFleaObjectPool(String, Class) Pool Name Key = {}", poolNameKey);
        }
        if (!fleaObjectPools.containsKey(poolNameKey)) {
            synchronized (fleaObjectPools) {
                if (!fleaObjectPools.containsKey(poolNameKey)) {
                    fleaObjectPools.put(poolNameKey, build(poolName, objClazz));
                }
            }
        }

        Object objPool = fleaObjectPools.get(poolNameKey);
        if (objPoolClazz.isInstance(objPool)) {
            return objPoolClazz.cast(objPool);
        } else {
            return null;
        }
    }

    /**
     * <p> 初始化Flea对象池创建 </p>
     *
     * @param poolName 对象池名
     * @param objClazz 指定对象Class
     * @since 1.0.0
     */
    private static FleaObjectPool build(String poolName, Class<?> objClazz) {
        String className = objClazz.getSimpleName();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("FleaObjectPoolFactory##build(String, Class) Object Simple Name = {}", className);
        }
        ConfigItem configItem = FleaConfigManager.getConfigItem(CommonConstants.FleaPoolConstants.FLEA_OBJECT_POOL, className);
        if (ObjectUtils.isEmpty(configItem)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("FleaObjectPoolFactory##build(String, Class) Can not find the builder by the <config-item key=\"{}\"> from <config-items key=\"{}\"> in [flea-config.xml]",
                        className, CommonConstants.FleaPoolConstants.FLEA_OBJECT_POOL);
            }
            return null;
        }

        String builderImpl = configItem.getValue();
        if (StringUtils.isBlank(builderImpl)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("FleaObjectPoolFactory##build(String, Class) The builder is empty, found by the <config-item key=\"{}\"> from <config-items key=\"{}\"> in [flea-config.xml]",
                        className, CommonConstants.FleaPoolConstants.FLEA_OBJECT_POOL);
            }
            return null;
        }

        FleaObjectPool fleaObjectPool = null;

        IFleaObjectPoolBuilder fleaObjectPoolBuilder = (IFleaObjectPoolBuilder) ReflectUtils.newInstance(builderImpl);
        if (ObjectUtils.isNotEmpty(fleaObjectPoolBuilder)) {
            // 调用指定的类，创建Flea对象池
            fleaObjectPool = fleaObjectPoolBuilder.build(poolName);
        }

        return fleaObjectPool;
    }
}