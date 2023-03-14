package com.ac.tuiasi.spotify.repository;

import com.ac.tuiasi.spotify.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface SongRepository extends JpaRepository<Song, Integer>, JpaSpecificationExecutor<Song> {
}
