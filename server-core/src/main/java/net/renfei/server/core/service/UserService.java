package net.renfei.server.core.service;

import net.renfei.server.core.constant.SecretLevelEnum;
import net.renfei.server.core.entity.UserDetail;
import net.renfei.server.core.entity.payload.request.SettingPasswordRequest;
import net.renfei.server.core.entity.payload.request.SignInRequest;
import net.renfei.server.core.entity.payload.response.SignInResponse;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 用户服务
 *
 * @author renfei
 */
public interface UserService extends UserDetailsService {
    /**
     * 根据用户名查找用户
     *
     * @param username the username identifying the user whose data is required.
     * @return
     * @throws UsernameNotFoundException
     */
    UserDetail loadUserByUsername(String username) throws UsernameNotFoundException;

    /**
     * 用户登录
     *
     * @param signInRequest 登录请求体
     * @return
     */
    SignInResponse signIn(SignInRequest signInRequest);

    /**
     * 创建系统用户（后台）
     *
     * @param userDetail 用户详情
     */
    void createSystemUser(UserDetail userDetail);

    /**
     * 更新用户资料，只能更新基础资料，不会更新密码等信息
     *
     * @param username   用户名
     * @param userDetail 用户详情对象
     */
    void updateSystemUser(String username, UserDetail userDetail);

    /**
     * 用户定密
     *
     * @param username        用户名
     * @param secretLevelEnum 密级
     */
    void settingUserSecretLevel(String username, SecretLevelEnum secretLevelEnum);

    /**
     * 设置用户密码
     *
     * @param username               用户名
     * @param settingPasswordRequest 密码
     */
    void settingUserPassword(String username, SettingPasswordRequest settingPasswordRequest);

    /**
     * 启用或禁用用户
     *
     * @param username 用户名
     * @param enable   启用或禁用
     */
    void enableUser(String username, boolean enable);

    /**
     * 锁定或解锁用户
     *
     * @param username 用户名
     * @param locked   锁定或解锁
     */
    void lockedUser(String username, boolean locked);
}
