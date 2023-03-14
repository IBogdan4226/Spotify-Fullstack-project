package com.ac.tuiasi.spotify.model;

import com.ac.tuiasi.spotify.pojo.ArtistSongsKey;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;


@Entity
@IdClass(ArtistSongsKey.class)
@Table(name = "artist_songs")
public class ArtistSongs extends RepresentationModel<ArtistSongs> {
    @Id
    @Column(name = "artist_uuid")
    private Integer uuid;
    @Id
    @Column(name = "song_id")
    private Integer id;

    public ArtistSongs(Integer uuid, Integer id) {
        this.uuid = uuid;
        this.id = id;
    }

    public ArtistSongs() {

    }

    public Integer getUuid() {
        return uuid;
    }

    public void setUuid(Integer uuid) {
        this.uuid = uuid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}