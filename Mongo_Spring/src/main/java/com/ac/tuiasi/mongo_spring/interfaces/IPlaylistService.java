package com.ac.tuiasi.mongo_spring.interfaces;

import com.ac.tuiasi.mongo_spring.exceptions.PlaylistNotFound;
import com.ac.tuiasi.mongo_spring.exceptions.SongNotFound;
import com.ac.tuiasi.mongo_spring.model.Playlist;
import com.ac.tuiasi.mongo_spring.pojo.Song;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.io.IOException;
import java.util.List;

public interface IPlaylistService {
    List<Playlist> listAll();

    Playlist get(String id) throws PlaylistNotFound;

    Playlist save(Playlist playlist);

    Playlist addSongs(Integer id, List<Song> songs) throws ChangeSetPersister.NotFoundException;

    void delete(String id);

    Song getSongFromSql(Integer id) throws IOException, InterruptedException, SongNotFound;

    String getArtistNameFromSql(Integer id) throws IOException, InterruptedException;
}