package com.maimeng.apigateway.config.filter;

import com.maimeng.apigateway.config.jwt.JwtUtils;
import com.maimeng.apigateway.core.service.PtUserService;
import com.xiaoleilu.hutool.date.DateUtil;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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
 * 校验jwt的filter。还需要校验用户的权限，userRole->roleMenu校验
 * @author wuweifeng wrote on 2018/10/24.
 */
@Component
public class AuthSignFilter implements GlobalFilter, Ordered {
    @Resource
    private JwtUtils jwtUtils;
    @Value("${gate.ignore.startWith}")
    private String startWith;
    @Value("${gate.ignore.contain}")
    private String contain;
    @Resource
    private PtUserService ptUserService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest serverHttpRequest = exchange.getRequest();
        //类似于 /youplus/company/error
        String requestPath = serverHttpRequest.getPath().pathWithinApplication().value();
        // 不进行拦截的地址
        if (isStartWith(requestPath) || isContains(requestPath)) {
            return chain.filter(exchange);
        }

        HttpHeaders httpHeaders = serverHttpRequest.getHeaders();
        List<String> list = httpHeaders.get(AUTHORIZATION);
        if (list == null || list.size() == 0) {
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

        //获取用户ID，开始校验用户的菜单权限
        String userId = claims.getSubject();

        String method = serverHttpRequest.getMethodValue().toUpperCase();
        if (!ptUserService.checkMenu(Long.valueOf(userId), requestPath, method)) {
            //没有Authorization    TODO
            //return noAuth(exchange);
        }

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


    /**
     * 是否包含某种特征
     */
    private boolean isContains(String requestUri) {
        for (String s : contain.split(",")) {
            if (requestUri.contains(s)) {
                return true;
            }
        }
        return false;
    }

    /**
     * URI是否以什么打头
     */
    private boolean isStartWith(String requestUri) {
        for (String s : startWith.split(",")) {
            if (requestUri.startsWith(s)) {
                return true;
            }
        }
        return false;
    }


}
