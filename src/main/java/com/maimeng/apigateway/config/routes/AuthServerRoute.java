package com.maimeng.apigateway.config.routes;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wuweifeng wrote on 2018/10/29.
 */
@Component
public class AuthServerRoute extends BaseRoute {
    @Override
    public String path() {
        return "/a/**";
    }

    @Override
    public String url() {
        return "http://baidu.com";
    }

    @Override
    public Map<String, Map<String, String>> filters() {
        Map<String, Map<String, String>> mapMap = new HashMap<>();
        Map<String, String> map = new HashMap<>();
        map.put("name", "name");
        mapMap.put("AddRequestParameter", map);
        return mapMap;
    }
}
