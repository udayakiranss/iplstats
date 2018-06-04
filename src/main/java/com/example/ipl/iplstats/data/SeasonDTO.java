package com.example.ipl.iplstats.data;


import lombok.*;

import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of ="year")
@NoArgsConstructor
@AllArgsConstructor
public class SeasonDTO {

    private Long id;
    private int year;
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

    @Override
    public String toString() {
        return "\nSeasonDTO[" +
                "year=" + year +
                ", \nteams=" + teams +
                ", \nmatches=" + matches +
                '[';
    }
}
