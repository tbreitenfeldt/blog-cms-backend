package com.timothybreitenfeldt.blog.filter;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.timothybreitenfeldt.blog.util.JWTUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String token = this.getJWT(request);

            if (token != null) {
                Claims jwtClaims = this.jwtUtil.validateToken(token);
                String username = jwtClaims.getSubject();
                System.out.println(jwtClaims);
                @SuppressWarnings("unchecked")
                List<LinkedHashMap<String, String>> extractedClaimAuthorities = jwtClaims.get("authorities",
                        List.class);
                List<GrantedAuthority> authorities = extractedClaimAuthorities.stream()
                        .map((authority) -> new SimpleGrantedAuthority(authority.get("authority")))
                        .collect(Collectors.toList());
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, null,
                        authorities);

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (JwtException e) {
            SecurityContextHolder.getContext().setAuthentication(null);
        } finally {
            filterChain.doFilter(request, response);
        }
    }

    private String getJWT(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring("Bearer ".length());
        }

        return jwt;
    }

}
