package com.timothybreitenfeldt.blog.util;

import java.util.Collection;
import java.util.Date;
import java.util.function.Function;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTUtil {

    @Value("${security.secret-key}")
    private String SECRET_KEY;
    private SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    public String extractUsername(String token) {
        return this.extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return this.extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = this.extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(this.getSigningKey()).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return this.extractExpiration(token).before(new Date());
    }

    public String generateToken(Authentication authentication) {
        User principal = (User) authentication.getPrincipal();
        return this.createToken(principal.getUsername(), authentication.getAuthorities());
    }

    public String generateToken(UserDetails userDetails) {
        return this.createToken(userDetails.getUsername(), userDetails.getAuthorities());
    }

    private String createToken(String subject, Collection<? extends GrantedAuthority> athorities) {
        return Jwts.builder().setSubject(subject).claim("athorities", athorities)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(this.signatureAlgorithm, this.getSigningKey()).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = this.extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !this.isTokenExpired(token));
    }

    private SecretKeySpec getSigningKey() {
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(this.SECRET_KEY);
        return new SecretKeySpec(apiKeySecretBytes, this.signatureAlgorithm.getJcaName());

    }

}
