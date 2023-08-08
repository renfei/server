package net.renfei.server.core.repositories;

import java.util.List;
import net.renfei.server.core.repositories.entity.SysMenu;
import net.renfei.server.core.repositories.entity.SysMenuExample;
import net.renfei.server.core.repositories.entity.SysMenuWithBLOBs;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SysMenuMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_menu
     *
     * @mbg.generated
     */
    long countByExample(SysMenuExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_menu
     *
     * @mbg.generated
     */
    int deleteByExample(SysMenuExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_menu
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_menu
     *
     * @mbg.generated
     */
    int insert(SysMenuWithBLOBs row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_menu
     *
     * @mbg.generated
     */
    int insertSelective(SysMenuWithBLOBs row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_menu
     *
     * @mbg.generated
     */
    List<SysMenuWithBLOBs> selectByExampleWithBLOBs(SysMenuExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_menu
     *
     * @mbg.generated
     */
    List<SysMenu> selectByExample(SysMenuExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_menu
     *
     * @mbg.generated
     */
    SysMenuWithBLOBs selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_menu
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("row") SysMenuWithBLOBs row, @Param("example") SysMenuExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_menu
     *
     * @mbg.generated
     */
    int updateByExampleWithBLOBs(@Param("row") SysMenuWithBLOBs row, @Param("example") SysMenuExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_menu
     *
     * @mbg.generated
     */
    int updateByExample(@Param("row") SysMenu row, @Param("example") SysMenuExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_menu
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(SysMenuWithBLOBs row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_menu
     *
     * @mbg.generated
     */
    int updateByPrimaryKeyWithBLOBs(SysMenuWithBLOBs row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_menu
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(SysMenu row);
}