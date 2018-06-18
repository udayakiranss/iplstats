package com.example.ipl.iplstats.data;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of ="year")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "SeasonDTO" , description = "Season Details Object")
public class SeasonDTO {

    private Long id;
    @ApiModelProperty(value = "Year" , position = 1 , required = true)
    private int year;
    @ApiModelProperty(value = "Description" , position = 2 , required = false)
    private String description;
    private Set<TeamDTO> teams;
    private Set<MatchSummaryDTO> matches;

    public SeasonDTO(String year){
        this.year = new Integer(year).intValue();
    }

    public void addTeamDTO(TeamDTO team){
        if(teams==null){
            teams = new HashSet<TeamDTO>();
        }
        teams.add(team);

    }

    public void addMatchDTO(MatchSummaryDTO summaryDTO){
        if(matches==null){
            matches = new HashSet<MatchSummaryDTO>();
        }

        matches.add(summaryDTO);

    }

    public static SeasonDTO copy(SeasonDTO seasonDTO){

        SeasonDTO newSeasonDTO = new SeasonDTO();

        newSeasonDTO.setYear(seasonDTO.getYear());
        newSeasonDTO.setDescription(seasonDTO.getDescription());
        newSeasonDTO.setId(seasonDTO.getId());
        return  newSeasonDTO;
    }

    @Override
    public String toString() {
        return "\nSeasonDTO[" +
                "year=" + year +
                ", \nteams=" + teams +
                ", \nmatches=" + matches +
                '[';
    }
}
