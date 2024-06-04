package es.uv.sersomon.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.uv.sersomon.models.TokenInfo;
import es.uv.sersomon.service.JwtService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import es.uv.sersomon.models.TokenInfo;
import es.uv.sersomon.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@Controller
@RequestMapping("/api/v1/jwt")
public class AuthController {

    @Autowired
    private JwtService jwtService;

    @PostMapping("/auth")
    @Operation(summary = "Check JWT Token", description = "Validates the JWT token provided in the Authorization header")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token validated successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TokenInfo.class)) }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden, token is invalid or expired", content = @Content),
            @ApiResponse(responseCode = "422", description = "Unprocessable entity, Authorization header is missing or malformed", content = @Content)
    })
    public ResponseEntity<?> checkJwtToken(@RequestBody String authHeader) {

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                String token = jwtService.getTokenFromHeader(authHeader);
                int id = jwtService.getIdFromToken(token);
                String role = jwtService.getRoleFromToken(token);
                return new ResponseEntity<>(new TokenInfo(id, role), HttpStatus.OK);
            } catch (Exception exception) {
                return new ResponseEntity<>(exception.getMessage(), HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>("", HttpStatus.UNPROCESSABLE_ENTITY);
        }

    }

}
