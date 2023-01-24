package com.example.vibecap_back.global.config.security;

import com.fasterxml.jackson.databind.ser.Serializers;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {

    private final Logger LOGGER = LoggerFactory.getLogger(JwtTokenProvider.class);
    @Value("${springboot.jwt.audience}")
    private final String audience = "vibecapDefaultServer";
    private String secretKey = Secret.JWT_SECRET_KEY;
    private final long tokenValidMillisecond = 1000L * 60 * 60; // 1분
    private final UserDetailsService memberDetailService;

    @Autowired
    public JwtTokenProvider(UserDetailsService memberDetailService) {
        this.memberDetailService = memberDetailService;
    }

    @PostConstruct
    protected void init() {
        LOGGER.info(String.format("[init] JwtTokenProvider secretKey : %s", secretKey));
        LOGGER.info(String.format("[init] JwtTokenProvider audience : %s", audience));
        /**
         * Base64 인코딩 과정
         * 1. 원본 문자열을 주어진 charset을 이용해 인코딩하여 binary data 형태(byte[])로 표현한다.
         * 2. binary data를 3byte씩 끊어 6bit씩 Base64 테이블에서 대응되는 문자로 바꾼다.
         * = 문자는 문자열의 끝을 의미한다.
         */
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
    }


    /**
     * 전달받은 memberId로 해당 회원에 대한 jwt 생성
     * token payload 구성
     * {
     *     "sub" : {email},
     *     "aud" : {audience},
     *     "exp" : {now} + {tokenValidMillisecond},
     *
     *     "role" : {role}
     * }
     * @param email
     * @param role
     * @return
     */
    public String createToken(String email, String role) {
        // Set registered claim.
        Date now = new Date();
        Claims claims = Jwts.claims()
                .setSubject(email)
                .setAudience(audience)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidMillisecond));
        // Set public claim.
        claims.put("role", role);

        // Base64 방식으로 인코딩된 문자열을 다시 byte[]로 바꾼 뒤 HMAC-SHA 알고리즘을 적용하여 Key 인스턴스를 생성한다.
        // TODO 어떤 방식으로 인코딩된 byte[]인지는 신경쓰지 않는것 같다...
        Key key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey));

        // token 생성
        /**
         * header
         * {
         *      "alg": "HS256,
         *      "typ": "JWT"
         * }
         */
        Map<String, Object> header = new HashMap<>();
        header.put("alg", "HS256");
        header.put("typ", "JWT");
        String token = Jwts.builder()
                .setHeader(header)
                .setClaims(claims)  // payload
                .signWith(key, SignatureAlgorithm.HS256)      // sign
                .compact();

        return token;
    }

    /**
     * token 인증 정보 조회
     * @param token
     * @return
     */
    // TODO credential?
    public Authentication getAuthentication(String token) {
        String email = extractEmail(token);
        UserDetails memberDetails = memberDetailService.loadUserByUsername(email);

        return new UsernamePasswordAuthenticationToken(memberDetails, "",
                memberDetails.getAuthorities());
    }

    /**
     * token에서 memberId 추출
     * @param token
     * @return
     */
    public String extractEmail(String token) {
        String email = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .requireAudience(audience)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        return email;
    }

    /**
     * token에서 email 추출
     * @return email
     */
    public String extractEmail() {
        String token = getToken();
        String email = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .requireAudience(audience)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        return email;
    }

    /**
     * HTTP header에서 token 값 추출
     * @return String
     */
    public String getToken() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getHeader("X-AUTH-TOKEN");
    }

    /**
     * HTTP header에서 token 값 추출
     * @param request
     * @return
     */
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("X-AUTH-TOKEN");
    }

    /**
     * token 만료 여부 검사
     * @param token
     * @return
     */
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build().
                    parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            LOGGER.info("[validateToken] 토큰 유효 체크 예외 발생\n" + e.getMessage());
            return false;
        }
    }
}