package com.ac.tuiasi.mongo_spring.model;

import com.ac.tuiasi.mongo_spring.pojo.Song;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Data
@NoArgsConstructor
@Document(collection = "spotify_playlist")
public class Playlist{
    @Id
    private String id;
    private Integer creatorId;
    private String playlistName;

    private Boolean visible = Boolean.TRUE;
    private List<Song> songs;

    public Playlist(Integer creator, String playlistName, Boolean visible, List<Song> songs) {
        this.creatorId = creator;
        this.playlistName = playlistName;
        this.visible = visible;
        this.songs = songs;
    }

    public String getId() {
        return id;
    }

    public Integer getCreator() {
        return creatorId;
    }

    public void setCreator(Integer creator) {
        this.creatorId = creator;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }
}
