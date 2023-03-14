package com.ac.tuiasi.mongo_spring.repository;

import com.ac.tuiasi.mongo_spring.model.Playlist;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PlaylistRepository extends MongoRepository<Playlist, String> {
    Playlist findPlaylistByCreator(String creator);

    Playlist findPlaylistByPlaylistName(String playlistName);
}
