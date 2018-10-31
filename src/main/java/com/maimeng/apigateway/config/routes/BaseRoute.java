package com.maimeng.apigateway.config.routes;

import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;

/**
 * 每个需要定义路由的服务，继承该类即可
 * @author wuweifeng wrote on 2018/10/29.
 */
public abstract class BaseRoute {

    public abstract String path();

    /**
     * path是否是通配，如/path/**
     * @return boolean
     */
    protected boolean pathRewrite() {
        return true;
    }

    /**
     * 如果开头没有/，则补上
     */
    private String parsePath() {
        String path = path();
        if (!path.startsWith("/")) {
            path = "/" + path();
        }
        return path;
    }

    /**
     * 截掉末尾的/
     */
    private String parseUrl() {
        String url = url();
        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }
        return url;
    }

    /**
     * 如http://localhost:8080/abc 或
     * lb://SC-CONSUMER
     * @return url
     */
    public abstract String url();

    /**
     * Filter-name，key-value</p>
     * 如AddRequestHeader: header-customHeader
     * @return map
     */
    public abstract Map<String, Map<String, String>> filters();


    public RouteDefinition buildRoute() {
        RouteDefinition definition = new RouteDefinition();
        URI uri = UriComponentsBuilder.fromHttpUrl(parseUrl()).build().toUri();
        definition.setUri(uri);

        List<PredicateDefinition> predicateDefinitions = new ArrayList<>();
        //定义第一个断言
        PredicateDefinition predicate = new PredicateDefinition();
        predicate.setName("Path");

        Map<String, String> predicateParams = new HashMap<>(8);
        predicateParams.put("pattern", path());
        predicate.setArgs(predicateParams);
        predicateDefinitions.add(predicate);

        List<FilterDefinition> filterDefinitionList = new ArrayList<>();
        if (pathRewrite()) {
            //定义Filter
            FilterDefinition filter = new FilterDefinition();
            filter.setName("RewritePath");
            Map<String, String> filterParams = new HashMap<>(8);
            //该_genkey_前缀是固定的，见org.springframework.cloud.gateway.support.NameUtils类
            String rewritePath = parsePath().substring(0, parsePath().length() - 2) + "(?<segment>.*)";
            filterParams.put("_genkey_0", rewritePath);
            filterParams.put("_genkey_1", "/$\\{segment}");
            filter.setArgs(filterParams);
            filterDefinitionList.add(filter);
        }

        for (String key : filters().keySet()) {
            FilterDefinition filter = new FilterDefinition();
            filter.setName(key);
            Map<String, String> filterParams = new HashMap<>(8);

            Map<String, String> paramMap = filters().get(key);
            Set<String> keySet = paramMap.keySet();
            String paramKey = keySet.iterator().next();
            filterParams.put("_genkey_0", paramKey);
            filterParams.put("_genkey_1", paramMap.get(paramKey));
            filter.setArgs(filterParams);
            filterDefinitionList.add(filter);

        }

        definition.setFilters(filterDefinitionList);
        definition.setPredicates(predicateDefinitions);
        return definition;
    }

    public static void main(String[] args) {
        String rep = "/a/(?<segment>.*)";
        String replacement = "/${segment}";
        String path = "/a/header";
        String newPath = path.replaceAll(rep, replacement);
        System.out.println(newPath);
    }
}
