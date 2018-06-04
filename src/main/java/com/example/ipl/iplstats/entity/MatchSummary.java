package com.example.ipl.iplstats.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class MatchSummary {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int matchID;
    private Date date;
    private String city;
    private String venue;

    private String teamA;
    private String teamB;
    private String tossWinner;
    private String tossDecision;
    private String winner;
    private int winByRuns;
    private int winByWickets;

    @ManyToOne
    @JoinColumn(name="season_id")
    private Season season;

}
