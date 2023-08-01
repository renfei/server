package net.renfei.server.core.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.renfei.server.core.constant.SecretLevelEnum;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

/**
 * 用户详情
 *
 * @author renfei
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "用户详情")
@JsonIgnoreProperties(value = {"accountNonExpired", "credentialsNonExpired", "accountNonLocked", "authorities"})
public class UserDetail implements UserDetails, Serializable {
    @Serial
    private static final long serialVersionUID = -5194970536302876575L;
    @Schema(description = "用户ID")
    private String id;
    @Schema(description = "用户名")
    private String username;
    @Schema(description = "电子邮箱")
    private String email;
    @Schema(description = "手机号")
    private String mobile;
    @Schema(description = "注册时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date registrationDate;
    @Schema(description = "密码")
    private String password;
    @Schema(description = "注册IP地址")
    private String registrationIp;
    @Schema(description = "密码错误次数")
    private Integer errorCount;
    @Schema(description = "锁定时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lockTime;
    @Schema(description = "密码修改时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date passwordUpdateTime;
    @Schema(description = "是否是内置用户")
    private Boolean builtInUser;
    @Schema(description = "密码过期时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date passwordExpirationTime;
    @Schema(description = "是否被锁定")
    private Boolean locked;
    @Schema(description = "是否启用")
    private Boolean enabled;
    @Schema(description = "姓名")
    private String name;
    @Schema(description = "一次性密码秘钥")
    private String totp;
    @Schema(description = "性别")
    private String gender;
    @Schema(description = "办公地址")
    private String officeAddress;
    @Schema(description = "办公电话")
    private String officePhone;
    @Schema(description = "保密等级")
    private SecretLevelEnum secretLevel;
    @Schema(description = "描述")
    private String description;
    @Schema(description = "生日")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date birthDay;
    @Schema(description = "职务")
    private String duty;
    @Schema(description = "学历")
    private String education;
    @Schema(description = "家庭住址")
    private String homeAddress;
    @Schema(description = "家庭电话")
    private String homePhone;
    @Schema(description = "政治面貌")
    private String politicalStatus;
    @Schema(description = "专业")
    private String professional;
    @Schema(description = "参加工作时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date workDate;
    @Schema(description = "角色列表")
    private Set<RoleDetail> roleDetails;

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new HashSet<>();
        if (this.roleDetails != null && !this.roleDetails.isEmpty()) {
            // 添加所有角色
            authorities.addAll(this.roleDetails);
            this.roleDetails.forEach(roleDetail -> {
                if (roleDetail.getMenuDetails() != null && !roleDetail.getMenuDetails().isEmpty()) {
                    // 递归添加添加角色下的菜单的权限
                    this.recursivelyAdd(authorities, roleDetail.getMenuDetails());
                }
            });
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        if (passwordExpirationTime == null) {
            return true;
        }
        return new Date().compareTo(passwordExpirationTime) < 0;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    /**
     * 递归添加子菜单菜单的权限表达式
     *
     * @param authorities
     * @param menuDetails
     */
    private void recursivelyAdd(Collection<GrantedAuthority> authorities, Set<MenuDetail> menuDetails) {
        if (menuDetails != null && !menuDetails.isEmpty()) {
            authorities.addAll(menuDetails);
            menuDetails.forEach(menuDetail -> this.recursivelyAdd(authorities, menuDetail.getChildMenu()));
        }
    }
}
