package com.ac.tuiasi.spotify.controllers;

import com.ac.tuiasi.spotify.enums.Genre;
import com.ac.tuiasi.spotify.enums.Type;
import com.ac.tuiasi.spotify.interfaces.ISongService;
import com.ac.tuiasi.spotify.model.Song;
import com.ac.tuiasi.spotify.soapPackage.Service.SoapService;
import com.ac.tuiasi.spotify.specifications.*;
import com.ac.tuiasi.spotify.utils.SongModelCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/songcollection/songs")
public class SongCollectionController {
    @Autowired
    private ISongService service;
    @Autowired
    private SongModelCreator songModel;
    @Autowired
    private SoapService soapService;

    @GetMapping("")
    public ResponseEntity<?> listAll(
            @RequestParam(defaultValue = "0", required = false) Integer page,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String match,
            @RequestParam(required = false) Genre genre,
            @RequestParam(required = false) Type type,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer parent
    ) {
        if (pageSize != null && pageSize < 1) {
            return new ResponseEntity<>("Invalid page size", HttpStatus.NOT_ACCEPTABLE);
        }

        PageRequest paging = PageRequest.of(page, pageSize);
        Specification<Song> spec = Specification.where(new SongWithName(name, match).and(new SongWithGenre(genre).and(new SongWithYear(year)).and(new SongWithType(type)).and(new SongWithParent(parent))));
        Page<Song> songs = service.listAllwSpec(spec, paging);
        if (songs.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        songs.stream().map(song -> songModel.toModel(song, page, pageSize, name, match, genre, type, year, parent)).collect(Collectors.toList());
        CollectionModel<Song> model = CollectionModel.of(songs);
        model.add(linkTo(methodOn(SongCollectionController.class).listAll(page, pageSize, name, match, genre, type, year, parent)).withSelfRel());
        model.add(linkTo(methodOn(SongCollectionController.class).listAll(page + 1, pageSize, name, match, genre, type, year, parent)).withRel("Next page"));
        if (page != null && page != 0) {
            model.add(linkTo(methodOn(SongCollectionController.class).listAll(page - 1, pageSize, name, match, genre, type, year, parent)).withRel("Previous page"));
        }
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> listOne(@PathVariable("id") Integer id) {
        try {
            Song song = service.get(id);
            Song model = songModel.toModel(song);
            return new ResponseEntity<>(model, HttpStatus.OK);
        } catch (NoSuchElementException ex) {
            return new ResponseEntity<>("No element found by id " + id, HttpStatus.NOT_FOUND);
        }


    }

    @PostMapping("")
    public ResponseEntity<?> add(@RequestBody Song song, @RequestHeader(name = "Authorization", required = false) String jwtToken) {
        if (jwtToken == null || !jwtToken.contains("Bearer")) {
            return new ResponseEntity<>("Authorization Header empty", HttpStatus.UNAUTHORIZED);
        }
        try {
            if (!soapService.validateTokenMinArtist(jwtToken)) {
                return new ResponseEntity<>("Authorization Header invalid", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception err) {
            return new ResponseEntity<>("Authorization is not available at the moment", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        try {
            Song savedSong = service.save(song);
            Song model = songModel.toModel(savedSong);
            return new ResponseEntity<>(model, HttpStatus.CREATED);
        } catch (DataAccessException ex) {
            return new ResponseEntity<>("Not acceptable", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> put(@RequestBody Song song, @PathVariable("id") Integer id, @RequestHeader(name = "Authorization", required = false) String jwtToken) {
        if (jwtToken == null || !jwtToken.contains("Bearer")) {
            return new ResponseEntity<>("Authorization Header empty", HttpStatus.UNAUTHORIZED);
        }
        try {
            if (!soapService.validateTokenMinArtist(jwtToken)) {
                return new ResponseEntity<>("Authorization Header invalid", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception err) {
            return new ResponseEntity<>("Authorization is not available at the moment", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        try {
            Song savedSong = service.get(id);
            savedSong.setName(song.getName());
            savedSong.setGenre(song.getGenre());
            savedSong.setYear(song.getYear());
            savedSong.setType(song.getType());
            service.save(savedSong);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (NoSuchElementException ex) {
            service.save(song);
            Song model = songModel.toModel(song);
            return new ResponseEntity<>(model, HttpStatus.CREATED);
        } catch (DataAccessException ex) {
            return new ResponseEntity<>("Not acceptable", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<?> patchSong(@RequestBody JsonPatch patch, @PathVariable("id") Integer id, @RequestHeader(name = "Authorization", required = false) String jwtToken) {
        if (jwtToken == null || !jwtToken.contains("Bearer")) {
            return new ResponseEntity<>("Authorization Header empty", HttpStatus.UNAUTHORIZED);
        }
        try {
            if (!soapService.validateTokenCM(jwtToken)) {
                return new ResponseEntity<>("Authorization Header invalid", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception err) {
            return new ResponseEntity<>("Authorization is not available at the moment", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        try {
            Song songPatch = service.patch(id, patch);
            Song model = songModel.toModel(songPatch);
            return new ResponseEntity<>(model, HttpStatus.OK);
        } catch (JsonPatchException | JsonProcessingException e) {
            return new ResponseEntity<>("Not acceptable", HttpStatus.NOT_ACCEPTABLE);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("No element found by id " + id, HttpStatus.NOT_FOUND);
        } catch (DataAccessException ex) {
            return new ResponseEntity<>("Conflict with previous data", HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id, @RequestHeader(name = "Authorization", required = false) String jwtToken) {
        if (jwtToken == null || !jwtToken.contains("Bearer")) {
            return new ResponseEntity<>("Authorization Header empty", HttpStatus.UNAUTHORIZED);
        }
        try {
            if (!soapService.validateTokenMinArtist(jwtToken)) {
                return new ResponseEntity<>("Authorization Header invalid", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception err) {
            return new ResponseEntity<>("Authorization is not available at the moment", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        try {
            Song deletedSong = service.get(id);
            Song model = songModel.toModel(deletedSong);
            service.delete(id);
            return new ResponseEntity<Song>(model, HttpStatus.OK);
        } catch (NoSuchElementException ex) {
            return new ResponseEntity<>("No element found by id " + id, HttpStatus.NOT_FOUND);
        }
    }
}
