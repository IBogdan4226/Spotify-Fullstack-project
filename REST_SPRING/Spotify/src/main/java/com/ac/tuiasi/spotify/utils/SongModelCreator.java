package com.ac.tuiasi.spotify.utils;

import com.ac.tuiasi.spotify.controllers.SongCollectionController;
import com.ac.tuiasi.spotify.enums.Genre;
import com.ac.tuiasi.spotify.enums.Type;
import com.ac.tuiasi.spotify.model.Song;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class SongModelCreator implements RepresentationModelAssembler<Song, Song> {

    public Song toModel(Song entity, Integer page, Integer pageSize, String name, String match, Genre genre, Type type, Integer year, Integer parent) {
        entity.add(linkTo(methodOn(SongCollectionController.class).listOne(entity.getId())).withSelfRel());
        entity.add(linkTo(methodOn(SongCollectionController.class).listAll(page, pageSize, name, match, genre, type, year, parent)).withRel(IanaLinkRelations.COLLECTION));
        entity.add(linkTo(methodOn(SongCollectionController.class).delete(entity.getId(), "Bearer {token}")).withRel("delete").withType("DELETE"));
        if (entity.getParent() != null) {
            entity.add(linkTo(methodOn(SongCollectionController.class).listOne(entity.getParent())).withRel("album").withType("GET"));
        }
        entity.add(Link.of("http://localhost:8081/api/playlist/append/{playlistID}").withRel("append-to-playlist").withType("POST"));
        entity.add(Link.of("http://localhost:8081/api/playlist/remove/{playlistID}").withRel("remove-from-playlist").withType("POST"));

        return entity;
    }

    @Override
    public Song toModel(Song entity) {
        entity.add(linkTo(methodOn(SongCollectionController.class).listOne(entity.getId())).withSelfRel());
        entity.add(linkTo(methodOn(SongCollectionController.class).listAll(null, null, null, null, null, null, null, null)).withRel(IanaLinkRelations.COLLECTION));
        entity.add(linkTo(methodOn(SongCollectionController.class).delete(entity.getId(), "Bearer {token}")).withRel("delete").withType("DELETE"));
        if (entity.getParent() != null) {
            entity.add(linkTo(methodOn(SongCollectionController.class).listOne(entity.getParent())).withRel("album").withType("GET"));
        }
        entity.add(Link.of("http://localhost:8081/api/playlist/append/{playlistID}").withRel("append-to-playlist").withType("POST"));
        entity.add(Link.of("http://localhost:8081/api/playlist/remove/{playlistID}").withRel("remove-from-playlist").withType("POST"));

        return entity;
    }
}
