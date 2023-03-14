package com.ac.tuiasi.spotify.specifications;

import com.ac.tuiasi.spotify.model.Artist;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class ArtistWithStatus implements Specification<Artist> {
    private final boolean active;

    public ArtistWithStatus(boolean active) {
        this.active = active;
    }

    @Override
    public Predicate toPredicate(Root<Artist> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        if (active) {
            return cb.equal(root.get("active"), this.active);
        } else {
            return cb.isTrue(cb.literal(true));
        }
    }
}
