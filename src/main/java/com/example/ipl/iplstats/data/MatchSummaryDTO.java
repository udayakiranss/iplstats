package com.example.ipl.iplstats.data;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class MatchSummaryDTO {

    private int matchID;
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

}
