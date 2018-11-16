package com.maimeng.apigateway.core.repository;

import com.maimeng.apigateway.core.model.PtRole;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author wuweifeng wrote on 2017/10/26.
 */
public interface PtRoleRepository extends JpaRepository<PtRole, Long> {
    PtRole findByName(String name);
}
