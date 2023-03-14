package com.ac.tuiasi.spotify.specifications;

import com.ac.tuiasi.spotify.model.Artist;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class ArtistWithName implements Specification<Artist> {
    private final String name;
    private final String match;

    public ArtistWithName(String name, String match) {
        this.name = name;
        this.match = match;
    }

    @Override
    public Predicate toPredicate(Root<Artist> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        if (this.name == null) {
            return cb.isTrue(cb.literal(true));
        }
        if (this.match == null) {
            return cb.like(root.get("name"), "%" + this.name + "%");
        } else {
            if (this.match.equals("exact")) {
                return cb.equal(root.get("name"), this.name);
            } else {
                return cb.like(root.get("name"), "%" + this.name + "%");
            }
        }
    }
}
