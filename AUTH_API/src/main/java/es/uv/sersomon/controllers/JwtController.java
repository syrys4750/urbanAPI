package es.uv.sersomon.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.management.relation.RoleNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException.BadRequest;

import es.uv.sersomon.enums.Roles;
import es.uv.sersomon.exceptions.IdNotFoundException;
import es.uv.sersomon.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;

@Controller
@RequestMapping("/api/v1")
public class JwtController {
    @Autowired
    private JwtService jwtService;

    @GetMapping("/generateJwtToken")
    @Operation(summary = "Generate JWT Token", description = "Generates a JWT token for a given user ID and role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "JWT token generated successfully", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "400", description = "Bad request, ID is required or role is invalid", content = @Content)
    })
    public ResponseEntity<String> generateJwtToken(@RequestParam Integer id, @RequestParam String role)
            throws RoleNotFoundException {
        boolean isRoleValid = Arrays.asList(Roles.values()).stream().anyMatch(r -> r.toString().equals(role));
        if (id == null) {
            throw new IdNotFoundException(
                    "An ID related to the object for which the token is to be created is required.");
        }
        if (!isRoleValid) {
            throw new RoleNotFoundException(
                    "The specified role `" + role + "` is not valid. Valid roles are "
                            + Arrays.toString(Roles.values()));
        }
        String jwtToken = jwtService.generateAccessToken(id.intValue(), role);
        return new ResponseEntity<>(jwtToken, HttpStatus.OK);
    }

}
