package com.nonce.restsecurity.config;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Andon
 * @date 2019/12/11
 * <p>
 * JwtToken解析并生成authentication身份信息过滤器
 */
@SuppressWarnings("unchecked")
@Slf4j
@Component
public class JwtAuthorizationTokenFilter extends OncePerRequestFilter {

    @Value("${jwt.token-header-key}")
    private String tokenHeaderKey; //token请求头Key
    @Value("${jwt.token-prefix}")
    private String tokenPrefix; //token前缀
    @Value("${jwt.token-secret}")
    private String tokenSecret; //token秘钥

    /**
     * 解析token并生成authentication身份信息
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String token = request.getHeader(tokenHeaderKey);
        log.info("JwtAuthorizationTokenFilter >> token:{}", token);
        if (null == token || !token.startsWith(tokenPrefix + " ")) {
            chain.doFilter(request, response);
            return;
        }
        Claims claims;
        try {
            // 解析token
            claims = Jwts.parser().setSigningKey(tokenSecret).parseClaimsJws(token.replace(tokenPrefix + " ", "")).getBody();
        } catch (Exception e) {
            log.error("JwtToken validity!! error={}", e.getMessage());
            chain.doFilter(request, response);
            return;
        }
        String username = claims.getSubject();
        List<String> roles = claims.get("role", List.class);
        List<SimpleGrantedAuthority> authorities = roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        if (null != username) {
            // 生成authentication身份信息
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }
}
