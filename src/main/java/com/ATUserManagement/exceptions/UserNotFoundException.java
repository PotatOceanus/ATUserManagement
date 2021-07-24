package com.ATUserManagement.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
@Getter
@Setter
public class UserNotFoundException extends Exception {

    public UserNotFoundException(String message) {
        super(message);
    }
}
