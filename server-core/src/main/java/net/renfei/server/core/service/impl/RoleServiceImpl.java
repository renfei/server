package net.renfei.server.core.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.renfei.server.core.entity.ListData;
import net.renfei.server.core.entity.RoleDetail;
import net.renfei.server.core.repositories.SysRoleMapper;
import net.renfei.server.core.repositories.SysUserRoleMapper;
import net.renfei.server.core.repositories.entity.SysRole;
import net.renfei.server.core.repositories.entity.SysRoleExample;
import net.renfei.server.core.repositories.entity.SysUserRole;
import net.renfei.server.core.repositories.entity.SysUserRoleExample;
import net.renfei.server.core.service.BaseService;
import net.renfei.server.core.service.MenuService;
import net.renfei.server.core.service.RoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 角色服务
 *
 * @author renfei
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl extends BaseService implements RoleService {
    private final MenuService menuService;
    private final SysRoleMapper sysRoleMapper;
    private final SysUserRoleMapper sysUserRoleMapper;

    /**
     * 查询角色列表
     *
     * @param roleEnName 角色英文名
     * @param roleZhName 角色中文名
     * @param pages      页码
     * @param rows       每页容量
     * @return
     */
    @Override
    public ListData<RoleDetail> queryRole(String roleEnName, String roleZhName, int pages, int rows) {
        SysRoleExample example = new SysRoleExample();
        example.setOrderByClause("createTime DESC");
        SysRoleExample.Criteria criteria = example.createCriteria();
        criteria.andBuiltInRoleEqualTo(false);
        if (StringUtils.hasLength(roleEnName)) {
            criteria.andRoleEnNameLike("%" + roleEnName + "%");
        }
        if (StringUtils.hasLength(roleZhName)) {
            criteria.andRoleZhNameLike("%" + roleZhName + "%");
        }
        try (Page<SysRole> page = PageHelper.startPage(pages, rows)) {
            sysRoleMapper.selectByExampleWithBLOBs(example);
            List<RoleDetail> roleDetails = new ArrayList<>(page.size());
            page.forEach(role -> roleDetails.add(this.convert(role)));
            return new ListData<>(page, roleDetails);
        }
    }

    /**
     * 创建角色
     *
     * @param roleDetail 角色详情对象
     */
    @Override
    public void createRole(RoleDetail roleDetail) {
        Assert.notNull(roleDetail, "请求体不能为空");
        Assert.hasLength(roleDetail.getRoleEnName(), "角色英文名不能为空");
        Assert.hasLength(roleDetail.getRoleZhName(), "角色中文名不能为空");
        SysRoleExample example = new SysRoleExample();
        example.createCriteria().andRoleEnNameEqualTo(roleDetail.getRoleEnName().toUpperCase());
        Assert.isTrue(sysRoleMapper.selectByExample(example).isEmpty(), "角色英文名已经被占用，请更换一个重试。");
        SysRole sysRole = this.convert(roleDetail);
        sysRole.setId(null);
        sysRole.setAddTime(new Date());
        sysRole.setUpdateTime(new Date());
        sysRole.setBuiltInRole(false);
        sysRoleMapper.insertSelective(sysRole);
    }

    /**
     * 更新角色信息，只能更新除角色英文名以外的字段
     *
     * @param roleEnName 角色英文名
     * @param roleDetail 角色详情对象
     */
    @Override
    public void updateRole(String roleEnName, RoleDetail roleDetail) {
        Assert.notNull(roleDetail, "请求体不能为空");
        Assert.hasLength(roleDetail.getRoleZhName(), "角色中文名不能为空");
        SysRoleExample example = new SysRoleExample();
        example.createCriteria()
                .andBuiltInRoleEqualTo(false)
                .andRoleEnNameEqualTo(roleEnName.toUpperCase());
        List<SysRole> sysRoles = sysRoleMapper.selectByExample(example);
        Assert.isTrue(!sysRoles.isEmpty(), "根据角色英文名未找到相应的角色");
        SysRole sysRole = sysRoles.get(0);
        sysRole.setRoleZhName(roleDetail.getRoleZhName());
        sysRole.setRoleDescribe(roleDetail.getRoleDescribe());
        sysRole.setExtendjson(roleDetail.getRoleZhName());
        sysRole.setUpdateTime(new Date());
        sysRoleMapper.updateByPrimaryKeyWithBLOBs(sysRole);
    }

    /**
     * 删除角色
     *
     * @param roleEnName 角色英文名
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(String roleEnName) {
        SysRoleExample example = new SysRoleExample();
        example.createCriteria()
                .andBuiltInRoleEqualTo(false)
                .andRoleEnNameEqualTo(roleEnName.toUpperCase());
        List<SysRole> sysRoles = sysRoleMapper.selectByExample(example);
        Assert.isTrue(!sysRoles.isEmpty(), "根据角色英文名未找到相应的角色");
        sysRoleMapper.deleteByPrimaryKey(sysRoles.get(0).getId());
        // 删除用户角色关联表
        SysUserRoleExample userRoleExample = new SysUserRoleExample();
        userRoleExample.createCriteria()
                .andRoleNameEqualTo(roleEnName.toUpperCase());
        sysUserRoleMapper.deleteByExample(userRoleExample);
        // TODO 删除角色菜单资源关联表
    }

    /**
     * 根据用户名获取角色列表
     *
     * @param username 用户名
     * @return
     */
    @Override
    public Set<RoleDetail> getRoleListByUsername(String username) {
        SysUserRoleExample userRoleExample = new SysUserRoleExample();
        userRoleExample.createCriteria()
                .andUsernameEqualTo(username.toLowerCase());
        List<SysUserRole> sysUserRoles = sysUserRoleMapper.selectByExample(userRoleExample);
        List<String> roleNames = new ArrayList<>(sysUserRoles.size());
        sysUserRoles.forEach(sysUserRole -> roleNames.add(sysUserRole.getRoleName().toUpperCase()));
        SysRoleExample example = new SysRoleExample();
        example.createCriteria()
                .andRoleEnNameIn(roleNames);
        List<SysRole> sysRoles = sysRoleMapper.selectByExampleWithBLOBs(example);
        Set<RoleDetail> roleDetails = new HashSet<>();
        sysRoles.forEach(sysRole -> roleDetails.add(this.convert(sysRole)));
        return roleDetails;
    }

    /**
     * 设置用户的角色列表
     *
     * @param username  用户名
     * @param roleNames 角色列表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setRoleListByUsername(String username, Set<String> roleNames) {
        // 先删再插
        SysUserRoleExample userRoleExample = new SysUserRoleExample();
        userRoleExample.createCriteria()
                .andUsernameEqualTo(username.toLowerCase());
        sysUserRoleMapper.deleteByExample(userRoleExample);
        for (String roleName : roleNames) {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUsername(username);
            sysUserRole.setRoleName(roleName);
            sysUserRole.setAddTime(new Date());
            sysUserRoleMapper.insertSelective(sysUserRole);
        }
    }

    private RoleDetail convert(SysRole sysRole) {
        if (sysRole == null) {
            return null;
        }
        RoleDetail roleDetail = new RoleDetail();
        BeanUtils.copyProperties(sysRole, roleDetail);
        roleDetail.setRoleEnName(sysRole.getRoleEnName().toUpperCase());
        return roleDetail;
    }

    private SysRole convert(RoleDetail roleDetail) {
        if (roleDetail == null) {
            return null;
        }
        SysRole sysRole = new SysRole();
        BeanUtils.copyProperties(roleDetail, sysRole);
        sysRole.setRoleEnName(roleDetail.getRoleEnName().toUpperCase());
        return sysRole;
    }
}
