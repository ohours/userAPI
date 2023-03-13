package org.ohours.userAPI.controller.exception;

public class UserNotFoundException extends UserException {

    public UserNotFoundException(String username) {
        super("UserNotFound", "Could not find user by username " + username);
    }
}
