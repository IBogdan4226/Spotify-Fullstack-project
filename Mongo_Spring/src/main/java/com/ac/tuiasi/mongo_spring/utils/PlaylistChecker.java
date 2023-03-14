package com.ac.tuiasi.mongo_spring.utils;

import com.ac.tuiasi.mongo_spring.model.Playlist;
import com.ac.tuiasi.mongo_spring.pojo.Song;

import java.util.ArrayList;
import java.util.List;

public class PlaylistChecker {
    public static List<Integer> getAllSongsId(Playlist playlist) {
        List<Integer> ids = new ArrayList<>();
        List<Song> songs = playlist.getSongs();
        if (songs == null) {
            return new ArrayList<>();
        }
        for (Song song : songs) {
            ids.add(song.getId());
        }
        return ids;
    }
}
