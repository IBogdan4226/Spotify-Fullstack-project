package com.ac.tuiasi.spotify.specifications;

import com.ac.tuiasi.spotify.model.Song;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class SongWithYear implements Specification<Song> {
    private final Integer year;


    public SongWithYear(Integer year) {
        this.year = year;
    }

    @Override
    public Predicate toPredicate(Root<Song> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        if (this.year == null) {
            return cb.isTrue(cb.literal(true));
        } else {
            return cb.equal(root.get("year"), this.year);
        }
    }
}
