package net.renfei.server.core.service;

import net.renfei.server.core.entity.ListData;
import net.renfei.server.core.entity.RoleDetail;

/**
 * 角色服务
 *
 * @author renfei
 */
public interface RoleService {
    /**
     * 查询角色列表
     *
     * @param roleEnName 角色英文名
     * @param roleZhName 角色中文名
     * @param pages      页码
     * @param rows       每页容量
     * @return
     */
    ListData<RoleDetail> queryRole(String roleEnName, String roleZhName, int pages, int rows);

    /**
     * 创建角色
     *
     * @param roleDetail 角色详情对象
     */
    void createRole(RoleDetail roleDetail);

    /**
     * 更新角色信息，只能更新除角色英文名以外的字段
     *
     * @param roleEnName 角色英文名
     * @param roleDetail 角色详情对象
     */
    void updateRole(String roleEnName, RoleDetail roleDetail);

    /**
     * 删除角色
     *
     * @param roleEnName 角色英文名
     */
    void deleteRole(String roleEnName);
}
