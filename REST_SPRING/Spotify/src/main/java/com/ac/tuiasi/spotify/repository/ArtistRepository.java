package com.ac.tuiasi.spotify.repository;

import com.ac.tuiasi.spotify.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ArtistRepository extends JpaRepository<Artist, Integer>, JpaSpecificationExecutor<Artist> {
}