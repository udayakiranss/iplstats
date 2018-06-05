package com.example.ipl.iplstats.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@EqualsAndHashCode(of = "teamID,name,season")
public class SeasonTeam {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long teamID;
    @NotNull
    private String name;
    private String description;
    private String baseLocation;
    private int totalMatches;
    private int winCount;
    private int lostCount;
    private int points;
    @ManyToOne
    @JoinColumn(name="season_id")
    private Season season;
//    @Column(name = "season_id")
//    private Integer seasonID;





}
