package com.maimeng.apigateway.config.filter;

import com.maimeng.apigateway.config.jwt.JwtUtils;
import com.xiaoleilu.hutool.date.DateUtil;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.List;

import static com.maimeng.apigateway.common.Constant.USER_ID;
import static com.xiaoleilu.hutool.date.DatePattern.NORM_DATETIME_MINUTE_PATTERN;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * 校验jwt的filter
 * @author wuweifeng wrote on 2018/10/24.
 */
@Component
public class AuthSignFilter implements GlobalFilter, Ordered {
    @Resource
    private JwtUtils jwtUtils;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest serverHttpRequest = exchange.getRequest();
        HttpHeaders httpHeaders = serverHttpRequest.getHeaders();
        String url = serverHttpRequest.getURI().toString();
        //登录请求放行
        if (url.endsWith("authserver/user/login")) {
            return chain.filter(exchange);
        }

        List<String> list = httpHeaders.get(AUTHORIZATION);
        if (list.size() == 0) {
            //没有Authorization
            return noAuth(exchange);
        }
        String jwtToken = list.get(0);
        Claims claims = jwtUtils.getClaimByToken(jwtToken);
        if (claims == null) {
            return noAuth(exchange);
        }
        logger.info("token的过期时间是：" + DateUtil.format(claims.getExpiration(), NORM_DATETIME_MINUTE_PATTERN));
        if (jwtUtils.isTokenExpired(claims.getExpiration())) {
            return noAuth(exchange);
        }

        String userId = claims.getSubject();
        //向headers中放文件，记得build
        ServerHttpRequest request = serverHttpRequest.mutate().header(USER_ID, userId).build();
        //将现在的request 变成 change对象 
        ServerWebExchange build = exchange.mutate().request(request).build();

        return chain.filter(build);
    }

    private Mono<Void> noAuth(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
