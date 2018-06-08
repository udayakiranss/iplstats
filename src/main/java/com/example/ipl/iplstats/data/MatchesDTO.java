package com.example.ipl.iplstats.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class MatchesDTO {
    private String id;
    private String season;
    private String city;
    private Date date;
    private String team1;
    private String team2;
    private String toss_winner;
    private String toss_decision;
    private String result;
    private String dl_applied;
    private String winner;
    private String win_by_runs;
    private String win_by_wickets;
    private String player_of_match ;
    private String venue;
    private String umpire1;
    private String umpire2;
    private String umpire3;

//    public MatchesDTO(String season){
//
//    }

    public MatchesDTO(){

    }

}
