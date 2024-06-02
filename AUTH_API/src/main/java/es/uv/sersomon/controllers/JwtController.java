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

@Controller
@RequestMapping("/api/v1")
public class JwtController {
    @Autowired
    private JwtService jwtService;

    @GetMapping("/generateJwtToken")
    public ResponseEntity<String> generateJwtToken(@RequestParam Integer id, @RequestParam String role)
            throws RoleNotFoundException {
        boolean isRoleValid = Arrays.asList(Roles.values()).stream().anyMatch(r -> r.toString().equals(role));
        if (id == null) {
            throw new IdNotFoundException(
                    "An ID related to the object for which the token is to be created is required.");
        }
        if (!isRoleValid) {
            throw new RoleNotFoundException(
                    "The specified role `" + role + "` is not valid. Valid roles are " + Roles.values().toString());
        }
        String jwtToken = jwtService.generateAccessToken(id.intValue(), role);
        return new ResponseEntity<>(jwtToken, HttpStatus.OK);
    }

}
