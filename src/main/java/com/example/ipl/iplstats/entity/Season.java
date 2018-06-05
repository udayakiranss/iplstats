package com.example.ipl.iplstats.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@EqualsAndHashCode(of = "id,year")
public class Season {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String year;
    private String description;
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "season")
////    @OneToMany(cascade = CascadeType.ALL)
////    @JoinColumn(name = "season_id", referencedColumnName = "season_id")
//    private Set<SeasonTeam> teams;
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "season")
//    private Set<MatchSummary> matches;

    @Override
    public String toString() {
        return "Season{" +
                "id=" + id +
                ", year='" + year + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}


