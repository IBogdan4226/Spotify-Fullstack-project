package com.ac.tuiasi.spotify.services;

import com.ac.tuiasi.spotify.interfaces.ISongService;
import com.ac.tuiasi.spotify.model.Song;
import com.ac.tuiasi.spotify.repository.SongRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional
public class SongService implements ISongService {
    @Autowired
    private SongRepository repo;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public List<Song> listAll() {
        return repo.findAll();
    }

    @Override
    public Page<Song> listAllwSpec(Specification sp, PageRequest page) {
        return repo.findAll(sp, page);
    }

    @Override
    public Song get(Integer id) {
        return repo.findById(id).get();
    }

    @Override
    public Song save(Song song) {
        return repo.save(song);
    }

    @Override
    public void delete(Integer id) {
        repo.deleteById(id);
    }

    @Override
    public Song patch(Integer id, JsonPatch jsonPatch) throws JsonPatchException, JsonProcessingException {
        Song song = repo.findById(id).orElseThrow(EntityNotFoundException::new);
        JsonNode patched = jsonPatch.apply(objectMapper.convertValue(song, JsonNode.class));
        return repo.save(objectMapper.treeToValue(patched, Song.class));
    }

}
