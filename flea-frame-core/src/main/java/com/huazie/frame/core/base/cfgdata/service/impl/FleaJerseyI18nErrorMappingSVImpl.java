package com.huazie.frame.core.base.cfgdata.service.impl;

import com.huazie.frame.common.exception.CommonException;
import com.huazie.frame.core.base.cfgdata.dao.interfaces.IFleaJerseyI18nErrorMappingDAO;
import com.huazie.frame.core.base.cfgdata.entity.FleaJerseyI18nErrorMapping;
import com.huazie.frame.core.base.cfgdata.service.interfaces.IFleaJerseyI18nErrorMappingSV;
import com.huazie.frame.db.jpa.dao.interfaces.IAbstractFleaJPADAO;
import com.huazie.frame.db.jpa.service.impl.AbstractFleaJPASVImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p> Flea Jersey 国际码和错误码映射服务层实现类 </p>
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@Service("i18nErrorMappingSV")
public class FleaJerseyI18nErrorMappingSVImpl extends AbstractFleaJPASVImpl<FleaJerseyI18nErrorMapping> implements IFleaJerseyI18nErrorMappingSV {

    private final IFleaJerseyI18nErrorMappingDAO i18NErrorMappingDAO;

    @Autowired
    public FleaJerseyI18nErrorMappingSVImpl(@Qualifier("i18nErrorMappingDAO") IFleaJerseyI18nErrorMappingDAO i18NErrorMappingDAO) {
        this.i18NErrorMappingDAO = i18NErrorMappingDAO;
    }

    @Override
    public List<FleaJerseyI18nErrorMapping> getMappings(String resourceCode, String serviceCode) throws CommonException {
        return i18NErrorMappingDAO.getMappings(resourceCode, serviceCode);
    }

    @Override
    public FleaJerseyI18nErrorMapping getMapping(String resourceCode, String serviceCode, String i18nCode) throws CommonException {
        return i18NErrorMappingDAO.getMapping(resourceCode, serviceCode, i18nCode);
    }

    @Override
    protected IAbstractFleaJPADAO<FleaJerseyI18nErrorMapping> getDAO() {
        return i18NErrorMappingDAO;
    }
}
