package com.ac.tuiasi.spotify.specifications;

import com.ac.tuiasi.spotify.enums.Genre;
import com.ac.tuiasi.spotify.model.Song;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class SongWithGenre implements Specification<Song> {
    private final Genre genre;

    public SongWithGenre(Genre genre) {
        this.genre = genre;
    }

    @Override
    public Predicate toPredicate(Root<Song> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        if (this.genre == null) {
            return cb.isTrue(cb.literal(true));
        } else {
            return cb.equal(root.get("genre"), this.genre);
        }
    }
}
