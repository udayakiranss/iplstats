package com.example.ipl.iplstats.data;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@EqualsAndHashCode()
@ToString
public class MatchDetailsDTO {

    private Long id;
    private Long matchID;
    private int innings;
    private  String battingTeam;
    private  String bowlingTeam;
//    private int over;
//    private int ball;
    private String batsman;
    private String nonStriker;
    private String bowler;
//    private boolean superOver;
//    private int wideRuns;
//    private int byeRuns;
//    private int legbyeRuns;
//    private int noballRuns;
//    private int penaltyRuns;
    private int batsmanRuns;
//    private int extraRuns;
    private int totalRuns;
    private String playerDismissed;
    private String dismissalKind;
    private String fielder;


}
