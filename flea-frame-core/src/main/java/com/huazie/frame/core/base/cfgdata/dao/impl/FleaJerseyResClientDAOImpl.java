package com.huazie.frame.core.base.cfgdata.dao.impl;

import com.huazie.frame.common.util.CollectionUtils;
import com.huazie.frame.common.util.StringUtils;
import com.huazie.frame.core.base.cfgdata.dao.interfaces.IFleaJerseyResClientDAO;
import com.huazie.frame.core.base.cfgdata.entity.FleaJerseyResClient;
import com.huazie.frame.core.common.EntityStateEnum;
import com.huazie.frame.core.common.FleaEntityConstants;
import com.huazie.frame.db.jpa.common.FleaJPAQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p> Flea Jersey 资源客户端DAO层实现 </p>
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@Repository("resClientDAO")
public class FleaJerseyResClientDAOImpl extends FleaConfigDAOImpl<FleaJerseyResClient> implements IFleaJerseyResClientDAO {

    private final static Logger LOGGER = LoggerFactory.getLogger(FleaJerseyResClientDAOImpl.class);

    @Override
    @SuppressWarnings(value = "unchecked")
    public FleaJerseyResClient getResClient(String clientCode) throws Exception {

        FleaJPAQuery query = getQuery(null);

        if (StringUtils.isNotBlank(clientCode)) {
            query.equal(FleaEntityConstants.S_CLIENT_CODE, clientCode);
        }

        query.equal(FleaEntityConstants.S_STATE, EntityStateEnum.IN_USE.getValue());

        List<FleaJerseyResClient> resClientList = query.getResultList();

        FleaJerseyResClient resClient = null;

        if (CollectionUtils.isNotEmpty(resClientList)) {
            resClient = resClientList.get(0);
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("FleaJerseyResClientDAOImpl##getResClient(String) ResClient = {}", resClient);
        }

        return resClient;
    }
}