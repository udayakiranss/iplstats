package com.example.ipl.iplstats.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@Entity
public class SeasonTeam {
    @Id
    @GeneratedValue
    private Long teamID;
    @NotNull
    private String name;
    @NotNull
    private String description;
    private String baseLocation;
    private int totalMatches;
    private int winCount;
    private int lostCount;
    private int points;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="season_id",nullable = false)
    private Season season;





}
