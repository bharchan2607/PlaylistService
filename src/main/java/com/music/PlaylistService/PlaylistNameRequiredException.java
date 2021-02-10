package com.music.PlaylistService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PlaylistNameRequiredException extends RuntimeException {
    public PlaylistNameRequiredException(String message) {
        super(message);
    }
}
