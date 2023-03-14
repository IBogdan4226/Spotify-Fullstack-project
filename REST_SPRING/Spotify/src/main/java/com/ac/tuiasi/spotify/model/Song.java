package com.ac.tuiasi.spotify.model;

import com.ac.tuiasi.spotify.enums.Genre;
import com.ac.tuiasi.spotify.enums.Type;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "songs")
public
class Song extends RepresentationModel<Song> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 100, nullable = false)
    private String name;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Genre genre;

    @Column(length = 4, nullable = false)
    private String year;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(nullable = true)
    private Integer parent;

    public Song() {
    }

    public Song(String name, Genre genre, String year, Type type) {
        this.name = name;
        this.genre = genre;
        this.year = year;
        this.type = type;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return this.type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Genre getGenre() {
        return this.genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public String getYear() {
        return this.year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Song))
            return false;
        Song song = (Song) o;
        return Objects.equals(this.id, song.id) && Objects.equals(this.name, song.name)
                && Objects.equals(this.genre, song.genre) && Objects.equals(this.type, song.type)
                && Objects.equals(this.year, song.year);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name, this.genre, this.year, this.type);
    }

    @Override
    public String toString() {
        return "Song{" + "id=" + this.id + ", name='" + this.name;
    }
}