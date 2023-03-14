package com.ac.tuiasi.mongo_spring.controller;

import com.ac.tuiasi.mongo_spring.exceptions.PlaylistNotFound;
import com.ac.tuiasi.mongo_spring.exceptions.SongNotFound;
import com.ac.tuiasi.mongo_spring.interfaces.IPlaylistService;
import com.ac.tuiasi.mongo_spring.model.Playlist;
import com.ac.tuiasi.mongo_spring.pojo.PlaylistDTO;
import com.ac.tuiasi.mongo_spring.pojo.Song;
import com.ac.tuiasi.mongo_spring.pojo.SongIds;
import com.ac.tuiasi.mongo_spring.soapPackage.Service.SoapService;
import com.ac.tuiasi.mongo_spring.utils.PlaylistChecker;

import com.ac.tuiasi.mongo_spring.utils.PlaylistModelCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/playlist")
public class PlaylistController {
    @Autowired
    private IPlaylistService service;

    @Autowired
    private PlaylistModelCreator modelCreator;

    @Autowired
    private SoapService soapService;
    @GetMapping("")
    public ResponseEntity<?> listAll() {
        List<Playlist> playlistCollection = service.listAll();
        if (playlistCollection.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<PlaylistDTO> collection=new ArrayList<>();
        for(Playlist pl:playlistCollection){
            collection.add(modelCreator.toModel(pl));
        }
        CollectionModel<PlaylistDTO> model= CollectionModel.of(collection);
        model.add(linkTo(methodOn(PlaylistController.class).listAll()).withSelfRel());
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> listOne(@PathVariable String id){
        try{
            Playlist playlist=service.get(id);
            PlaylistDTO model=modelCreator.toModel(playlist);
            return new ResponseEntity<>(model,HttpStatus.OK);
        } catch (PlaylistNotFound ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("")
    public ResponseEntity<?> createPlaylist(@RequestBody Playlist playlist, @RequestHeader (name="Authorization",required = false)  String jwtToken) {
        if(jwtToken==null||!jwtToken.contains("Bearer")){
            return new ResponseEntity<>("Authorization Header empty",HttpStatus.UNAUTHORIZED);
        }
        try{
            if(!soapService.validateTokenClient(jwtToken)){
                return new ResponseEntity<>("Authorization Header invalid",HttpStatus.UNAUTHORIZED);
            }
        }
        catch (Exception err){
            return new ResponseEntity<>("Authorization is not available at the moment",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        try {
            Playlist playlistSaved = service.save(playlist);
            PlaylistDTO model=modelCreator.toModel(playlist);
            return new ResponseEntity<>(model, HttpStatus.CREATED);
        } catch (DataAccessException ex) {
            return new ResponseEntity<>("Not acceptable", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PostMapping("/append/{uuid}")
    public ResponseEntity<?> appendSongs(@RequestBody SongIds songs, @PathVariable("uuid") String uuid, @RequestHeader (name="Authorization",required = false)  String jwtToken) {
        if(jwtToken==null||!jwtToken.contains("Bearer")){
            return new ResponseEntity<>("Authorization Header empty",HttpStatus.UNAUTHORIZED);
        }
        try {
            Playlist playlist = service.get(uuid);
            try{
                if(!soapService.validateTokenPlaylist(jwtToken,playlist.getCreatorId())){
                    return new ResponseEntity<>("Authorization Header invalid",HttpStatus.UNAUTHORIZED);
                }
            }
            catch (Exception err){
                return new ResponseEntity<>("Authorization is not available at the moment",HttpStatus.INTERNAL_SERVER_ERROR);
            }
            List<Integer> playListSongsIds = PlaylistChecker.getAllSongsId(playlist);
            List<Integer> songIds = songs.getSongs();
            for (Integer songId : songIds) {
                if (playListSongsIds.contains(songId)) {
                    continue;
                }
                playListSongsIds.add(songId);
                if (playlist.getSongs() == null) {
                    playlist.setSongs(new ArrayList<>());
                    playlist.getSongs().add(service.getSongFromSql(songId));
                } else {
                    playlist.getSongs().add(service.getSongFromSql(songId));
                }
            }
            service.save(playlist);
            PlaylistDTO model=modelCreator.toModel(playlist);
            return new ResponseEntity<>(model, HttpStatus.OK);
        } catch (PlaylistNotFound ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (SongNotFound ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PostMapping("/remove/{uuid}")
    public ResponseEntity<?> removeSongs(@RequestBody SongIds songs, @PathVariable("uuid") String uuid,@RequestHeader (name="Authorization",required = false)  String jwtToken) {
        if(jwtToken==null||!jwtToken.contains("Bearer")){
            return new ResponseEntity<>("Authorization Header empty",HttpStatus.UNAUTHORIZED);
        }
        try {
            Playlist playlist = service.get(uuid);
            try{
                if(!soapService.validateTokenPlaylist(jwtToken,playlist.getCreatorId())){
                    return new ResponseEntity<>("Authorization Header invalid",HttpStatus.UNAUTHORIZED);
                }
            }
            catch (Exception err){
                return new ResponseEntity<>("Authorization is not available at the moment",HttpStatus.INTERNAL_SERVER_ERROR);
            }
            List<Integer> playListSongsIds = PlaylistChecker.getAllSongsId(playlist);
            if(playListSongsIds.isEmpty()){
                return new ResponseEntity<>(playlist, HttpStatus.OK);
            }
            List<Integer> songIds = songs.getSongs();
            ListIterator<Song> iter=playlist.getSongs().listIterator();
            while(iter.hasNext()){
                if(songIds.contains(iter.next().getId())){
                    iter.remove();
                }
            }
            service.save(playlist);
            PlaylistDTO model=modelCreator.toModel(playlist);
            return new ResponseEntity<>(model, HttpStatus.OK);
        } catch (PlaylistNotFound ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> put(@RequestBody Playlist playlist, @PathVariable("id") String id,@RequestHeader (name="Authorization",required = false)  String jwtToken) {
        if(jwtToken==null||!jwtToken.contains("Bearer")){
            return new ResponseEntity<>("Authorization Header empty",HttpStatus.UNAUTHORIZED);
        }
        try{
            Playlist previousPlaylist=service.get(id);
            try{
                if(!soapService.validateTokenPlaylist(jwtToken,previousPlaylist.getCreatorId())){
                    return new ResponseEntity<>("Authorization Header invalid",HttpStatus.UNAUTHORIZED);
                }
            }
            catch (Exception err){
                return new ResponseEntity<>("Authorization is not available at the moment",HttpStatus.INTERNAL_SERVER_ERROR);
            }
            previousPlaylist.setPlaylistName(playlist.getPlaylistName());
            previousPlaylist.setCreator(playlist.getCreator());
            previousPlaylist.setVisible(playlist.getVisible());
            service.save(previousPlaylist);
            PlaylistDTO model=modelCreator.toModel(previousPlaylist);
            return new ResponseEntity<>(model,HttpStatus.OK);
        } catch (PlaylistNotFound ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (DataAccessException ex) {
        return new ResponseEntity<>("Not acceptable", HttpStatus.NOT_ACCEPTABLE);
    }

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") String id,@RequestHeader (name="Authorization",required = false)  String jwtToken) {
        if(jwtToken==null||!jwtToken.contains("Bearer")){
            return new ResponseEntity<>("Authorization Header empty",HttpStatus.UNAUTHORIZED);
        }
        try {
            Playlist deletedPlaylist = service.get(id);
            try{
                if(!soapService.validateTokenPlaylist(jwtToken,deletedPlaylist.getCreatorId())){
                    return new ResponseEntity<>("Authorization Header invalid",HttpStatus.UNAUTHORIZED);
                }
            }
            catch (Exception err){
                return new ResponseEntity<>("Authorization is not available at the moment",HttpStatus.INTERNAL_SERVER_ERROR);
            }
            service.delete(id);
            PlaylistDTO model=modelCreator.toModel(deletedPlaylist);
            return new ResponseEntity<>(model, HttpStatus.OK);
        } catch (PlaylistNotFound ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }

    }
}
