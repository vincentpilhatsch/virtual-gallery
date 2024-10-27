package com.vincent.gallerybackend.exceptions;

public class ExpiredJwtCustomException extends RuntimeException {

    public ExpiredJwtCustomException(String message) {
        super(message);
    }

}
