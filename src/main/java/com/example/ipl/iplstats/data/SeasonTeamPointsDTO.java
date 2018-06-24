package com.example.ipl.iplstats.data;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Data
@EqualsAndHashCode(of = "teamName")
@ToString(of={"teamaName,points"})
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


    public boolean incrementMatches(){
        boolean leagueMatch=false;
        if(totalMatchesPlayed !=14) {
            totalMatchesPlayed += 1;
            leagueMatch = true;
        }else{
            log.debug(teamName + " is playing a playoff match or final");
        }
        return leagueMatch;
    }


    public int getLostMatches(){
        return totalMatchesPlayed-wonMatches-noResultMatches;
    }


    public void addPoints(int points){
        if(totalMatchesPlayed != 8 && points == 2){
            wonMatches+=1;
        }else if(points == 1){
            noResultMatches+=1;
        }
        this.points+=points;
    }

}
