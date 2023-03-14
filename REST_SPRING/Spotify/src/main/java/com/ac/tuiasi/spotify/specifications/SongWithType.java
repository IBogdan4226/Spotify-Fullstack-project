package com.ac.tuiasi.spotify.specifications;

import com.ac.tuiasi.spotify.enums.Type;
import com.ac.tuiasi.spotify.model.Song;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class SongWithType implements Specification<Song> {
    private final Type type;

    public SongWithType(Type type) {
        this.type = type;
    }

    @Override
    public Predicate toPredicate(Root<Song> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        if (this.type == null) {
            return cb.isTrue(cb.literal(true));
        } else {
            return cb.equal(root.get("type"), this.type);
        }
    }
}