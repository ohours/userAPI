package org.ohours.userAPI.controller.exception;

public abstract class UserException extends RuntimeException {
    private final String title;

    UserException(String title, String message) {
        super(message);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
