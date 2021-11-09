package com.generation.SpringSecurityJWT.controller;

import com.generation.SpringSecurityJWT.model.User;
import com.generation.SpringSecurityJWT.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Entity;

@RestController
@RequestMapping("/api/user") //aplica prefijoa  todas las url de los metodos
public class UserController {

    private final UserService userService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserController(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {

        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder; //para encriptar contrañseas
    }

    @PostMapping
    public User saveUser(@RequestBody User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword())); //realiza encriptacion de password
        return userService.save(user);
    } //envia a traves de body


    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }


}
