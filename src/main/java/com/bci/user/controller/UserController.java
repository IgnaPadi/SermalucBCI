package com.bci.user.controller;

import com.bci.user.entity.User;
import com.bci.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    @Autowired UserService userService;


    /**
     * Crea usuario
     * @param user
     * @return ResponseEntity<Object>
     */
    @RequestMapping (value = "/sign-up", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object>  signUp(@RequestBody User user) throws Exception {
        return userService.setUser(user);

    }

    /**
     * Inicia sesión
     * @param authorization
     * @return ResponseEntity<Object>
     */
    @Operation(security = @SecurityRequirement(name = "BearerAuth"))
    @RequestMapping (value = "/login", method = RequestMethod.POST)
    public ResponseEntity<Object>  login(@Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization){
        return userService.login(authorization);
    }
}
