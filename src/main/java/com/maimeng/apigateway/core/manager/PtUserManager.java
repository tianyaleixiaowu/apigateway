package com.maimeng.apigateway.core.manager;

import com.maimeng.apigateway.core.model.PtUser;
import com.maimeng.apigateway.core.repository.PtUserRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author wuweifeng wrote on 2018/10/30.
 */
@Service
public class PtUserManager {
    @Resource
    private PtUserRepository ptUserRepository;

    /**
     * 根据account查询User
     *
     * @param account
     *         账号名
     * @return 用户
     */
    public PtUser findByAccount(String account) {
        return ptUserRepository.findByAccount(account);
    }
}
