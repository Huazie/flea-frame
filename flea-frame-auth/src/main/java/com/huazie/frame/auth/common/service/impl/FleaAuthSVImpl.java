package com.huazie.frame.auth.common.service.impl;

import com.huazie.frame.auth.base.function.entity.FleaFunctionAttr;
import com.huazie.frame.auth.base.function.entity.FleaMenu;
import com.huazie.frame.auth.base.function.service.interfaces.IFleaFunctionAttrSV;
import com.huazie.frame.auth.base.function.service.interfaces.IFleaMenuSV;
import com.huazie.frame.auth.base.privilege.entity.FleaPrivilegeGroupRel;
import com.huazie.frame.auth.base.privilege.entity.FleaPrivilegeRel;
import com.huazie.frame.auth.base.privilege.service.interfaces.IFleaPrivilegeGroupRelSV;
import com.huazie.frame.auth.base.privilege.service.interfaces.IFleaPrivilegeRelSV;
import com.huazie.frame.auth.base.role.entity.FleaRoleGroupRel;
import com.huazie.frame.auth.base.role.entity.FleaRoleRel;
import com.huazie.frame.auth.base.role.service.interfaces.IFleaRoleGroupRelSV;
import com.huazie.frame.auth.base.role.service.interfaces.IFleaRoleRelSV;
import com.huazie.frame.auth.base.user.entity.FleaAccount;
import com.huazie.frame.auth.base.user.entity.FleaAccountAttr;
import com.huazie.frame.auth.base.user.entity.FleaRealNameInfo;
import com.huazie.frame.auth.base.user.entity.FleaUser;
import com.huazie.frame.auth.base.user.entity.FleaUserAttr;
import com.huazie.frame.auth.base.user.entity.FleaUserGroupRel;
import com.huazie.frame.auth.base.user.entity.FleaUserRel;
import com.huazie.frame.auth.base.user.service.interfaces.IFleaAccountAttrSV;
import com.huazie.frame.auth.base.user.service.interfaces.IFleaAccountSV;
import com.huazie.frame.auth.base.user.service.interfaces.IFleaRealNameInfoSV;
import com.huazie.frame.auth.base.user.service.interfaces.IFleaUserAttrSV;
import com.huazie.frame.auth.base.user.service.interfaces.IFleaUserGroupRelSV;
import com.huazie.frame.auth.base.user.service.interfaces.IFleaUserRelSV;
import com.huazie.frame.auth.base.user.service.interfaces.IFleaUserSV;
import com.huazie.frame.auth.common.AuthRelTypeEnum;
import com.huazie.frame.auth.common.FleaAuthConstants;
import com.huazie.frame.auth.common.FleaAuthEntityConstants;
import com.huazie.frame.auth.common.FunctionTypeEnum;
import com.huazie.frame.auth.common.exception.FleaAuthCommonException;
import com.huazie.frame.auth.common.pojo.user.FleaUserModuleData;
import com.huazie.frame.auth.common.service.interfaces.IFleaAuthSV;
import com.huazie.frame.common.CommonConstants;
import com.huazie.frame.common.exception.CommonException;
import com.huazie.frame.common.util.ArrayUtils;
import com.huazie.frame.common.util.CollectionUtils;
import com.huazie.frame.common.util.NumberUtils;
import com.huazie.frame.common.util.ObjectUtils;
import com.huazie.frame.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p> Flea 授权服务实现类 </p>
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@Service("fleaAuthSV")
public class FleaAuthSVImpl implements IFleaAuthSV {

    private IFleaAccountSV fleaAccountSV; // Flea账户信息服务

    private IFleaUserSV fleaUserSV; // Flea用户信息服务

    private IFleaAccountAttrSV fleaAccountAttrSV; // Flea账户扩展属性服务

    private IFleaUserAttrSV fleaUserAttrSV; // Flea用户扩展属性服务

    private IFleaRealNameInfoSV fleaRealNameInfoSV; // Flea实名信息服务

    private IFleaUserGroupRelSV fleaUserGroupRelSV; // Flea用户组关联服务

    private IFleaUserRelSV fleaUserRelSV; // Flea用户关联服务

    private IFleaRoleGroupRelSV fleaRoleGroupRelSV; // Flea角色组关联服务

    private IFleaRoleRelSV fleaRoleRelSV; // Flea角色关联服务

    private IFleaPrivilegeGroupRelSV fleaPrivilegeGroupRelSV; // Flea权限组关联服务

    private IFleaPrivilegeRelSV fleaPrivilegeRelSV; // Flea权限关联服务

    private IFleaMenuSV fleaMenuSV; // Flea菜单服务

    private IFleaFunctionAttrSV fleaFunctionAttrSV; // Flea扩展属性服务

    @Autowired
    @Qualifier("fleaAccountSV")
    public void setFleaAccountSV(IFleaAccountSV fleaAccountSV) {
        this.fleaAccountSV = fleaAccountSV;
    }

    @Autowired
    @Qualifier("fleaUserSV")
    public void setFleaUserSV(IFleaUserSV fleaUserSV) {
        this.fleaUserSV = fleaUserSV;
    }

    @Autowired
    @Qualifier("fleaAccountAttrSV")
    public void setFleaAccountAttrSV(IFleaAccountAttrSV fleaAccountAttrSV) {
        this.fleaAccountAttrSV = fleaAccountAttrSV;
    }

    @Autowired
    @Qualifier("fleaUserAttrSV")
    public void setFleaUserAttrSV(IFleaUserAttrSV fleaUserAttrSV) {
        this.fleaUserAttrSV = fleaUserAttrSV;
    }

    @Autowired
    @Qualifier("fleaRealNameInfoSV")
    public void setFleaRealNameInfoSV(IFleaRealNameInfoSV fleaRealNameInfoSV) {
        this.fleaRealNameInfoSV = fleaRealNameInfoSV;
    }

    @Autowired
    @Qualifier("fleaUserGroupRelSV")
    public void setFleaUserGroupRelSV(IFleaUserGroupRelSV fleaUserGroupRelSV) {
        this.fleaUserGroupRelSV = fleaUserGroupRelSV;
    }

    @Autowired
    @Qualifier("fleaUserRelSV")
    public void setFleaUserRelSV(IFleaUserRelSV fleaUserRelSV) {
        this.fleaUserRelSV = fleaUserRelSV;
    }

    @Autowired
    @Qualifier("fleaRoleGroupRelSV")
    public void setFleaRoleGroupRelSV(IFleaRoleGroupRelSV fleaRoleGroupRelSV) {
        this.fleaRoleGroupRelSV = fleaRoleGroupRelSV;
    }

    @Autowired
    @Qualifier("fleaRoleRelSV")
    public void setFleaRoleRelSV(IFleaRoleRelSV fleaRoleRelSV) {
        this.fleaRoleRelSV = fleaRoleRelSV;
    }

    @Autowired
    @Qualifier("fleaPrivilegeGroupRelSV")
    public void setFleaPrivilegeGroupRelSV(IFleaPrivilegeGroupRelSV fleaPrivilegeGroupRelSV) {
        this.fleaPrivilegeGroupRelSV = fleaPrivilegeGroupRelSV;
    }

    @Autowired
    @Qualifier("fleaPrivilegeRelSV")
    public void setFleaPrivilegeRelSV(IFleaPrivilegeRelSV fleaPrivilegeRelSV) {
        this.fleaPrivilegeRelSV = fleaPrivilegeRelSV;
    }

    @Autowired
    @Qualifier("fleaMenuSV")
    public void setFleaMenuSV(IFleaMenuSV fleaMenuSV) {
        this.fleaMenuSV = fleaMenuSV;
    }

    @Autowired
    @Qualifier("fleaFunctionAttrSV")
    public void setFleaFunctionAttrSV(IFleaFunctionAttrSV fleaFunctionAttrSV) {
        this.fleaFunctionAttrSV = fleaFunctionAttrSV;
    }

    @Override
    @Cacheable(value = "fleaauthmenu", key = "#accountId + '_' + #systemAccountId")
    public List<FleaMenu> queryAllAccessibleMenus(Long accountId, Long systemAccountId) throws CommonException {

        // 校验操作账户编号
        // ERROR-AUTH-COMMON0000000001 【{0}】不能为空
        ObjectUtils.checkEmpty(accountId, FleaAuthCommonException.class, "ERROR-AUTH-COMMON0000000001", FleaAuthEntityConstants.UserEntityConstants.E_ACCOUNT_ID);

        // 校验系统账户编号
        // ERROR-AUTH-COMMON0000000001 【{0}】不能为空
        ObjectUtils.checkEmpty(systemAccountId, FleaAuthCommonException.class, "ERROR-AUTH-COMMON0000000001", "systemAccountId");

        // 根据操作帐户编号accountId查询帐户信息
        FleaAccount fleaAccount = fleaAccountSV.query(accountId);
        // 账户【account_id = {0}】不存在！
        ObjectUtils.checkEmpty(fleaAccount, FleaAuthCommonException.class, "ERROR-AUTH-COMMON0000000006", accountId);

        Long userId = fleaAccount.getUserId();
        // 根据操作用户编号userId查询用户信息
        FleaUser fleaUser = fleaUserSV.query(userId);
        // 用户【user_id = {0}】不存在！
        ObjectUtils.checkEmpty(fleaUser, FleaAuthCommonException.class, "ERROR-AUTH-COMMON0000000007", userId);

        FleaAccount systemFleaAccount = fleaAccountSV.query(systemAccountId);
        // 账户【account_id = {0}】不存在！
        ObjectUtils.checkEmpty(systemFleaAccount, FleaAuthCommonException.class, "ERROR-AUTH-COMMON0000000006", systemAccountId);

        List<Long> roleIdList = new ArrayList<>(); // 角色编号列表

        Long groupId = fleaUser.getGroupId(); // 获取用户组编号
        if (CommonConstants.NumeralConstants.MINUS_ONE != groupId) { // 用户关联了用户组
            // 获取用户组关联的角色组
            List<FleaUserGroupRel> userGroupRelRoleGroups = fleaUserGroupRelSV.getUserGroupRelList(groupId, AuthRelTypeEnum.USER_GROUP_REL_ROLE_GROUP.getRelType());
            // 处理用户组关联的角色组信息
            handleUserGroupRel(roleIdList, userGroupRelRoleGroups, AuthRelTypeEnum.USER_GROUP_REL_ROLE_GROUP.getRelType());

            // 获取用户组关联的角色
            List<FleaUserGroupRel> userGroupRelRoles = fleaUserGroupRelSV.getUserGroupRelList(groupId, AuthRelTypeEnum.USER_GROUP_REL_ROLE.getRelType());
            // 处理用户组关联的角色信息
            handleUserGroupRel(roleIdList, userGroupRelRoles, AuthRelTypeEnum.USER_GROUP_REL_ROLE.getRelType());
        }

        // 获取用户关联的角色组
        List<FleaUserRel> userRelRoleGroups = fleaUserRelSV.getUserRelList(userId, AuthRelTypeEnum.USER_REL_ROLE_GROUP.getRelType());
        // 处理用户关联的角色组信息
        handleUserRel(roleIdList, userRelRoleGroups, AuthRelTypeEnum.USER_REL_ROLE_GROUP.getRelType());

        // 获取用户关联的角色
        List<FleaUserRel> userRelRoles = fleaUserRelSV.getUserRelList(userId, AuthRelTypeEnum.USER_REL_ROLE.getRelType());
        // 处理用户关联的角色信息
        handleUserRel(roleIdList, userRelRoles, AuthRelTypeEnum.USER_REL_ROLE.getRelType());

        List<Long> privilegeIdList = new ArrayList<>(); // 权限列表

        if (CollectionUtils.isNotEmpty(roleIdList)) {
            for (Long roleId : roleIdList) {
                // 获取角色关联的权限组
                List<FleaRoleRel> roleRelPrivilegeGroups = fleaRoleRelSV.getRoleRelList(roleId, AuthRelTypeEnum.ROLE_REL_PRIVILEGE_GROUP.getRelType());
                // 处理角色关联的权限组信息
                handleRoleRel(privilegeIdList, roleRelPrivilegeGroups, AuthRelTypeEnum.ROLE_REL_PRIVILEGE_GROUP.getRelType());

                // 获取角色关联的权限
                List<FleaRoleRel> roleRelPrivileges = fleaRoleRelSV.getRoleRelList(roleId, AuthRelTypeEnum.ROLE_REL_PRIVILEGE.getRelType());
                // 处理角色关联的权限信息
                handleRoleRel(privilegeIdList, roleRelPrivileges, AuthRelTypeEnum.ROLE_REL_PRIVILEGE.getRelType());
            }
        }

        List<Long> menuIdList = new ArrayList<>(); // 菜单列表

        if (CollectionUtils.isNotEmpty(privilegeIdList)) {
            for (Long privilegeId : privilegeIdList) {
                // 获取权限关联的菜单
                List<FleaPrivilegeRel> privilegeRelMenus = fleaPrivilegeRelSV.getPrivilegeRelList(privilegeId, AuthRelTypeEnum.PRIVILEGE_REL_MENU.getRelType());
                // 处理权限关联的菜单信息
                handlePrivilegeRel(menuIdList, privilegeRelMenus);
            }
        }

        List<Long> systemRelMenuIdList = new ArrayList<>(); // 系统关联的菜单编号列表

        // 获取系统账户下关联的菜单
        // 取 功能类型 function_type = MENU, ATTR_CODE = SYSTEM_IN_USE
        List<FleaFunctionAttr> fleaFunctionAttrList = fleaFunctionAttrSV.getFunctionAttrList(null, FunctionTypeEnum.MENU.getType(), FleaAuthConstants.AttrCodeConstants.ATTR_CODE_SYSTEM_IN_USE);
        // 处理系统账户下关联的菜单信息
        handleFunctionAttr(systemRelMenuIdList, fleaFunctionAttrList, systemAccountId);

        return fleaMenuSV.queryAllAccessibleMenus(systemRelMenuIdList, menuIdList);
    }

    /**
     * <p> 处理用户组关联信息 </p>
     *
     * @param roleIdList       角色编号列表
     * @param userGroupRelList 用户组关联信息
     * @param authRelType      授权关联类型
     * @throws CommonException 通用异常
     * @since 1.0.0
     */
    private void handleUserGroupRel(List<Long> roleIdList, List<FleaUserGroupRel> userGroupRelList, String authRelType) throws CommonException {
        if (CollectionUtils.isNotEmpty(userGroupRelList)) {
            for (FleaUserGroupRel userGroupRel : userGroupRelList) {
                if (AuthRelTypeEnum.USER_GROUP_REL_ROLE_GROUP.getRelType().equals(authRelType)) { // 用户组关联角色组
                    // 用户组关联中rel_ext_a用于指定用户组关联的角色组中实际指定的角色编号【存在多个，以逗号分隔】
                    String relExtA = userGroupRel.getRelExtA();
                    if (StringUtils.isNotBlank(relExtA)) {
                        addRelIds(relExtA, roleIdList);
                    } else { // 用户组关联中rel_ext_a为空，表示关联其角色组【rel_id = 角色组编号】下所有角色
                        handleRoleGroupRel(userGroupRel.getRelId(), roleIdList);
                    }
                } else if (AuthRelTypeEnum.USER_GROUP_REL_ROLE.getRelType().equals(authRelType)) { // 用户组关联角色
                    // 用户组关联角色，直接取关联编号rel_id即可
                    addRelId(userGroupRel.getRelId(), roleIdList);
                }
            }
        }
    }

    /**
     * <p> 处理用户关联信息 </p>
     *
     * @param roleIdList  角色编号列表
     * @param userRelList 用户关联信息
     * @param authRelType 授权关联类型
     * @since 1.0.0
     */
    private void handleUserRel(List<Long> roleIdList, List<FleaUserRel> userRelList, String authRelType) throws CommonException {
        if (CollectionUtils.isNotEmpty(userRelList)) {
            for (FleaUserRel userRel : userRelList) {
                if (AuthRelTypeEnum.USER_REL_ROLE_GROUP.getRelType().equals(authRelType)) { // 用户关联角色组
                    // 用户关联中rel_ext_a用于指定用户关联的角色组中实际指定的角色编号
                    String relExtA = userRel.getRelExtA();
                    if (StringUtils.isNotBlank(relExtA)) {
                        addRelIds(relExtA, roleIdList);
                    } else { // 用户组关联中rel_ext_a为空，表示关联其角色组【rel_id = 角色组编号】下所有角色
                        handleRoleGroupRel(userRel.getRelId(), roleIdList);
                    }
                } else if (AuthRelTypeEnum.USER_REL_ROLE.getRelType().equals(authRelType)) { // 用户关联角色
                    // 用户关联角色，直接取关联编号rel_id即可
                    addRelId(userRel.getRelId(), roleIdList);
                }
            }
        }
    }

    /**
     * <p> 处理角色组关联信息 </p>
     *
     * @param roleGroupId 角色组编号
     * @param roleIdList  角色编号列表
     * @throws CommonException 通用异常
     * @since 1.0.0
     */
    private void handleRoleGroupRel(Long roleGroupId, List<Long> roleIdList) throws CommonException {
        // 获取角色组关联的所有角色
        List<FleaRoleGroupRel> roleGroupRelRoles = fleaRoleGroupRelSV.getRoleGroupRelList(roleGroupId, AuthRelTypeEnum.ROLE_GROUP_REL_ROLE.getRelType());
        if (CollectionUtils.isNotEmpty(roleGroupRelRoles)) {
            for (FleaRoleGroupRel roleGroupRel : roleGroupRelRoles) {
                if (ObjectUtils.isNotEmpty(roleGroupRel)) {
                    addRelId(roleGroupRel.getRelId(), roleIdList);
                }
            }
        }
    }

    /**
     * <p> 处理角色关联信息 </p>
     *
     * @param privilegeIdList 权限编号列表
     * @param roleRelList     角色关联信息
     * @param authRelType     授权关联类型
     * @throws CommonException 通用异常
     * @since 1.0.0
     */
    private void handleRoleRel(List<Long> privilegeIdList, List<FleaRoleRel> roleRelList, String authRelType) throws CommonException {
        if (CollectionUtils.isNotEmpty(roleRelList)) {
            for (FleaRoleRel roleRel : roleRelList) {
                if (AuthRelTypeEnum.ROLE_REL_PRIVILEGE_GROUP.getRelType().equals(authRelType)) { // 角色关联权限组
                    // 角色关联中rel_ext_a用于指定角色关联的权限组中实际指定的权限编号
                    String relExtA = roleRel.getRelExtA();
                    if (StringUtils.isNotBlank(relExtA)) {
                        addRelIds(relExtA, privilegeIdList);
                    } else { // 角色关联中rel_ext_a为空，则表示关联其权限组【rel_id = 权限组编号】下所有权限
                        handlePrivilegeGroupRel(roleRel.getRelId(), privilegeIdList);
                    }
                } else if (AuthRelTypeEnum.ROLE_REL_PRIVILEGE.getRelType().equals(authRelType)) { // 角色关联权限
                    addRelId(roleRel.getRelId(), privilegeIdList);
                }
            }
        }
    }

    /**
     * <p> 处理权限组关联信息 </p>
     *
     * @param privilegeGroupId 权限组编号
     * @param privilegeIdList  权权限编号列表
     * @throws CommonException 通用异常
     * @since 1.0.0
     */
    private void handlePrivilegeGroupRel(Long privilegeGroupId, List<Long> privilegeIdList) throws CommonException {
        // 获取权限组关联的所有权限
        List<FleaPrivilegeGroupRel> privilegeGroupRelPrivileges = fleaPrivilegeGroupRelSV.getPrivilegeGroupRelList(privilegeGroupId, AuthRelTypeEnum.PRIVILEGE_GROUP_REL_PRIVILEGE.getRelType());
        if (CollectionUtils.isNotEmpty(privilegeGroupRelPrivileges)) {
            for (FleaPrivilegeGroupRel privilegeGroupRel : privilegeGroupRelPrivileges) {
                if (ObjectUtils.isNotEmpty(privilegeGroupRel)) {
                    addRelId(privilegeGroupRel.getRelId(), privilegeIdList);
                }
            }
        }
    }

    /**
     * <p> 处理权限关联信息 </p>
     *
     * @param relIdList        关联ID列表
     * @param privilegeRelList 权限关联信息
     * @since 1.0.0
     */
    private void handlePrivilegeRel(List<Long> relIdList, List<FleaPrivilegeRel> privilegeRelList) {
        if (CollectionUtils.isNotEmpty(privilegeRelList)) {
            for (FleaPrivilegeRel privilegeRel : privilegeRelList) {
                if (ObjectUtils.isNotEmpty(privilegeRel)) {
                    addRelId(privilegeRel.getRelId(), relIdList);
                }
            }
        }
    }

    /**
     * <p> 处理功能扩展属性信息 </p>
     *
     * @param functionIdList   功能编号列表
     * @param functionAttrList 功能参数列表
     * @param systemAccountId     系统账号编号
     * @since 1.0.0
     */
    private void handleFunctionAttr(List<Long> functionIdList, List<FleaFunctionAttr> functionAttrList, Long systemAccountId) {
        if (CollectionUtils.isNotEmpty(functionAttrList)) {
            for (FleaFunctionAttr functionAttr : functionAttrList) {
                if (ObjectUtils.isNotEmpty(functionAttr)) {
                    if (!functionIdList.contains(functionAttr.getFunctionId()) && StringUtils.valueOf(systemAccountId).equals(functionAttr.getAttrValue())) {
                        functionIdList.add(functionAttr.getFunctionId());
                    }
                }
            }
        }
    }

    /**
     * <p> 添加多个关联信息 </p>
     *
     * @param relIdStr  关联编号字符串【逗号分隔】
     * @param relIdList 关联编号列表
     * @since 1.0.0
     */
    private void addRelIds(String relIdStr, List<Long> relIdList) {
        String[] relIdArr = StringUtils.split(relIdStr, CommonConstants.SymbolConstants.COMMA);
        if (ArrayUtils.isNotEmpty(relIdArr)) {
            for (String relId : relIdArr) {
                addRelId(Long.valueOf(relId), relIdList);
            }
        }
    }

    /**
     * <p> 添加关联信息 </p>
     *
     * @param relId     关联编号
     * @param relIdList 关联编号列表
     * @since 1.0.0
     */
    private void addRelId(Long relId, List<Long> relIdList) {
        if (!relIdList.contains(relId)) {
            relIdList.add(relId);
        }
    }

    @Override
    @Cacheable(value = "fleaauthuser", key = "'accountId_' + #accountId")
    public FleaUserModuleData getFleaUserModuleData(Long accountId) throws CommonException {

        FleaUserModuleData fleaUserModuleData = new FleaUserModuleData();

        // 校验账户编号
        // 【{0}】必须是正数！
        NumberUtils.checkNonPositiveNumber(accountId, FleaAuthCommonException.class, "ERROR-AUTH-COMMON0000000008", FleaAuthEntityConstants.UserEntityConstants.E_ACCOUNT_ID);

        // 获取有效的账户信息
        FleaAccount fleaAccount = fleaAccountSV.queryValidAccount(accountId);

        Long userId = 0L;
        List<FleaAccountAttr> fleaAccountAttrs = null;
        if (ObjectUtils.isNotEmpty(fleaAccount)) {
            userId = fleaAccount.getUserId();
            fleaAccountAttrs = fleaAccountAttrSV.queryValidAccountAttrs(accountId);
        }

        // 获取有效的用户信息
        FleaUser fleaUser = null;
        List<FleaUserAttr> fleaUserAttrs = null;
        FleaRealNameInfo fleaRealNameInfo = null;
        if (NumberUtils.isPositiveNumber(userId)) {
            fleaUser = fleaUserSV.queryValidUser(userId);
            fleaUserAttrs = fleaUserAttrSV.queryValidUserAttrs(userId);
            // 从用户扩展属性中获取用户关联实名信息
            long realNameId = 0L;
            if (CollectionUtils.isNotEmpty(fleaUserAttrs)) {
                for (FleaUserAttr fleaUserAttr : fleaUserAttrs) {
                    if (FleaAuthConstants.AttrCodeConstants.ATTR_CODE_REAL_NAME_ID.equals(fleaUserAttr.getAttrCode())) {
                        realNameId = Long.parseLong(fleaUserAttr.getAttrValue());
                        break;
                    }
                }
            }
            fleaRealNameInfo = fleaRealNameInfoSV.queryValidRealNameInfo(realNameId);
        }

        fleaUserModuleData.setFleaUser(fleaUser);
        fleaUserModuleData.setFleaAccount(fleaAccount);
        fleaUserModuleData.setFleaUserAttrs(fleaUserAttrs);
        fleaUserModuleData.setFleaAccountAttrs(fleaAccountAttrs);
        fleaUserModuleData.setFleaRealNameInfo(fleaRealNameInfo);

        return fleaUserModuleData;
    }
}
