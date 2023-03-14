package com.ac.tuiasi.mongo_spring.exceptions;

public class PlaylistNotFound extends Exception {
    public PlaylistNotFound(String id) {
        super("Playlist not found with id: " + id);
    }
}