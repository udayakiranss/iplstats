package com.example.ipl.iplstats.data;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@ToString(of = "name")
@EqualsAndHashCode(of = "name")
@NoArgsConstructor
public class TeamDTO {

    private Long teamID;
    private String name;
    private String description;
    private String baseLocation;
    private int totalMatches;
    private int winCount;
    private int lostCount;
    private int points;
    private SeasonDTO seasonDTO;

    private Set<PlayerDTO> players;

    public TeamDTO(String name){
        this.name = name;
    }


}
