package com.ATUserManagement.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    private Date timestamp;
    private String status;
    private String error;
    private String requestDetail;

}
