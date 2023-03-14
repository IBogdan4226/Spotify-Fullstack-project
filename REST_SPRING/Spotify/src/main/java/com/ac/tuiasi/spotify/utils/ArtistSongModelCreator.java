package com.ac.tuiasi.spotify.utils;

import com.ac.tuiasi.spotify.controllers.ArtistController;
import com.ac.tuiasi.spotify.controllers.ArtistSongsController;
import com.ac.tuiasi.spotify.controllers.SongCollectionController;
import com.ac.tuiasi.spotify.model.ArtistSongs;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class ArtistSongModelCreator implements RepresentationModelAssembler<ArtistSongs, ArtistSongs> {
    @Override
    public ArtistSongs toModel(ArtistSongs entity) {
        entity.add(linkTo(methodOn(ArtistSongsController.class).delete(entity.getUuid(), entity.getId())).withRel("delete").withType("DELETE"));
        entity.add(linkTo(methodOn(ArtistController.class).listOne(entity.getUuid())).withRel("artist").withType("GET"));
        entity.add(linkTo(methodOn(SongCollectionController.class).listOne(entity.getId())).withRel("song").withType("GET"));
        return entity;
    }
}
