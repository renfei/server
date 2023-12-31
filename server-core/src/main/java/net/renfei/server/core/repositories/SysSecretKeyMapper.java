package net.renfei.server.core.repositories;

import java.util.List;
import net.renfei.server.core.repositories.entity.SysSecretKey;
import net.renfei.server.core.repositories.entity.SysSecretKeyExample;
import net.renfei.server.core.repositories.entity.SysSecretKeyWithBLOBs;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SysSecretKeyMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_secret_key
     *
     * @mbg.generated
     */
    long countByExample(SysSecretKeyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_secret_key
     *
     * @mbg.generated
     */
    int deleteByExample(SysSecretKeyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_secret_key
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_secret_key
     *
     * @mbg.generated
     */
    int insert(SysSecretKeyWithBLOBs row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_secret_key
     *
     * @mbg.generated
     */
    int insertSelective(SysSecretKeyWithBLOBs row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_secret_key
     *
     * @mbg.generated
     */
    List<SysSecretKeyWithBLOBs> selectByExampleWithBLOBs(SysSecretKeyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_secret_key
     *
     * @mbg.generated
     */
    List<SysSecretKey> selectByExample(SysSecretKeyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_secret_key
     *
     * @mbg.generated
     */
    SysSecretKeyWithBLOBs selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_secret_key
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("row") SysSecretKeyWithBLOBs row, @Param("example") SysSecretKeyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_secret_key
     *
     * @mbg.generated
     */
    int updateByExampleWithBLOBs(@Param("row") SysSecretKeyWithBLOBs row, @Param("example") SysSecretKeyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_secret_key
     *
     * @mbg.generated
     */
    int updateByExample(@Param("row") SysSecretKey row, @Param("example") SysSecretKeyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_secret_key
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(SysSecretKeyWithBLOBs row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_secret_key
     *
     * @mbg.generated
     */
    int updateByPrimaryKeyWithBLOBs(SysSecretKeyWithBLOBs row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_secret_key
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(SysSecretKey row);
}