package com.example.ipl.iplstats.data;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@Data
@Slf4j
public class SeasonPointsDTO {

    private String season;

    private Set<SeasonTeamPointsDTO> teams = new HashSet<SeasonTeamPointsDTO>();

}
