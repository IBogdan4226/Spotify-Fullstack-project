package com.ac.tuiasi.spotify.interfaces;

import com.ac.tuiasi.spotify.enums.Type;
import com.ac.tuiasi.spotify.model.ArtistSongs;
import com.ac.tuiasi.spotify.model.Song;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface IArtistSongsService {
    List<Song> getAllFromArtist(Integer artistUUID, Type type, PageRequest page);

    ArtistSongs getArtistSong(Integer artistUUID, Integer songID);

    ArtistSongs save(ArtistSongs artistSongs);

    void delete(Integer artistUUID, Integer songID);

}
