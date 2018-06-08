package com.example.ipl.iplstats.entity;

import lombok.Data;
import lombok.NonNull;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
public class Player {
    @Id
    @GeneratedValue
    private Long id;
    @NotNull
    private String name;
    private int NoOfMatches;
    private int totalRuns;
    private int totalWickets;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="team_id",nullable = false)
//    private SeasonTeam team;
}
