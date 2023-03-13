package org.ohours.userAPI.controller;

import jakarta.validation.Valid;
import org.ohours.userAPI.controller.exception.UserNotFoundException;
import org.ohours.userAPI.model.MyUser;
import org.ohours.userAPI.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserService service;

    @GetMapping("/users/{username}")
    public MyUser getUserByName(@Valid @PathVariable String username) {
        return service.getUser(username).orElseThrow(() -> new UserNotFoundException(username));
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public void newUser(@Valid @RequestBody MyUser newUSer) {
        service.createUser(newUSer);
    }

}
