package com.maimeng.apigateway.service;

import com.maimeng.apigateway.config.jwt.KeyConfiguration;
import com.maimeng.apigateway.manager.PtUserManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author wuweifeng wrote on 2018/10/30.
 */
@Service
public class PtUserService {
    @Resource
    private PtUserManager ptUserManager;
    @Resource
    private KeyConfiguration keyConfiguration;

}
