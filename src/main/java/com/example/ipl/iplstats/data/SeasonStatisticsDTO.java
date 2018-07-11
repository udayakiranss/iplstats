package com.example.ipl.iplstats.data;

import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@Data
@Slf4j
@ToString
public class SeasonStatisticsDTO {

    private int season;

    private Set<SeasonTeamPointsDTO> teams = new HashSet<SeasonTeamPointsDTO>();

    private String winner;

    private String playerOfMatch;

    private String loser;

    private int runs;

    private  int wickets;

    private PlayerInningsDTO manOfTheMatch;

}
