package com.ATUserManagement.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FOUND)
@Getter
@Setter
public class UserExistException extends Exception {

    public UserExistException(String message) {
        super(message);
    }
}
