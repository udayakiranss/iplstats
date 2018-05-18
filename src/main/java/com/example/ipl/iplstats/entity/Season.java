package com.example.ipl.iplstats.entity;

import lombok.Data;
import lombok.Generated;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Season {
    @Id
    @GeneratedValue
    private Long id;
    private String year;
    private String description;


}
