package com.bci.user.common;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class TokenGenerator {

    private static final String SECRET = "Z3VpZGUteW91ci1wcm9qZWN0LXdpdGgtYS12ZXJ5LXNlY3VyZS1rZXktZ2VuZXJhdGVkLWJ5LW9wZW5zc2w=";

    /**
     * Genera y retorna token JWT
     */
    public static String getToken(String valueToToken) {
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_USER");

        SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .id("softtekJWT")
                .subject(valueToToken)
                .claim("authorities", grantedAuthorities.stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 600000)) // 10 minutos
                .signWith(key)
                .compact();
    }
}
