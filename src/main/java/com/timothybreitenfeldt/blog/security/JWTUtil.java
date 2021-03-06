package com.timothybreitenfeldt.blog.security;

import java.util.Collection;
import java.util.Date;
import java.util.UUID;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTUtil {

    @Value("${security.secret-key}")
    private String SECRET_KEY;
    private SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    public String generateToken(UserDetailsImpl userDetailsImpl) {
        return this.createToken(userDetailsImpl.getUsername(), userDetailsImpl.getAuthorities(),
                userDetailsImpl.isAccountNonLocked(), userDetailsImpl.getUserId());
    }

    private String createToken(String subject, Collection<? extends GrantedAuthority> authorities,
            boolean isAccountNonLocked, Long userId) {
        return Jwts.builder().claim("authorities", authorities).claim("is_account_non_locked", isAccountNonLocked)
                .claim("user_id", userId).setSubject(subject).setIssuer("blog.timothybreitenfeldt.com")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .setId(UUID.randomUUID().toString()).signWith(this.signatureAlgorithm, this.getSigningKey()).compact();
    }

    public Claims validateToken(String token) throws JwtException {
        Jws<Claims> jwt = Jwts.parser().setSigningKey(this.getSigningKey()).parseClaimsJws(token);
        return jwt.getBody();
    }

    private SecretKeySpec getSigningKey() {
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(this.SECRET_KEY);
        return new SecretKeySpec(apiKeySecretBytes, this.signatureAlgorithm.getJcaName());
    }

}
