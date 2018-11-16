package com.maimeng.apigateway.core.manager;

import com.maimeng.apigateway.core.model.PtMenu;
import com.maimeng.apigateway.core.repository.PtMenuRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author wuweifeng wrote on 2017/10/26.
 */
@Service
public class PtMenuManager {
    @Resource
    private PtMenuRepository ptMenuRepository;

    /**
     * 添加一个菜单
     *
     * @return 菜单
     */
    public PtMenu save(PtMenu ptMenu) {
        return ptMenuRepository.save(ptMenu);
    }

    public void delete(Long id) {
        ptMenuRepository.delete(findOne(id));
    }

    public PtMenu findOne(Long id) {
        return ptMenuRepository.getOne(id);
    }

    public boolean hasChild(Long id) {
        return ptMenuRepository.countByParentIdAndHideIsFalse(id) > 0;
    }
}
