package com.maimeng.apigateway.config.jwt;

import com.maimeng.apigateway.common.Constant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

/**
 * Created by ace on 2017/9/10.
 */
public class JWTHelper {
    private static RsaKeyHelper rsaKeyHelper = new RsaKeyHelper();


    /**
     * 公钥解析token
     */
    public static Jws<Claims> parserToken(String token, String pubKeyPath) throws Exception {
        return Jwts.parser().setSigningKey(rsaKeyHelper.getPublicKey(pubKeyPath)).parseClaimsJws
                (token);
    }

    /**
     * 公钥解析token
     */
    public static Jws<Claims> parserToken(String token, byte[] pubKey) throws Exception {
        return Jwts.parser().setSigningKey(rsaKeyHelper.getPublicKey(pubKey)).parseClaimsJws(token);
    }

    /**
     * 获取token中的用户信息
     */
    public static JwtInfo getInfoFromToken(String token, String pubKeyPath) throws Exception {
        Jws<Claims> claimsJws = parserToken(token, pubKeyPath);
        Claims body = claimsJws.getBody();

        return parseClaim(body);
    }

    /**
     * 获取token中的用户信息
     */
    public static JwtInfo getInfoFromToken(String token, byte[] pubKey) throws Exception {
        Jws<Claims> claimsJws = parserToken(token, pubKey);
        Claims body = claimsJws.getBody();
        return parseClaim(body);
    }

    private static JwtInfo parseClaim(Claims claims) {
        return new JwtInfo(claims.getSubject(), getObjectValue(claims.get(Constant.JWT_KEY_NAME)),
                Long.valueOf(getObjectValue(claims.get(Constant.JWT_KEY_CREATETIME))));
    }

    private static String getObjectValue(Object obj) {
        return obj == null ? "" : obj.toString();
    }
}
