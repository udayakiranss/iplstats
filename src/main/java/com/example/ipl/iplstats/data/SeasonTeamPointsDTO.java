package com.example.ipl.iplstats.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Data
@EqualsAndHashCode(of = "teamName")
@NoArgsConstructor
public class SeasonTeamPointsDTO {

    private String teamName;
    private int totalMatchesPlayed;
    private int wonMatches;
    private int lostMatches;
    private int noResultMatches;
    private int points;

    public SeasonTeamPointsDTO(String name){
        this.teamName = name;

    }


    public void incrementMatches(){
        totalMatchesPlayed+=1;
    }


    public int getLostMatches(){
        return totalMatchesPlayed-wonMatches-noResultMatches;
    }


    public void addPoints(int points){
        if(points == 2){
            wonMatches+=1;
        }else if(points == 1){
            noResultMatches+=1;
        }
        this.points+=points;
    }

}
