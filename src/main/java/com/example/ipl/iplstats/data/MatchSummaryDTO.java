package com.example.ipl.iplstats.data;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@EqualsAndHashCode
@NoArgsConstructor
public class MatchSummaryDTO {

    private int matchID;
    private Date date;
    private String city;
    private String venue;
    private TeamDTO teamA;
    private TeamDTO teamB;
    private TeamDTO tossWinner;
    private String tossDecision;
    private TeamDTO winner;
    private int winByRuns;
    private int winByWickets;
    private PlayerDTO playerOfMatch;

    public MatchSummaryDTO(String city, String venue, TeamDTO teamA, TeamDTO teamB, TeamDTO tossWinner, String tossDecision,
                           TeamDTO winner, String winByRuns, String winByWickets){
        this.city= city;
        this.venue = venue;
        this.teamA = teamA;
        this.teamB = teamB;
        this.tossWinner = tossWinner;
        this.tossDecision = tossDecision;
        this.winner = winner;
        this.winByRuns = new Integer(winByRuns).intValue();
        this.winByWickets = new Integer(winByWickets).intValue();;
    }

    @Override
    public String toString() {
        return "\nMatchSummaryDTO[" +
                "matchID=" + matchID +
                ", date='" + date + '\'' +
                ", teamA=" + teamA +
                ", teamB=" + teamB +
                ", winner=" + winner +
                ']';
    }
}
