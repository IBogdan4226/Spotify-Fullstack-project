package com.ac.tuiasi.spotify.interfaces;

import com.ac.tuiasi.spotify.model.Song;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface ISongService {
    List<Song> listAll();

    Page<Song> listAllwSpec(Specification sp, PageRequest paging);

    Song get(Integer id);

    Song save(Song song);

    void delete(Integer id);

    Song patch(Integer id, JsonPatch jsonPatch) throws JsonPatchException, JsonProcessingException;
}
