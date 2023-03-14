package com.ac.tuiasi.mongo_spring.utils;

import com.ac.tuiasi.mongo_spring.controller.PlaylistController;
import com.ac.tuiasi.mongo_spring.model.Playlist;
import com.ac.tuiasi.mongo_spring.pojo.PlaylistDTO;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PlaylistModelCreator implements RepresentationModelAssembler<PlaylistDTO,PlaylistDTO> {
    @Override
    public PlaylistDTO toModel(PlaylistDTO entity) {
        return null;
    }
    public PlaylistDTO toModel(Playlist ent){
        PlaylistDTO entity=new PlaylistDTO(ent);
        entity.add(linkTo(methodOn(PlaylistController.class).listOne(entity.getId())).withSelfRel());
        entity.add(linkTo(methodOn(PlaylistController.class).listAll()).withRel(IanaLinkRelations.COLLECTION));
        entity.add(linkTo(methodOn(PlaylistController.class).delete(entity.getId(),"Bearer {token}")).withRel("delete").withType("DELETE"));
        entity.add(linkTo(methodOn(PlaylistController.class).appendSongs(null,entity.getId(),"Bearer {token}")).withRel("append-songs").withType("POST"));
        entity.add(linkTo(methodOn(PlaylistController.class).removeSongs(null,entity.getId(),"Bearer {token}")).withRel("remove-songs").withType("POST"));
        return entity;
    }
}
