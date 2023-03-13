package org.ohours.userAPI.controller.advice;

import java.util.List;

public class ErrorsResponseEntity {
    private List<ErrorEntity> errors;

    public ErrorsResponseEntity(List<ErrorEntity> errors) {
        this.errors = errors;
    }

    public List<ErrorEntity> getErrors() {
        return errors;
    }

    public void setErrors(List<ErrorEntity> errors) {
        this.errors = errors;
    }
}
