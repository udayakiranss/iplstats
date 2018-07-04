package com.example.ipl.iplstats.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Table(name = "MATCH_SUMMARY")
public class MatchSummary {
    @Id
    private Long matchID;
    private Date date;
    private String city;
    private String venue;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="teamA")
    private Team teamA;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="teamB")
    private Team teamB;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="tossWinner")
    private Team tossWinner;
    private String tossDecision;
    private String result;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="winner")
    private Team winner;
    private int winByRuns;
    private int winByWickets;
    private String playerOfMatch;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="season_id")
    private Season season;


    public MatchSummary(String id, String city, String venue, Team teamA, Team teamB, Team tossWinner,
                           String tossDecision,
                        Team winner, String winByRuns, String winByWickets){
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
