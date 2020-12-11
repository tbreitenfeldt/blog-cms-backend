package com.timothybreitenfeldt.blog.util;

import java.util.Collection;
import java.util.Date;
import java.util.UUID;
import java.util.function.Function;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.timothybreitenfeldt.blog.exception.UserNotAuthenticatedException;

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
    private Jws<Claims> jwt;

    public String generateToken(Authentication authentication) {
        User principal = (User) authentication.getPrincipal();
        return this.createToken(principal.getUsername(), authentication.getAuthorities());
    }

    public String generateToken(UserDetails userDetails) {
        return this.createToken(userDetails.getUsername(), userDetails.getAuthorities());
    }

    private String createToken(String subject, Collection<? extends GrantedAuthority> athorities) {
        return Jwts.builder().claim("athorities", athorities).setSubject(subject)
                .setIssuer("blog.timothybreitenfeldt.com").setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .setId(UUID.randomUUID().toString()).signWith(this.signatureAlgorithm, this.getSigningKey()).compact();
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> jwt = Jwts.parser().setSigningKey(this.getSigningKey()).parseClaimsJws(token);
            this.jwt = jwt;
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    private SecretKeySpec getSigningKey() {
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(this.SECRET_KEY);
        return new SecretKeySpec(apiKeySecretBytes, this.signatureAlgorithm.getJcaName());
    }

    public <T> T extractClaim(Function<Claims, T> claimsResolver) {
        if (this.jwt == null) {
            throw new UserNotAuthenticatedException(
                    "You must validate your token first by calling 'boolean validateToken(String token)'");
        }

        Claims claims = this.jwt.getBody();
        return claimsResolver.apply(claims);
    }

    public String extractSubject() {
        return this.extractClaim(Claims::getSubject);
    }

    public Date extractExpiration() {
        return this.extractClaim(Claims::getExpiration);
    }

    public Jws<Claims> getParsedJwt() {
        return this.jwt;
    }

}
