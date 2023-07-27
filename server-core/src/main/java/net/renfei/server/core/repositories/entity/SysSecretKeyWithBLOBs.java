package net.renfei.server.core.repositories.entity;

import java.io.Serializable;

/**
 * Database Table Remarks:
 *   系统安全秘钥
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table sys_secret_key
 */
public class SysSecretKeyWithBLOBs extends SysSecretKey implements Serializable {
    /**
     * Database Column Remarks:
     *   公钥
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_secret_key.public_key
     *
     * @mbg.generated
     */
    private String publicKey;

    /**
     * Database Column Remarks:
     *   私钥
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_secret_key.private_key
     *
     * @mbg.generated
     */
    private String privateKey;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table sys_secret_key
     *
     * @mbg.generated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_secret_key.public_key
     *
     * @return the value of sys_secret_key.public_key
     *
     * @mbg.generated
     */
    public String getPublicKey() {
        return publicKey;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_secret_key.public_key
     *
     * @param publicKey the value for sys_secret_key.public_key
     *
     * @mbg.generated
     */
    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_secret_key.private_key
     *
     * @return the value of sys_secret_key.private_key
     *
     * @mbg.generated
     */
    public String getPrivateKey() {
        return privateKey;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_secret_key.private_key
     *
     * @param privateKey the value for sys_secret_key.private_key
     *
     * @mbg.generated
     */
    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_secret_key
     *
     * @mbg.generated
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", publicKey=").append(publicKey);
        sb.append(", privateKey=").append(privateKey);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_secret_key
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
        SysSecretKeyWithBLOBs other = (SysSecretKeyWithBLOBs) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUuid() == null ? other.getUuid() == null : this.getUuid().equals(other.getUuid()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getPublicKey() == null ? other.getPublicKey() == null : this.getPublicKey().equals(other.getPublicKey()))
            && (this.getPrivateKey() == null ? other.getPrivateKey() == null : this.getPrivateKey().equals(other.getPrivateKey()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_secret_key
     *
     * @mbg.generated
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUuid() == null) ? 0 : getUuid().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getPublicKey() == null) ? 0 : getPublicKey().hashCode());
        result = prime * result + ((getPrivateKey() == null) ? 0 : getPrivateKey().hashCode());
        return result;
    }
}