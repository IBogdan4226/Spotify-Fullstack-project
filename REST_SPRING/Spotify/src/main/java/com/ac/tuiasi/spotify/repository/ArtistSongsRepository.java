package com.ac.tuiasi.spotify.repository;

import com.ac.tuiasi.spotify.model.ArtistSongs;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArtistSongsRepository extends JpaRepository<ArtistSongs, Integer> {

    List<ArtistSongs> findByUuid(Integer artistUUID, PageRequest page);

    ArtistSongs findByUuidAndId(Integer artistUUID, Integer songID);

    void deleteByUuidAndId(Integer artistUUID, Integer songID);
}
