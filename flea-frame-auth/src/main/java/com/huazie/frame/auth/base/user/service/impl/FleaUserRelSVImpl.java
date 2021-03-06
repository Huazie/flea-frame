package com.huazie.frame.auth.base.user.service.impl;

import com.huazie.frame.auth.base.user.dao.interfaces.IFleaUserRelDAO;
import com.huazie.frame.auth.base.user.entity.FleaUserRel;
import com.huazie.frame.auth.base.user.service.interfaces.IFleaUserRelSV;
import com.huazie.frame.common.exception.CommonException;
import com.huazie.frame.db.jpa.dao.interfaces.IAbstractFleaJPADAO;
import com.huazie.frame.db.jpa.service.impl.AbstractFleaJPASVImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p> Flea用户关联（角色，角色组）SV层实现类 </p>
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@Service("fleaUserRelSV")
public class FleaUserRelSVImpl extends AbstractFleaJPASVImpl<FleaUserRel> implements IFleaUserRelSV {

    private IFleaUserRelDAO fleaUserRelDao;

    @Autowired
    @Qualifier("fleaUserRelDAO")
    public void setFleaUserRelDao(IFleaUserRelDAO fleaUserRelDao) {
        this.fleaUserRelDao = fleaUserRelDao;
    }

    @Override
    public List<FleaUserRel> getUserRelList(Long userId, String authRelType) throws CommonException {
        return fleaUserRelDao.getUserRelList(userId, authRelType);
    }

    @Override
    protected IAbstractFleaJPADAO<FleaUserRel> getDAO() {
        return fleaUserRelDao;
    }
}