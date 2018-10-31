package com.maimeng.apigateway.core.controller;

import com.maimeng.apigateway.core.service.DynamicRouteService;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author wuweifeng wrote on 2018/10/25.
 */
@RestController
@RequestMapping("/route")
public class RouteController {

    @Resource
    private DynamicRouteService dynamicRouteService;

    /**
     * 刷新路由
     */
    @RequestMapping("/refresh")
    public String refresh() {
        dynamicRouteService.refreshRoute();
        return "refresh success";
    }

    /**
     * 增加路由
     */
    @PostMapping("/add")
    public String add(@RequestBody RouteDefinition definition) {
        try {
            return this.dynamicRouteService.add(definition);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id) {
        return this.dynamicRouteService.delete(id);
    }

    @PostMapping("/update")
    public String update(@RequestBody RouteDefinition definition) {
        return this.dynamicRouteService.update(definition);
    }


}