package net.renfei.server.core.repositories.entity;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table sys_role
 */
public class SysRole implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_role.id
     *
     * @mbg.generated
     */
    private Long id;

    /**
     * Database Column Remarks:
     *   角色名称
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_role.role_en_name
     *
     * @mbg.generated
     */
    private String roleEnName;

    /**
     * Database Column Remarks:
     *   角色中文名称
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_role.role_zh_name
     *
     * @mbg.generated
     */
    private String roleZhName;

    /**
     * Database Column Remarks:
     *   角色描述
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_role.role_describe
     *
     * @mbg.generated
     */
    private String roleDescribe;

    /**
     * Database Column Remarks:
     *   添加时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_role.add_time
     *
     * @mbg.generated
     */
    private Date addTime;

    /**
     * Database Column Remarks:
     *   更新时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_role.update_time
     *
     * @mbg.generated
     */
    private Date updateTime;

    /**
     * Database Column Remarks:
     *   是否是内置角色
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_role.built_in_role
     *
     * @mbg.generated
     */
    private Boolean builtInRole;

    /**
     * Database Column Remarks:
     *   扩展预留
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_role.extendJson
     *
     * @mbg.generated
     */
    private String extendjson;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table sys_role
     *
     * @mbg.generated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_role.id
     *
     * @return the value of sys_role.id
     *
     * @mbg.generated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_role.id
     *
     * @param id the value for sys_role.id
     *
     * @mbg.generated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_role.role_en_name
     *
     * @return the value of sys_role.role_en_name
     *
     * @mbg.generated
     */
    public String getRoleEnName() {
        return roleEnName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_role.role_en_name
     *
     * @param roleEnName the value for sys_role.role_en_name
     *
     * @mbg.generated
     */
    public void setRoleEnName(String roleEnName) {
        this.roleEnName = roleEnName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_role.role_zh_name
     *
     * @return the value of sys_role.role_zh_name
     *
     * @mbg.generated
     */
    public String getRoleZhName() {
        return roleZhName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_role.role_zh_name
     *
     * @param roleZhName the value for sys_role.role_zh_name
     *
     * @mbg.generated
     */
    public void setRoleZhName(String roleZhName) {
        this.roleZhName = roleZhName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_role.role_describe
     *
     * @return the value of sys_role.role_describe
     *
     * @mbg.generated
     */
    public String getRoleDescribe() {
        return roleDescribe;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_role.role_describe
     *
     * @param roleDescribe the value for sys_role.role_describe
     *
     * @mbg.generated
     */
    public void setRoleDescribe(String roleDescribe) {
        this.roleDescribe = roleDescribe;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_role.add_time
     *
     * @return the value of sys_role.add_time
     *
     * @mbg.generated
     */
    public Date getAddTime() {
        return addTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_role.add_time
     *
     * @param addTime the value for sys_role.add_time
     *
     * @mbg.generated
     */
    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_role.update_time
     *
     * @return the value of sys_role.update_time
     *
     * @mbg.generated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_role.update_time
     *
     * @param updateTime the value for sys_role.update_time
     *
     * @mbg.generated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_role.built_in_role
     *
     * @return the value of sys_role.built_in_role
     *
     * @mbg.generated
     */
    public Boolean getBuiltInRole() {
        return builtInRole;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_role.built_in_role
     *
     * @param builtInRole the value for sys_role.built_in_role
     *
     * @mbg.generated
     */
    public void setBuiltInRole(Boolean builtInRole) {
        this.builtInRole = builtInRole;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_role.extendJson
     *
     * @return the value of sys_role.extendJson
     *
     * @mbg.generated
     */
    public String getExtendjson() {
        return extendjson;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_role.extendJson
     *
     * @param extendjson the value for sys_role.extendJson
     *
     * @mbg.generated
     */
    public void setExtendjson(String extendjson) {
        this.extendjson = extendjson;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_role
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
        sb.append(", roleEnName=").append(roleEnName);
        sb.append(", roleZhName=").append(roleZhName);
        sb.append(", roleDescribe=").append(roleDescribe);
        sb.append(", addTime=").append(addTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", builtInRole=").append(builtInRole);
        sb.append(", extendjson=").append(extendjson);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_role
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
        SysRole other = (SysRole) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getRoleEnName() == null ? other.getRoleEnName() == null : this.getRoleEnName().equals(other.getRoleEnName()))
            && (this.getRoleZhName() == null ? other.getRoleZhName() == null : this.getRoleZhName().equals(other.getRoleZhName()))
            && (this.getRoleDescribe() == null ? other.getRoleDescribe() == null : this.getRoleDescribe().equals(other.getRoleDescribe()))
            && (this.getAddTime() == null ? other.getAddTime() == null : this.getAddTime().equals(other.getAddTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getBuiltInRole() == null ? other.getBuiltInRole() == null : this.getBuiltInRole().equals(other.getBuiltInRole()))
            && (this.getExtendjson() == null ? other.getExtendjson() == null : this.getExtendjson().equals(other.getExtendjson()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_role
     *
     * @mbg.generated
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getRoleEnName() == null) ? 0 : getRoleEnName().hashCode());
        result = prime * result + ((getRoleZhName() == null) ? 0 : getRoleZhName().hashCode());
        result = prime * result + ((getRoleDescribe() == null) ? 0 : getRoleDescribe().hashCode());
        result = prime * result + ((getAddTime() == null) ? 0 : getAddTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getBuiltInRole() == null) ? 0 : getBuiltInRole().hashCode());
        result = prime * result + ((getExtendjson() == null) ? 0 : getExtendjson().hashCode());
        return result;
    }
}