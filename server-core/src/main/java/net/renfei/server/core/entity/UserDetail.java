package net.renfei.server.core.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

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
    private String phone;
    @Schema(description = "注册时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date registrationDate;
    @Schema(description = "密码")
    private String password;
    @Schema(description = "注册IP地址")
    private String registrationIp;
    @Schema(description = "密码错误次数")
    private Integer trialErrorTimes;
    @Schema(description = "锁定时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lockTime;
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
    @Schema(description = "昵称")
    private String nickname;
    private List<RoleDetail> roleDetails;

    @Override
    public List<RoleDetail> getAuthorities() {
        return this.roleDetails;
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
}
