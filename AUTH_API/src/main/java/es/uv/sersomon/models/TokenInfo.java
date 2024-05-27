package es.uv.sersomon.models;

import java.util.Collection;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenInfo {
    private int id;
    private String role;
}
