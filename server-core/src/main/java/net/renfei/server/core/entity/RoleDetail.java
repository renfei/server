package net.renfei.server.core.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.intercept.RunAsManager;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 角色对象
 *
 * @author renfei
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "角色对象")
public class RoleDetail implements ConfigAttribute, GrantedAuthority, Serializable {
    @Serial
    private static final long serialVersionUID = -5194970536302876575L;
    @Schema(description = "角色ID")
    private String id;
    @Schema(description = "客户端ID")
    private String clientId;
    @Schema(description = "角色名称")
    private String roleName;
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

    /**
     * If the <code>ConfigAttribute</code> can be represented as a <code>String</code> and
     * that <code>String</code> is sufficient in precision to be relied upon as a
     * configuration parameter by a {@link RunAsManager}, {@link AccessDecisionManager} or
     * <code>AccessDecisionManager</code> delegate, this method should return such a
     * <code>String</code>.
     * <p>
     * If the <code>ConfigAttribute</code> cannot be expressed with sufficient precision
     * as a <code>String</code>, <code>null</code> should be returned. Returning
     * <code>null</code> will require any relying classes to specifically support the
     * <code>ConfigAttribute</code> implementation, so returning <code>null</code> should
     * be avoided unless actually required.
     * @return a representation of the configuration attribute (or <code>null</code> if
     * the configuration attribute cannot be expressed as a <code>String</code> with
     * sufficient precision).
     */
    @Override
    public String getAttribute() {
        return roleName;
    }

    /**
     * If the <code>GrantedAuthority</code> can be represented as a <code>String</code>
     * and that <code>String</code> is sufficient in precision to be relied upon for an
     * access control decision by an {@link AccessDecisionManager} (or delegate), this
     * method should return such a <code>String</code>.
     * <p>
     * If the <code>GrantedAuthority</code> cannot be expressed with sufficient precision
     * as a <code>String</code>, <code>null</code> should be returned. Returning
     * <code>null</code> will require an <code>AccessDecisionManager</code> (or delegate)
     * to specifically support the <code>GrantedAuthority</code> implementation, so
     * returning <code>null</code> should be avoided unless actually required.
     * @return a representation of the granted authority (or <code>null</code> if the
     * granted authority cannot be expressed as a <code>String</code> with sufficient
     * precision).
     */
    @Override
    public String getAuthority() {
        return roleName;
    }
}
