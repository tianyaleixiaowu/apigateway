package com.maimeng.apigateway.config.routes;

import com.alibaba.fastjson.JSON;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.reactive.HiddenHttpMethodFilter;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

import static com.maimeng.apigateway.core.repository.RedisRouteDefinitionRepository.GATEWAY_ROUTES;


/**
 * @author wuweifeng wrote on 2018/10/26.
 */
@Component
public class RouteConfig {
    @Resource
    private StringRedisTemplate redisTemplate;

    @Resource
    private List<BaseRoute> routeConfigs;

    /**
     * 防止post的Only one connection receive subscriber allowed.异常
     */
    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
                return chain.filter(exchange);
            }
        };
    }

    /**
     * 启动时加载所有的filter
     */
    @PostConstruct
    public void main() {
        int i = 1;
        for (BaseRoute baseRoute : routeConfigs) {
            redisTemplate.opsForHash().put(GATEWAY_ROUTES, "key" + i++, JSON.toJSONString(baseRoute.buildRoute()));
        }
    }
}
