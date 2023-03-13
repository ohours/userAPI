package org.ohours.userAPI.controller.exception;

public class UsernameTakenException extends UserException {

    public UsernameTakenException(String username) {
        super("UsernameTaken", "Username " + username + " is taken, choose another");
    }
}
