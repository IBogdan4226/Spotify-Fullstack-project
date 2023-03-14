package com.ac.tuiasi.spotify.specifications;

import com.ac.tuiasi.spotify.model.Song;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class SongWithParent implements Specification<Song> {
    private final Integer parent;

    public SongWithParent(Integer parentID) {
        this.parent = parentID;
    }

    @Override
    public Predicate toPredicate(Root<Song> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        if (this.parent == null) {
            return cb.isTrue(cb.literal(true));
        } else {
            return cb.equal(root.get("parent"), this.parent);
        }
    }
}