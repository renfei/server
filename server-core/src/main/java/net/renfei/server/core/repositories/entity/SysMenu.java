package net.renfei.server.core.repositories.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Database Table Remarks:
 *   系统菜单权限资源
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table sys_menu
 */
public class SysMenu implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_menu.id
     *
     * @mbg.generated
     */
    private Long id;

    /**
     * Database Column Remarks:
     *   父级菜单，顶级为0
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_menu.pid
     *
     * @mbg.generated
     */
    private Long pid;

    /**
     * Database Column Remarks:
     *   类型：菜单、按钮、接口
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_menu.menu_type
     *
     * @mbg.generated
     */
    private String menuType;

    /**
     * Database Column Remarks:
     *   菜单名称
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_menu.menu_name
     *
     * @mbg.generated
     */
    private String menuName;

    /**
     * Database Column Remarks:
     *   前端使用：菜单图标
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_menu.menu_icon
     *
     * @mbg.generated
     */
    private String menuIcon;

    /**
     * Database Column Remarks:
     *   前端使用：菜单链接
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_menu.menu_link
     *
     * @mbg.generated
     */
    private String menuLink;

    /**
     * Database Column Remarks:
     *   前端使用：打开方式
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_menu.menu_target
     *
     * @mbg.generated
     */
    private String menuTarget;

    /**
     * Database Column Remarks:
     *   前端使用：样式类
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_menu.menu_class
     *
     * @mbg.generated
     */
    private String menuClass;

    /**
     * Database Column Remarks:
     *   前端使用：鼠标悬停标题
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_menu.menu_title
     *
     * @mbg.generated
     */
    private String menuTitle;

    /**
     * Database Column Remarks:
     *   前端使用：点击执行事件
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_menu.menu_onclick
     *
     * @mbg.generated
     */
    private String menuOnclick;

    /**
     * Database Column Remarks:
     *   排序升序
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_menu.menu_order
     *
     * @mbg.generated
     */
    private Integer menuOrder;

    /**
     * Database Column Remarks:
     *   添加时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_menu.add_time
     *
     * @mbg.generated
     */
    private Date addTime;

    /**
     * Database Column Remarks:
     *   更新时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_menu.update_time
     *
     * @mbg.generated
     */
    private Date updateTime;

    /**
     * Database Column Remarks:
     *   授权表达式
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_menu.authority
     *
     * @mbg.generated
     */
    private String authority;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table sys_menu
     *
     * @mbg.generated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_menu.id
     *
     * @return the value of sys_menu.id
     *
     * @mbg.generated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_menu.id
     *
     * @param id the value for sys_menu.id
     *
     * @mbg.generated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_menu.pid
     *
     * @return the value of sys_menu.pid
     *
     * @mbg.generated
     */
    public Long getPid() {
        return pid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_menu.pid
     *
     * @param pid the value for sys_menu.pid
     *
     * @mbg.generated
     */
    public void setPid(Long pid) {
        this.pid = pid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_menu.menu_type
     *
     * @return the value of sys_menu.menu_type
     *
     * @mbg.generated
     */
    public String getMenuType() {
        return menuType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_menu.menu_type
     *
     * @param menuType the value for sys_menu.menu_type
     *
     * @mbg.generated
     */
    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_menu.menu_name
     *
     * @return the value of sys_menu.menu_name
     *
     * @mbg.generated
     */
    public String getMenuName() {
        return menuName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_menu.menu_name
     *
     * @param menuName the value for sys_menu.menu_name
     *
     * @mbg.generated
     */
    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_menu.menu_icon
     *
     * @return the value of sys_menu.menu_icon
     *
     * @mbg.generated
     */
    public String getMenuIcon() {
        return menuIcon;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_menu.menu_icon
     *
     * @param menuIcon the value for sys_menu.menu_icon
     *
     * @mbg.generated
     */
    public void setMenuIcon(String menuIcon) {
        this.menuIcon = menuIcon;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_menu.menu_link
     *
     * @return the value of sys_menu.menu_link
     *
     * @mbg.generated
     */
    public String getMenuLink() {
        return menuLink;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_menu.menu_link
     *
     * @param menuLink the value for sys_menu.menu_link
     *
     * @mbg.generated
     */
    public void setMenuLink(String menuLink) {
        this.menuLink = menuLink;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_menu.menu_target
     *
     * @return the value of sys_menu.menu_target
     *
     * @mbg.generated
     */
    public String getMenuTarget() {
        return menuTarget;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_menu.menu_target
     *
     * @param menuTarget the value for sys_menu.menu_target
     *
     * @mbg.generated
     */
    public void setMenuTarget(String menuTarget) {
        this.menuTarget = menuTarget;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_menu.menu_class
     *
     * @return the value of sys_menu.menu_class
     *
     * @mbg.generated
     */
    public String getMenuClass() {
        return menuClass;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_menu.menu_class
     *
     * @param menuClass the value for sys_menu.menu_class
     *
     * @mbg.generated
     */
    public void setMenuClass(String menuClass) {
        this.menuClass = menuClass;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_menu.menu_title
     *
     * @return the value of sys_menu.menu_title
     *
     * @mbg.generated
     */
    public String getMenuTitle() {
        return menuTitle;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_menu.menu_title
     *
     * @param menuTitle the value for sys_menu.menu_title
     *
     * @mbg.generated
     */
    public void setMenuTitle(String menuTitle) {
        this.menuTitle = menuTitle;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_menu.menu_onclick
     *
     * @return the value of sys_menu.menu_onclick
     *
     * @mbg.generated
     */
    public String getMenuOnclick() {
        return menuOnclick;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_menu.menu_onclick
     *
     * @param menuOnclick the value for sys_menu.menu_onclick
     *
     * @mbg.generated
     */
    public void setMenuOnclick(String menuOnclick) {
        this.menuOnclick = menuOnclick;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_menu.menu_order
     *
     * @return the value of sys_menu.menu_order
     *
     * @mbg.generated
     */
    public Integer getMenuOrder() {
        return menuOrder;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_menu.menu_order
     *
     * @param menuOrder the value for sys_menu.menu_order
     *
     * @mbg.generated
     */
    public void setMenuOrder(Integer menuOrder) {
        this.menuOrder = menuOrder;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_menu.add_time
     *
     * @return the value of sys_menu.add_time
     *
     * @mbg.generated
     */
    public Date getAddTime() {
        return addTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_menu.add_time
     *
     * @param addTime the value for sys_menu.add_time
     *
     * @mbg.generated
     */
    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_menu.update_time
     *
     * @return the value of sys_menu.update_time
     *
     * @mbg.generated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_menu.update_time
     *
     * @param updateTime the value for sys_menu.update_time
     *
     * @mbg.generated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_menu.authority
     *
     * @return the value of sys_menu.authority
     *
     * @mbg.generated
     */
    public String getAuthority() {
        return authority;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_menu.authority
     *
     * @param authority the value for sys_menu.authority
     *
     * @mbg.generated
     */
    public void setAuthority(String authority) {
        this.authority = authority;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_menu
     *
     * @mbg.generated
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", pid=").append(pid);
        sb.append(", menuType=").append(menuType);
        sb.append(", menuName=").append(menuName);
        sb.append(", menuIcon=").append(menuIcon);
        sb.append(", menuLink=").append(menuLink);
        sb.append(", menuTarget=").append(menuTarget);
        sb.append(", menuClass=").append(menuClass);
        sb.append(", menuTitle=").append(menuTitle);
        sb.append(", menuOnclick=").append(menuOnclick);
        sb.append(", menuOrder=").append(menuOrder);
        sb.append(", addTime=").append(addTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", authority=").append(authority);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_menu
     *
     * @mbg.generated
     */
    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        SysMenu other = (SysMenu) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getPid() == null ? other.getPid() == null : this.getPid().equals(other.getPid()))
            && (this.getMenuType() == null ? other.getMenuType() == null : this.getMenuType().equals(other.getMenuType()))
            && (this.getMenuName() == null ? other.getMenuName() == null : this.getMenuName().equals(other.getMenuName()))
            && (this.getMenuIcon() == null ? other.getMenuIcon() == null : this.getMenuIcon().equals(other.getMenuIcon()))
            && (this.getMenuLink() == null ? other.getMenuLink() == null : this.getMenuLink().equals(other.getMenuLink()))
            && (this.getMenuTarget() == null ? other.getMenuTarget() == null : this.getMenuTarget().equals(other.getMenuTarget()))
            && (this.getMenuClass() == null ? other.getMenuClass() == null : this.getMenuClass().equals(other.getMenuClass()))
            && (this.getMenuTitle() == null ? other.getMenuTitle() == null : this.getMenuTitle().equals(other.getMenuTitle()))
            && (this.getMenuOnclick() == null ? other.getMenuOnclick() == null : this.getMenuOnclick().equals(other.getMenuOnclick()))
            && (this.getMenuOrder() == null ? other.getMenuOrder() == null : this.getMenuOrder().equals(other.getMenuOrder()))
            && (this.getAddTime() == null ? other.getAddTime() == null : this.getAddTime().equals(other.getAddTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getAuthority() == null ? other.getAuthority() == null : this.getAuthority().equals(other.getAuthority()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_menu
     *
     * @mbg.generated
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getPid() == null) ? 0 : getPid().hashCode());
        result = prime * result + ((getMenuType() == null) ? 0 : getMenuType().hashCode());
        result = prime * result + ((getMenuName() == null) ? 0 : getMenuName().hashCode());
        result = prime * result + ((getMenuIcon() == null) ? 0 : getMenuIcon().hashCode());
        result = prime * result + ((getMenuLink() == null) ? 0 : getMenuLink().hashCode());
        result = prime * result + ((getMenuTarget() == null) ? 0 : getMenuTarget().hashCode());
        result = prime * result + ((getMenuClass() == null) ? 0 : getMenuClass().hashCode());
        result = prime * result + ((getMenuTitle() == null) ? 0 : getMenuTitle().hashCode());
        result = prime * result + ((getMenuOnclick() == null) ? 0 : getMenuOnclick().hashCode());
        result = prime * result + ((getMenuOrder() == null) ? 0 : getMenuOrder().hashCode());
        result = prime * result + ((getAddTime() == null) ? 0 : getAddTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getAuthority() == null) ? 0 : getAuthority().hashCode());
        return result;
    }
}