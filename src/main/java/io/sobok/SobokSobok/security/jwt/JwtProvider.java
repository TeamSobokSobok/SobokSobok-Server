package io.sobok.SobokSobok.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.InitializingBean;
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

@Component
public class JwtProvider implements InitializingBean {

    private static final String AUTHORITIES_KEY = "auth";
    private static final String TOKEN_TYPE_KEY = "type";
    private static final String ACCESS_TOKEN_TYPE = "access";
    private static final String REFRESH_TOKEN_TYPE = "refresh";

    private final String secretCode;
    private final Long accessTokenExpiration;
    private final Long refreshTokenExpiration;

    private Key key;

    public JwtProvider(
            @Value("${jwt.secret}") String secretCode,
            @Value("${jwt.access-expiration}") Long accessTokenExpiration,
            @Value("${jwt.refresh-expiration}") Long refreshTokenExpiration
    ) {
        this.secretCode = secretCode;
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    @Override
    public void afterPropertiesSet() {

        byte[] keyBytes = Decoders.BASE64.decode(secretCode);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public Jwt createToken(Authentication authentication) {

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String accessToken = generateToken(authentication.getName(), authorities, ACCESS_TOKEN_TYPE, accessTokenExpiration);
        String refreshToken = generateToken(authentication.getName(), "", REFRESH_TOKEN_TYPE, refreshTokenExpiration);

        return Jwt.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public Authentication getAuthentication(String accessToken) {

        Claims claims = parseClaims(accessToken);

        if (claims.get("auth") == null || claims.get("type") == null) {
            throw new RuntimeException("정보가 누락된 토큰입니다.");
        }

        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get("auth").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .toList();

        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    private String generateToken(String subject, String authorities, String tokenType, Long expiration) {

        Long now = (new Date()).getTime();
        Date expirationDate = new Date(now + expiration);

        return Jwts.builder()
                .setSubject(subject)
                .claim(AUTHORITIES_KEY, authorities)
                .claim(TOKEN_TYPE_KEY, tokenType)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(expirationDate)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException |
                 MalformedJwtException |
                ExpiredJwtException |
                UnsupportedJwtException |
                IllegalArgumentException e
        ) {
            return false;
        }
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
