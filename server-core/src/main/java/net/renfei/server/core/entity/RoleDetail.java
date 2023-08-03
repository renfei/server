package net.renfei.server.core.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * 角色对象
 *
 * @author renfei
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "角色对象")
public class RoleDetail implements GrantedAuthority, Serializable {
    private static final String ROLE_PREFIX = "ROLE_";
    @Serial
    private static final long serialVersionUID = -5194970536302876575L;
    @Schema(description = "角色ID")
    private String id;
    @Schema(description = "角色名称")
    @NotNull
    private String roleEnName;
    @Schema(description = "角色中文名称")
    private String roleZhName;
    @Schema(description = "角色描述")
    private String roleDescribe;
    @Schema(description = "添加时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date addTime;
    @Schema(description = "更新时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
    @Schema(description = "是否是内置角色")
    private Boolean builtInRole;
    @Schema(description = "扩展预留")
    private String extendJson;
    @Schema(description = "菜单")
    private Set<MenuDetail> menuDetails;

    @Override
    public String getAuthority() {
        return roleEnName.startsWith(ROLE_PREFIX) ? roleEnName : ROLE_PREFIX + roleEnName;
    }
}
