package com.ac.tuiasi.mongo_spring.exceptions;

public class SongNotFound extends Exception {
    public SongNotFound(Integer id) {
        super("Song not found with id: " + id);
    }
}
