package com.ac.tuiasi.spotify.interfaces;

import com.ac.tuiasi.spotify.model.Artist;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IArtistService {
    List<Artist> listAll();

    Page<Artist> listAllwSpec(Specification sp, PageRequest page);

    Artist get(Integer id);

    Artist save(Artist artist);

    void delete(Integer id);

    Artist patch(Integer id, JsonPatch jsonPatch) throws JsonPatchException, JsonProcessingException;

    JpaRepository getRepo();
}
