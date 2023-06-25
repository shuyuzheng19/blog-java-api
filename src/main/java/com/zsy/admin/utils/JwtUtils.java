package com.zsy.admin.utils;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.zsy.admin.constants.Constants;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {
    private static final String SECRET_KEY = "shuyu!zheng@!$#!@";

    private static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET_KEY);

    private static final long EXPIRATION_TIME = Duration.ofDays(Constants.TOKEN_EXPIRE).getSeconds()*1000;

    // 生成 JWT
    public static String generateJwt(String username, Map<String, Object> claims) {

        Date now = new Date();

        Date expiration = new Date(now.getTime() + EXPIRATION_TIME);

        return JWT.create()
                .withSubject(username)
                .withIssuedAt(now)
                .withExpiresAt(expiration)
                .withPayload(claims)
                .sign(ALGORITHM);
    }

    // 解析 JWT
    public static Map<String, Claim> parseJwt(String jwt) {
        try {
            DecodedJWT decodedJWT = JWT.decode(jwt);
            return decodedJWT.getClaims();
        } catch (JWTDecodeException e) {
            // 处理解析失败的异常
            e.printStackTrace();
        }
        return null;
    }

    public static String parseTokenToUsername(String token){
        try {
            DecodedJWT decode = JWT.decode(token);
            return decode.getSubject();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    // 验证 JWT 签名和过期时间
    public static boolean validateJwt(String jwt) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            JWT.require(algorithm).build().verify(jwt);
            return true;
        } catch (JWTVerificationException e) {
            // 处理验证失败的异常
            e.printStackTrace();
        }
        return false;
    }

}
