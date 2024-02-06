package com.example.petcommunity.security.jwt.admin;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtAdminTokenProvider {

    private final Key adminKey;

    public JwtAdminTokenProvider(@Value("${jwt.adminSecret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.adminKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public JwtAdminToken createAdminToken(String username, Collection<? extends GrantedAuthority> authorities) {
        long now = (new Date()).getTime();
        // 관리자의 권한 정보 설정 ("ROLE_ADMIN")
        String authoritiesString = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date accessTokenExpiresIn = new Date(now + 3600000);
        String accessToken = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(username)
                .claim("auth", authoritiesString)
                .setExpiration(accessTokenExpiresIn)
                .signWith(adminKey, SignatureAlgorithm.HS256)
                .compact();

        Date refreshTokenExpiresIn = new Date(now + 604800000);
        String refreshToken = Jwts.builder()
                .setExpiration(refreshTokenExpiresIn)
                .signWith(adminKey, SignatureAlgorithm.HS256)
                .compact();

        return JwtAdminToken.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseAdminClaims(accessToken);
        if (claims.get("auth") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get("auth").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public boolean validateAdminToken(String token) { // JWT 토큰이 유효한지 검증
        try {
            Jwts.parserBuilder()
                    .setSigningKey(adminKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token");
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty");
        }
        return false;
    }

    private Claims parseAdminClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(adminKey)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
