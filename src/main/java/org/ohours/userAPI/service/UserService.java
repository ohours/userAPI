package org.ohours.userAPI.service;

import org.ohours.userAPI.model.MyUser;

import java.util.Optional;

public interface UserService {
    MyUser createUser(MyUser user);

    Optional<MyUser> getUser(String username);
}
