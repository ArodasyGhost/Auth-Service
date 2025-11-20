package br.ucsal.Auth_Service.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys; // Importação essencial
import org.springframework.stereotype.Component;

import java.util.Date;
import java.security.Key;

@Component
public class JwtUtil {

    // CHAVE SECRETA PADRONIZADA E SEGURA (Mínimo de 32 caracteres)
    private static final String SECRET_KEY = "CHAVE_SECRETA_PADRAO_PARA_UCSAL_MICROSERVICES_2025_E_MUITO_LONGA";
    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    // Tempo de expiração: 1 hora (60 minutos * 60 segundos * 1000 milissegundos)
    private static final long EXPIRATION_TIME = 1000 * 60 * 60;

    public String gerarToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuer("auth-service")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extrairEmail(String token) {
        return Jwts.parserBuilder() // Sintaxe moderna e segura
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}