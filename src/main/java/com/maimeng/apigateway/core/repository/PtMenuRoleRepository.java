package com.maimeng.apigateway.core.repository;

import com.maimeng.apigateway.core.model.PtRoleMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author wuweifeng wrote on 2017/10/26.
 */
public interface PtMenuRoleRepository extends JpaRepository<PtRoleMenu, Long> {
    /**
     * 根据role查询所有的菜单
     *
     * @param roleId
     *         role
     * @return 集合
     */
    List<PtRoleMenu> findByRoleId(Long roleId);

    /**
     * 查询某个menu的所有role
     *
     * @param menuId
     *         menuId
     * @return 集合
     */
    List<PtRoleMenu> findByMenuId(Long menuId);

    /**
     * 删除某个菜单的所有记录
     *
     * @param menuId
     *         菜单id
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Query("delete from PtRoleMenu where menuId = ?1")
    void deleteByMenuId(Long menuId);

    /**
     * 删除某个菜单的所有记录
     *
     * @param roleId
     *         角色id
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Query("delete from PtRoleMenu where roleId = ?1")
    void deleteByRoleId(Long roleId);

    /**
     * 查询某个对应关系是否存在
     *
     * @param menuId
     *         菜单id
     * @param roleId
     *         roleId
     * @return menuRole
     */
    PtRoleMenu findFirstByMenuIdAndRoleId(Long menuId, Long roleId);
}
