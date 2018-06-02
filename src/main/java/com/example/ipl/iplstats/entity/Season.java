package com.example.ipl.iplstats.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
public class Season {
    @Id
    @GeneratedValue
    private Long id;
    private String year;
    private String description;
    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.LAZY, mappedBy = "season")
    private Set<SeasonTeam> teams;

}
