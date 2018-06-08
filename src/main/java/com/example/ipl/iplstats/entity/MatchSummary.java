package com.example.ipl.iplstats.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class MatchSummary {
    @Id
    private Long matchID;
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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="season_id")
    private Season season;


    public MatchSummary(String id, String city, String venue, String teamA, String teamB, String tossWinner,
                           String tossDecision,
                           String winner, String winByRuns, String winByWickets){
        this.city= city;
        this.venue = venue;
        this.teamA = teamA;
        this.teamB = teamB;
        this.tossWinner = tossWinner;
        this.tossDecision = tossDecision;
        this.winner = winner;
        this.winByRuns = new Integer(winByRuns).intValue();
        this.winByWickets = new Integer(winByWickets).intValue();;
        this.matchID = new Long(id).longValue();
    }

}
