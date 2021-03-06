package com.huazie.frame.auth.base.function.service.impl;

import com.huazie.frame.auth.base.function.dao.interfaces.IFleaMenuDAO;
import com.huazie.frame.auth.base.function.entity.FleaMenu;
import com.huazie.frame.auth.base.function.service.interfaces.IFleaMenuSV;
import com.huazie.frame.auth.common.FleaAuthEntityConstants;
import com.huazie.frame.auth.common.exception.FleaAuthCommonException;
import com.huazie.frame.auth.common.pojo.function.menu.FleaMenuPOJO;
import com.huazie.frame.common.exception.CommonException;
import com.huazie.frame.common.util.CollectionUtils;
import com.huazie.frame.common.util.NumberUtils;
import com.huazie.frame.common.util.ObjectUtils;
import com.huazie.frame.common.util.StringUtils;
import com.huazie.frame.db.jpa.dao.interfaces.IAbstractFleaJPADAO;
import com.huazie.frame.db.jpa.service.impl.AbstractFleaJPASVImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p> Flea菜单SV层实现类 </p>
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
@Service("fleaMenuSV")
public class FleaMenuSVImpl extends AbstractFleaJPASVImpl<FleaMenu> implements IFleaMenuSV {

    private IFleaMenuDAO fleaMenuDao;

    @Autowired
    @Qualifier("fleaMenuDAO")
    public void setFleaMenuDao(IFleaMenuDAO fleaMenuDao) {
        this.fleaMenuDao = fleaMenuDao;
    }

    @Override
    public List<FleaMenu> queryAllAccessibleMenus(List<Long> systemRelMenuIdList, List<Long> menuIdList) throws CommonException {

        // 取交集
        systemRelMenuIdList.retainAll(menuIdList);

        // 获取菜单列表
        List<FleaMenu> allAccessibleMenus = null;
        if (CollectionUtils.isNotEmpty(systemRelMenuIdList)) {
            allAccessibleMenus = new ArrayList<>();
            for (Long menuId : systemRelMenuIdList) {
                if (NumberUtils.isPositiveNumber(menuId)) {
                    // 获取系统关联菜单和用户授权菜单交集中有效的菜单信息
                    List<FleaMenu> fleaMenuList = this.queryValidMenus(menuId, null, null, null, null);
                    if (CollectionUtils.isNotEmpty(fleaMenuList)) {
                        allAccessibleMenus.add(fleaMenuList.get(0));
                    }
                }
            }
        }

        return allAccessibleMenus;
    }

    @Override
    public List<FleaMenu> queryValidMenus(Long menuId, String menuCode, String menuName, Integer menuLevel, Long parentId) throws CommonException {
        return fleaMenuDao.getValidMenu(menuId, menuCode, menuName, menuLevel, parentId);
    }

    @Override
    public List<FleaMenu> queryAllValidMenus() throws CommonException {
        return fleaMenuDao.getValidMenu(null, null, null, null, null);
    }

    @Override
    public FleaMenu saveFleaMenu(FleaMenuPOJO fleaMenuPOJO) throws CommonException {
        FleaMenu fleaMenu = newFleaMenu(fleaMenuPOJO);
        // 保存Flea菜单
        this.save(fleaMenu);
        return fleaMenu;
    }

    /**
     * <p> 新建一个Flea菜单实体类对象 </p>
     *
     * @param fleaMenuPOJO flea菜单POJO对象
     * @return Flea菜单
     * @throws CommonException 通用异常
     * @since 1.0.0
     */
    private FleaMenu newFleaMenu(FleaMenuPOJO fleaMenuPOJO) throws CommonException {

        // 校验Flea菜单POJO类对象是否为空
        // ERROR-AUTH-COMMON0000000001 【{0}】不能为空
        ObjectUtils.checkEmpty(fleaMenuPOJO, FleaAuthCommonException.class, "ERROR-AUTH-COMMON0000000001", FleaMenuPOJO.class.getSimpleName());

        // 校验菜单编码不能为空
        String menuCode = fleaMenuPOJO.getMenuCode();
        StringUtils.checkBlank(menuCode, FleaAuthCommonException.class, "ERROR-AUTH-COMMON0000000001", FleaAuthEntityConstants.FunctionEntityConstants.E_MENU_CODE);

        // 校验菜单名称不能为空
        String menuName = fleaMenuPOJO.getMenuName();
        StringUtils.checkBlank(menuName, FleaAuthCommonException.class, "ERROR-AUTH-COMMON0000000001", FleaAuthEntityConstants.FunctionEntityConstants.E_MENU_NAME);

        // 校验菜单FontAwesome小图标不能为空
        String menuIcon = fleaMenuPOJO.getMenuIcon();
        StringUtils.checkBlank(menuIcon, FleaAuthCommonException.class, "ERROR-AUTH-COMMON0000000001", FleaAuthEntityConstants.FunctionEntityConstants.E_MENU_ICON);

        // 校验菜单顺序不能为空
        Integer menuSort = fleaMenuPOJO.getMenuSort();
        ObjectUtils.checkEmpty(menuSort, FleaAuthCommonException.class, "ERROR-AUTH-COMMON0000000001", FleaAuthEntityConstants.FunctionEntityConstants.E_MENU_SORT);

        // 校验菜单层级必须是正数
        Integer menuLevel = fleaMenuPOJO.getMenuLevel();
        // 【{0}】必须是正数！
        NumberUtils.checkNonPositiveNumber(menuLevel, FleaAuthCommonException.class, "ERROR-AUTH-COMMON0000000008", FleaAuthEntityConstants.FunctionEntityConstants.E_MENU_LEVEL);

        return new FleaMenu(menuCode, menuName, menuIcon, menuSort,
                fleaMenuPOJO.getMenuView(), menuLevel,
                fleaMenuPOJO.getParentId(),
                fleaMenuPOJO.getEffectiveDate(),
                fleaMenuPOJO.getExpiryDate(),
                fleaMenuPOJO.getRemarks());
    }

    @Override
    protected IAbstractFleaJPADAO<FleaMenu> getDAO() {
        return fleaMenuDao;
    }
}