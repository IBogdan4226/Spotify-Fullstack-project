package com.ac.tuiasi.spotify.controllers;

import com.ac.tuiasi.spotify.enums.Type;
import com.ac.tuiasi.spotify.interfaces.IArtistSongsService;
import com.ac.tuiasi.spotify.model.ArtistSongs;
import com.ac.tuiasi.spotify.model.Song;
import com.ac.tuiasi.spotify.soapPackage.Service.SoapService;
import com.ac.tuiasi.spotify.utils.ArtistSongModelCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;
import java.util.NoSuchElementException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/songcollection/artist")
public class ArtistSongsController implements Serializable {
    @Autowired
    private IArtistSongsService service;

    @Autowired
    private ArtistSongModelCreator modelCreator;

    @Autowired
    private SoapService soapService;

    @GetMapping("/{uuid}/songs")
    public ResponseEntity<?> listAllSongsFromArtist(
            @RequestParam(defaultValue = "0", required = false) Integer page,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize,
            @PathVariable("uuid") Integer uuid,
            @RequestParam(required = false) Type type) {
        if (pageSize != null && pageSize < 1) {
            return new ResponseEntity<>("Invalid page size", HttpStatus.NOT_ACCEPTABLE);
        }
        PageRequest paging = PageRequest.of(page, pageSize);
        List<Song> artistSongs = service.getAllFromArtist(uuid, type, paging);
        if (artistSongs.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        CollectionModel<Song> model = CollectionModel.of(artistSongs);
        model.add(linkTo(methodOn(ArtistSongsController.class).listAllSongsFromArtist(page, pageSize, uuid, type)).withSelfRel());
        model.add(linkTo(methodOn(ArtistSongsController.class).listAllSongsFromArtist(page + 1, pageSize, uuid, type)).withRel("Next page"));
        if (page != null && page != 0) {
            model.add(linkTo(methodOn(ArtistSongsController.class).listAllSongsFromArtist(page - 1, pageSize, uuid, type)).withRel("Previous page"));
        }
        return new ResponseEntity<>(model, HttpStatus.OK);

    }

    @PostMapping("/song")
    public ResponseEntity<?> add(@RequestBody ArtistSongs artistSongs, @RequestHeader(name = "Authorization", required = false) String jwtToken) {
        if (jwtToken == null || !jwtToken.contains("Bearer")) {
            return new ResponseEntity<>("Authorization Header empty", HttpStatus.UNAUTHORIZED);
        }
        try {
            if (!soapService.validateTokenArtist(jwtToken, artistSongs.getUuid())) {
                return new ResponseEntity<>("Authorization Header invalid", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception err) {
            return new ResponseEntity<>("Authorization is not available at the moment", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        try {
            ArtistSongs savedEntry = service.save(artistSongs);
            ArtistSongs model = modelCreator.toModel(savedEntry);
            return new ResponseEntity<>(model, HttpStatus.CREATED);
        } catch (DataAccessException ex) {
            return new ResponseEntity<>("Not acceptable", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @DeleteMapping("/{uuid}/song/{id}")
    public ResponseEntity<?> delete(@PathVariable("uuid") Integer uuid, @PathVariable("id") Integer id) {
        try {
            ArtistSongs deleted = service.getArtistSong(uuid, id);
            ArtistSongs model = modelCreator.toModel(deleted);
            service.delete(uuid, id);

            return new ResponseEntity<ArtistSongs>(model, HttpStatus.OK);
        } catch (NoSuchElementException ex) {
            return new ResponseEntity<>("No element found by uuid " + uuid + " and id " + id, HttpStatus.NOT_FOUND);
        }
    }

}
