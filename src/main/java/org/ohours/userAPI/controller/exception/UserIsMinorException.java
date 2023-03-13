package org.ohours.userAPI.controller.exception;

public class UserIsMinorException extends UserException {

    public UserIsMinorException() {
        super("UserIsMinor", "Currently only accepting adult users");
    }
}
