package es.uv.sersomon.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import jakarta.annotation.PostConstruct;

@Component
public class JwtService {
    @Value("${sys.token.key}")
    private String key;
    @Value("${sys.token.issuer}")
    private String issuer;
    @Value("${sys.token.duration}")
    private Integer duration;

    private Algorithm algorithm;
    private JWTVerifier verifier;

    @PostConstruct
    public void init() {
        this.algorithm = Algorithm.HMAC256(this.key.getBytes());
        this.verifier = JWT.require(this.algorithm).build();
    }

    public String generateAccessToken(String username, List<String> claims) {
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + this.duration))
                .withIssuer(this.issuer)
                .withClaim("roles", claims)
                .sign(this.algorithm);
    }

    public String generateRefreshToken(int id, String claim) {
        return JWT.create()
                .withSubject(String.valueOf(id))
                .withExpiresAt(new Date(System.currentTimeMillis() + (this.duration * 2)))
                .withIssuer(this.issuer)
                .withClaim("role", claim)
                .sign(this.algorithm);
    }

    public int getIdFromToken(String token) {
        DecodedJWT decoded = this.verifier.verify(token);
        return Integer.valueOf(decoded.getSubject()).intValue();
    }

    public SimpleGrantedAuthority getRoleFromToken(String token) {
        DecodedJWT decoded = this.verifier.verify(token);
        String role = decoded.getClaim("role").asString();
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);
        return authority;
    }

    public Boolean isTokenExpired(String token) {
        DecodedJWT decoded = this.verifier.verify(token);
        final Date expiration = decoded.getExpiresAt();
        return expiration.before(new Date());
    }

    public String getTokenFromHeader(String header) {
        return header.substring(this.getTokenHeaderPrefix().length());
    }

    public String getTokenHeaderPrefix() {
        return "Bearer ";
    }

}
