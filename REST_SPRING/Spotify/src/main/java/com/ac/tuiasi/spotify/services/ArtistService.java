package com.ac.tuiasi.spotify.services;

import com.ac.tuiasi.spotify.interfaces.IArtistService;
import com.ac.tuiasi.spotify.model.Artist;
import com.ac.tuiasi.spotify.repository.ArtistRepository;
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

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ArtistService implements IArtistService {
    @Autowired
    private ArtistRepository repo;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public List<Artist> listAll() {
        return repo.findAll();
    }

    @Override
    public Page<Artist> listAllwSpec(Specification sp, PageRequest page) {
        return repo.findAll(sp, page);
    }

    @Override
    public Artist get(Integer id) {
        return repo.findById(id).get();
    }

    @Override
    public Artist save(Artist artist) {
        return repo.save(artist);
    }

    @Override
    public void delete(Integer id) {
        repo.deleteById(id);
    }

    @Override
    public Artist patch(Integer id, JsonPatch jsonPatch) throws JsonPatchException, JsonProcessingException {
        Artist artist = repo.findById(id).orElseThrow(EntityNotFoundException::new);
        JsonNode patched = jsonPatch.apply(objectMapper.convertValue(artist, JsonNode.class));
        return repo.save(objectMapper.treeToValue(patched, Artist.class));
    }

    public ArtistRepository getRepo() {
        return repo;
    }
}
