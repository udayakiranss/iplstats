package com.example.ipl.iplstats.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@Entity
@EqualsAndHashCode(of = {"summary","player"})
public class PlayerInnings {
    @Id
    @GeneratedValue
    private Long id;

    private int innings;

    @ManyToOne
    @JoinColumn(name="summary_id")
    private MatchSummary summary;

    @ManyToOne
    @JoinColumn(name="player_id")
    private Player player;

    private int ballsFaced;

    private int boundaries;

    private int sixers;

    private int totalRuns;

    private int totalWickets;

    private int catches;

    private boolean notOut;

    private String dismissalType;




    public void incrementBalls(){
        ballsFaced+=1;
    }

    public void addRuns(int runs){
        totalRuns+=runs;
    }

    public void incrementBoundaries(){
        boundaries+=1;
    }

    public void incrementSixers(){
        sixers+=1;
    }

    public void incrementWickets(){
        totalWickets+=1;
    }

    public void incrementCatches(){
        catches+=1;
    }
}
