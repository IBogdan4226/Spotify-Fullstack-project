package com.ac.tuiasi.spotify.utils;

import com.ac.tuiasi.spotify.controllers.ArtistController;
import com.ac.tuiasi.spotify.controllers.ArtistSongsController;
import com.ac.tuiasi.spotify.enums.Type;
import com.ac.tuiasi.spotify.model.Artist;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class ArtistModelCreator implements RepresentationModelAssembler<Artist, Artist> {

    public Artist toModel(Artist entity, Integer page, Integer pageSize, String name, String match, Boolean status) {
        entity.add(linkTo(methodOn(ArtistController.class).listOne(entity.getUuid())).withSelfRel());
        entity.add(linkTo(methodOn(ArtistController.class).listAll(page, pageSize, name, match, status)).withRel(IanaLinkRelations.COLLECTION));
        entity.add(linkTo(methodOn(ArtistController.class).delete(entity.getUuid(), "Bearer {token}")).withRel("delete").withType("DELETE"));
        entity.add(linkTo(methodOn(ArtistSongsController.class).listAllSongsFromArtist(null, null, entity.getUuid(), null)).withRel("all-releases").withType("GET"));
        entity.add(linkTo(methodOn(ArtistSongsController.class).listAllSongsFromArtist(null, null, entity.getUuid(), Type.album)).withRel("albums").withType("GET"));
        return entity;
    }

    @Override
    public Artist toModel(Artist entity) {
        entity.add(linkTo(methodOn(ArtistController.class).listOne(entity.getUuid())).withSelfRel());
        entity.add(linkTo(methodOn(ArtistController.class).listAll(null, null, null, null, null)).withRel(IanaLinkRelations.COLLECTION));
        entity.add(linkTo(methodOn(ArtistController.class).delete(entity.getUuid(), "Bearer {token}")).withRel("delete").withType("DELETE"));
        entity.add(linkTo(methodOn(ArtistSongsController.class).listAllSongsFromArtist(null, null, entity.getUuid(), null)).withRel("all-releases").withType("GET"));
        entity.add(linkTo(methodOn(ArtistSongsController.class).listAllSongsFromArtist(null, null, entity.getUuid(), Type.album)).withRel("albums").withType("GET"));
        return entity;
    }
}
