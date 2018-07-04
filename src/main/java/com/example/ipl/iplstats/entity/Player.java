package com.example.ipl.iplstats.entity;

import lombok.Data;
import lombok.NonNull;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@ToString
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"name","season_id","team_id"})})
public class Player {
    @Id
    @GeneratedValue
    private Long id;
    @NotNull
    private String name;
    private int noOfMatches;
    private int totalRuns;
    private int totalWickets;
    @ManyToOne
    @JoinColumn(name="season_id")
    private Season season;
    @ManyToOne
    @JoinColumn(name="team_id")
    private Team team;

    public Player(String name) {
        this.name = name;
    }

    public Player() {

    }


    public void addRuns(int runs){
        totalRuns+=runs;
    }
}
