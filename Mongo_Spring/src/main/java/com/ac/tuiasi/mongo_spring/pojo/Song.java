package com.ac.tuiasi.mongo_spring.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Song {
    private Integer id;
    private String name;
    private String self;
}
