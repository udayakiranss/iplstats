package com.example.ipl.iplstats.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Component
@Entity
@Data
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
    private String batsman;
    private String nonStriker;
    private String bowler;
    private boolean isSuperOver;
    private int wideRuns;
    private int byeRuns;
    private int legbyeRuns;
    private int noballRuns;
    private int penaltyRuns;
    private int batsmanRuns;
    private int extraRuns;
    private int totalRuns;
    private String playerDismissed;
    private String dismissalKind;
    private String fielder;
    @ManyToOne
    @JoinColumn(name = "match_summary_id")
    private MatchSummary matchSummary;
}
