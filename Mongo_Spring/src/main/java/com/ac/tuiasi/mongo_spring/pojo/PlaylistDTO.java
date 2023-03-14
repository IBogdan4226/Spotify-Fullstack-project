package com.ac.tuiasi.mongo_spring.pojo;

import com.ac.tuiasi.mongo_spring.model.Playlist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistDTO extends RepresentationModel<PlaylistDTO> {
    private String id;
    private Integer creatorId;
    private String playlistName;
    private Boolean visible;
    private List<Song> songs;

    public PlaylistDTO(Playlist playlist){
        this.id=playlist.getId();
        this.creatorId=playlist.getCreator();
        this.playlistName=playlist.getPlaylistName();
        this.visible=playlist.getVisible();
        this.songs=playlist.getSongs();
    }
}
