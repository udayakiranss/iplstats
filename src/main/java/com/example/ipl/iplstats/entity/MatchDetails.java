package com.example.ipl.iplstats.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Component
@Entity
@Data
@Table(name = "MATCH_DETAILS")
public class MatchDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long matchID;
    private int innings;
    private  String battingTeam;
    private  String bowlingTeam;
    private int over;
    private int ball;
    @ManyToOne
    @JoinColumn(name="batsman")
    private Player batsman;
    @ManyToOne
    @JoinColumn(name="nonStriker")
    private Player nonStriker;
    @ManyToOne
    @JoinColumn(name="bowler")
    private Player bowler;
    private boolean isSuperOver;
    private int wideRuns;
    private int byeRuns;
    private int legbyeRuns;
    private int noballRuns;
    private int penaltyRuns;
    private int batsmanRuns;
    private int extraRuns;
    private int totalRuns;
    @ManyToOne
    @JoinColumn(name="playerDismissed")
    private Player playerDismissed;
    private String dismissalKind;
    @ManyToOne
    @JoinColumn(name="fielder")
    private Player fielder;
    @ManyToOne
    @JoinColumn(name = "match_summary_id")
    private MatchSummary matchSummary;
}
