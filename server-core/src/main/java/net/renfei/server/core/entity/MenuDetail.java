package net.renfei.server.core.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.renfei.server.core.constant.MenuTypeEnum;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 菜单与授权
 *
 * @author renfei
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "菜单与授权")
public class MenuDetail implements GrantedAuthority, Serializable {
    @Serial
    private static final long serialVersionUID = -5194970536302876575L;
    @Schema(description = "ID")
    private String id;
    @Schema(description = "父级ID")
    private String pid;
    @NotNull
    @Schema(description = "类型：菜单、按钮、接口")
    private MenuTypeEnum menuType;
    @NotNull
    @Schema(description = "菜单名称")
    private String menuName;
    @Schema(description = "菜单图标")
    private String menuIcon;
    @Schema(description = "菜单链接")
    private String menuLink;
    @Schema(description = "打开方式")
    private String menuTarget;
    @Schema(description = "样式类")
    private String menuClass;
    @Schema(description = "鼠标悬停标题")
    private String menuTitle;
    @Schema(description = "点击事件")
    private String menuOnclick;
    @Schema(description = "菜单排序:升序")
    private Integer menuOrder;
    @Schema(description = "添加时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date addTime;
    @Schema(description = "更新时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
    @Schema(description = "CSS样式")
    private String menuCss;
    @Schema(description = "扩展预留")
    private String extendJson;
    @Schema(title = "授权表达式")
    private String authority;
    @Schema(description = "子菜单")
    private List<MenuDetail> childMenu;

    @Override
    public String getAuthority() {
        return this.authority;
    }
}
