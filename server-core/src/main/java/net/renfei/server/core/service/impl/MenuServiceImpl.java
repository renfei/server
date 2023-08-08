package net.renfei.server.core.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.renfei.server.core.constant.MenuTypeEnum;
import net.renfei.server.core.entity.ListData;
import net.renfei.server.core.entity.MenuDetail;
import net.renfei.server.core.repositories.SysMenuMapper;
import net.renfei.server.core.repositories.entity.SysMenuExample;
import net.renfei.server.core.repositories.entity.SysMenuWithBLOBs;
import net.renfei.server.core.service.BaseService;
import net.renfei.server.core.service.MenuService;
import net.renfei.server.core.service.SystemService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 菜单（权限资源）服务
 *
 * @author renfei
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MenuServiceImpl extends BaseService implements MenuService {
    private final SysMenuMapper sysMenuMapper;
    private final SystemService systemService;

    /**
     * 获取全部菜单树
     *
     * @param pid 父级菜单，顶级传0
     * @return
     */
    @Override
    public List<MenuDetail> getMenuTree(Long pid) {
        if (pid == null) {
            return null;
        }
        SysMenuExample example = new SysMenuExample();
        example.createCriteria()
                .andPidEqualTo(pid);
        List<SysMenuWithBLOBs> sysMenus = sysMenuMapper.selectByExampleWithBLOBs(example);
        if (sysMenus.isEmpty()) {
            return null;
        }
        List<MenuDetail> menuDetails = new ArrayList<>(sysMenus.size());
        for (SysMenuWithBLOBs sysMenu : sysMenus) {
            MenuDetail convert = this.convert(sysMenu);
            convert.setChildMenu(this.getMenuTree(sysMenu.getId()));
            menuDetails.add(convert);
        }
        return menuDetails;
    }

    /**
     * 查询菜单列表
     *
     * @param pid       父级菜单
     * @param menuType  菜单类型
     * @param menuName  菜单名称
     * @param authority 授权表达式
     * @param pages     页码
     * @param rows      每页容量
     * @return
     */
    @Override
    public ListData<MenuDetail> queryMenuList(Long pid, MenuTypeEnum menuType, String menuName,
                                              String authority, int pages, int rows) {
        SysMenuExample example = new SysMenuExample();
        SysMenuExample.Criteria criteria = example.createCriteria();
        if (pid != null) {
            criteria.andPidEqualTo(pid);
        }
        if (menuType != null) {
            criteria.andMenuTypeEqualTo(menuType.toString());
        }
        if (StringUtils.hasLength(menuName)) {
            criteria.andMenuNameLike("%" + menuName + "%");
        }
        if (StringUtils.hasLength(authority)) {
            criteria.andAuthorityLike("%" + authority + "%");
        }
        try (Page<SysMenuWithBLOBs> page = PageHelper.startPage(pages, rows)) {
            sysMenuMapper.selectByExampleWithBLOBs(example);
            List<MenuDetail> sysMenus = new ArrayList<>(page.size());
            page.forEach(sysMenu -> sysMenus.add(this.convert(sysMenu)));
            return new ListData<>(page, sysMenus);
        }
    }

    @Override
    public void createMenu(MenuDetail menuDetail) {
        assert menuDetail != null : "请求体不能为空";
        assert StringUtils.hasLength(menuDetail.getMenuName()) : "菜单名称不能为空";
        assert menuDetail.getPid() == null
                || "0".equals(menuDetail.getPid())
                || sysMenuMapper.selectByPrimaryKey(Long.parseLong(menuDetail.getPid())) != null : "PID错误";
        // 授权表达式必须在 authority-reference.json 中登记
        assert !StringUtils.hasLength(menuDetail.getAuthority())
                || systemService.queryAuthorityReference().stream()
                .anyMatch(response -> response.getAuthority().equals(menuDetail.getAuthority())) : "授权表达式不正确";
        menuDetail.setId(null);
        menuDetail.setAddTime(new Date());
        menuDetail.setUpdateTime(new Date());
        sysMenuMapper.insertSelective(this.convert(menuDetail));
    }

    @Override
    public void updateMenu(Long id, MenuDetail menuDetail) {
        assert menuDetail != null : "请求体不能为空";
        assert StringUtils.hasLength(menuDetail.getMenuName()) : "菜单名称不能为空";
        SysMenuWithBLOBs sysMenu = sysMenuMapper.selectByPrimaryKey(id);
        assert sysMenu != null : "根据菜单ID未能找到菜单数据";
        assert menuDetail.getPid() == null
                || "0".equals(menuDetail.getPid())
                || sysMenuMapper.selectByPrimaryKey(Long.parseLong(menuDetail.getPid())) != null : "PID错误";
        // 授权表达式必须在 authority-reference.json 中登记
        assert !StringUtils.hasLength(menuDetail.getAuthority())
                || systemService.queryAuthorityReference().stream()
                .anyMatch(response -> response.getAuthority().equals(menuDetail.getAuthority())) : "授权表达式不正确";
        sysMenu.setPid(menuDetail.getPid() == null ? 0L : Long.parseLong(menuDetail.getPid()));
        sysMenu.setMenuType(menuDetail.getMenuType().toString());
        sysMenu.setMenuName(menuDetail.getMenuName());
        sysMenu.setMenuIcon(menuDetail.getMenuIcon());
        sysMenu.setMenuLink(menuDetail.getMenuLink());
        sysMenu.setMenuTarget(menuDetail.getMenuTarget());
        sysMenu.setMenuClass(menuDetail.getMenuClass());
        sysMenu.setMenuTitle(menuDetail.getMenuTitle());
        sysMenu.setMenuOnclick(menuDetail.getMenuOnclick());
        sysMenu.setMenuOrder(menuDetail.getMenuOrder());
        sysMenu.setMenuCss(menuDetail.getMenuCss());
        sysMenu.setExtendJson(menuDetail.getExtendJson());
        sysMenu.setAuthority(menuDetail.getAuthority());
        sysMenu.setUpdateTime(new Date());
        sysMenuMapper.updateByPrimaryKeyWithBLOBs(sysMenu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMenu(Long id) {
        sysMenuMapper.deleteByPrimaryKey(id);
        // TODO 删除关联表
    }

    private MenuDetail convert(SysMenuWithBLOBs sysMenu) {
        if (sysMenu == null) {
            return null;
        }
        MenuDetail menuDetail = new MenuDetail();
        BeanUtils.copyProperties(sysMenu, menuDetail);
        menuDetail.setId(sysMenu.getId().toString());
        menuDetail.setPid(sysMenu.getPid().toString());
        menuDetail.setMenuType(MenuTypeEnum.valueOf(sysMenu.getMenuType()));
        return menuDetail;
    }

    private SysMenuWithBLOBs convert(MenuDetail menuDetail) {
        if (menuDetail == null) {
            return null;
        }
        SysMenuWithBLOBs sysMenu = new SysMenuWithBLOBs();
        BeanUtils.copyProperties(menuDetail, sysMenu);
        sysMenu.setId(menuDetail.getId() == null ? null : Long.valueOf(menuDetail.getId()));
        sysMenu.setPid(menuDetail.getPid() == null ? null : Long.valueOf(menuDetail.getPid()));
        sysMenu.setMenuType(menuDetail.getMenuType().toString());
        return sysMenu;
    }
}
