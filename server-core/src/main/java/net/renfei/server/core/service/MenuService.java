package net.renfei.server.core.service;

import net.renfei.server.core.constant.MenuTypeEnum;
import net.renfei.server.core.entity.ListData;
import net.renfei.server.core.entity.MenuDetail;

import java.util.List;
import java.util.Set;

/**
 * 菜单（权限资源）服务
 *
 * @author renfei
 */
public interface MenuService {
    /**
     * 获取全部菜单树
     *
     * @param pid 父级菜单，顶级传0
     * @return
     */
    List<MenuDetail> getMenuTree(Long pid);

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
    ListData<MenuDetail> queryMenuList(Long pid, MenuTypeEnum menuType, String menuName,
                                       String authority, int pages, int rows);

    /**
     * 创建菜单或资源
     *
     * @param menuDetail 菜单详情
     */
    void createMenu(MenuDetail menuDetail);

    /**
     * 修改菜单或资源
     *
     * @param id         菜单ID
     * @param menuDetail 菜单详情
     */
    void updateMenu(Long id, MenuDetail menuDetail);

    /**
     * 删除菜单或资源
     *
     * @param id 菜单ID
     */
    void deleteMenu(Long id);

    /**
     * 根据角色名获取菜单列表
     *
     * @param roleName 角色名
     * @return
     */
    List<MenuDetail> getMenuListByRoleName(String roleName);

    /**
     * 根据角色名设置菜单关系
     *
     * @param roleName 角色名
     * @param ids      菜单ID列表
     */
    void setMenuListByRoleName(String roleName, Set<Long> ids);

    /**
     * 获取我的菜单树（当前登录用户的菜单树）
     *
     * @return
     */
    List<MenuDetail> getMyMenuTree();
}
