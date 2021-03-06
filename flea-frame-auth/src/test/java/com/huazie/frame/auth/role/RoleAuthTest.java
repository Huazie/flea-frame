package com.huazie.frame.auth.role;

import com.huazie.frame.auth.base.role.entity.FleaRole;
import com.huazie.frame.auth.base.role.entity.FleaRoleRel;
import com.huazie.frame.auth.base.role.service.interfaces.IFleaRoleGroupRelSV;
import com.huazie.frame.auth.base.role.service.interfaces.IFleaRoleRelSV;
import com.huazie.frame.auth.base.role.service.interfaces.IFleaRoleSV;
import com.huazie.frame.auth.common.AuthRelTypeEnum;
import com.huazie.frame.common.CommonConstants;
import com.huazie.frame.common.EntityStateEnum;
import com.huazie.frame.common.exception.CommonException;
import com.huazie.frame.common.slf4j.FleaLogger;
import com.huazie.frame.common.slf4j.impl.FleaLoggerProxy;
import com.huazie.frame.common.util.DateUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * <p>  </p>
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public class RoleAuthTest {

    private static final FleaLogger LOGGER = FleaLoggerProxy.getProxyInstance(RoleAuthTest.class);

    private ApplicationContext applicationContext;

    @Before
    public void init() {
        applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        LOGGER.debug("ApplicationContext={}", applicationContext);
    }

    @Test
    public void testInsertRole() {

        FleaRole fleaRole = new FleaRole();
        fleaRole.setRoleName("超级管理员");
        fleaRole.setRoleDesc("系统最高权限拥有者");
        fleaRole.setGroupId(CommonConstants.NumeralConstants.MINUS_ONE);
        fleaRole.setRoleState(EntityStateEnum.IN_USE.getState());
        fleaRole.setCreateDate(DateUtils.getCurrentTime());
        fleaRole.setRemarks("超级管理员拥有系统最高权限");

        try {
            IFleaRoleSV fleaRoleSV = (IFleaRoleSV) applicationContext.getBean("fleaRoleSV");
            fleaRoleSV.save(fleaRole);
        } catch (CommonException e) {
            LOGGER.error("Exception :", e);
        }
    }

    @Test
    public void testInsertRoleRel() {

        FleaRoleRel fleaRoleRel = new FleaRoleRel();
        fleaRoleRel.setRoleId(1000L);
        fleaRoleRel.setRelId(1000L);
        fleaRoleRel.setRelType(AuthRelTypeEnum.ROLE_REL_PRIVILEGE_GROUP.getRelType()); // 角色关联权限组
        fleaRoleRel.setRelState(EntityStateEnum.IN_USE.getState());
        fleaRoleRel.setCreateDate(DateUtils.getCurrentTime());
        fleaRoleRel.setRemarks("【超级管理员】角色绑定【菜单访问】权限组");

        try {
            IFleaRoleRelSV fleaRoleRelSV = (IFleaRoleRelSV) applicationContext.getBean("fleaRoleRelSV");
            fleaRoleRelSV.save(fleaRoleRel);
        } catch (CommonException e) {
            LOGGER.error("Exception :", e);
        }
    }

    @Test
    public void testQueryRoleRel() {
        try {
            IFleaRoleRelSV fleaRoleRelSV = (IFleaRoleRelSV) applicationContext.getBean("fleaRoleRelSV");
            fleaRoleRelSV.getRoleRelList(1000L, AuthRelTypeEnum.ROLE_REL_PRIVILEGE_GROUP.getRelType());
        } catch (CommonException e) {
            LOGGER.error("Exception = ", e);
        }
    }

    @Test
    public void testQueryRoleGroupRel() {
        try {
            IFleaRoleGroupRelSV fleaRoleGroupRelSV = (IFleaRoleGroupRelSV) applicationContext.getBean("fleaRoleGroupRelSV");
            fleaRoleGroupRelSV.getRoleGroupRelList(1000L, AuthRelTypeEnum.ROLE_GROUP_REL_ROLE.getRelType());
        } catch (CommonException e) {
            LOGGER.error("Exception = ", e);
        }
    }
}
