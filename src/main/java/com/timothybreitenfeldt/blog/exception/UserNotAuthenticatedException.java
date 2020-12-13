package com.timothybreitenfeldt.blog.exception;

public class UserNotAuthenticatedException extends RuntimeException {

    private static final long serialVersionUID = 3359038845760932202L;

    public UserNotAuthenticatedException(String message) {
        super(message);
    }

}
