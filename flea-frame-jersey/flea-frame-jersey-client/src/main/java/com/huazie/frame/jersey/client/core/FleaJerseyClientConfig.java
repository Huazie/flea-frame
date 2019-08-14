package com.huazie.frame.jersey.client.core;

import com.huazie.frame.common.FleaConfigManager;
import com.huazie.frame.common.FleaFrameManager;
import com.huazie.frame.common.IFleaUser;
import com.huazie.frame.common.util.ObjectUtils;
import com.huazie.frame.common.util.StringUtils;
import com.huazie.frame.jersey.common.FleaJerseyConstants;

/**
 * <p> Flea Jersey 客户端配置工具类 </p>
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public class FleaJerseyClientConfig {

    /**
     * <p> 获取系统账户编号 </p>
     *
     * @return 系统账户编号
     * @since 1.0.0
     */
    public static String getSystemAcctId() {
        return FleaConfigManager.getConfigItemValue(FleaJerseyConstants.JerseyClientConstants.CONFIG_ITEMS_KEY,
                FleaJerseyConstants.JerseyClientConstants.CONFIG_ITEM_SYSTEM_ACCT_ID);
    }

    /**
     * <p> 获取系统账户密码 </p>
     *
     * @return 系统账户密码
     * @since 1.0.0
     */
    public static String getSystemAcctPwd() {
        return FleaConfigManager.getConfigItemValue(FleaJerseyConstants.JerseyClientConstants.CONFIG_ITEMS_KEY,
                FleaJerseyConstants.JerseyClientConstants.CONFIG_ITEM_SYSTEM_ACCT_PWD);
    }

    /**
     * <p> 获取当前操作账户编号 </p>
     *
     * @return 当前操作账户编号
     */
    public static String getAcctId() {
        String acctId = "";
        IFleaUser fleaUser = FleaFrameManager.getManager().getUserInfo();
        if (ObjectUtils.isNotEmpty(fleaUser)) {
            acctId = StringUtils.valueOf(fleaUser.getAcctId());
        }
        return acctId;
    }

}