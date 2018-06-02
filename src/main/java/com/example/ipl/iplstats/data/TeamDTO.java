package com.example.ipl.iplstats.data;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Set;

@Data
@ToString
@EqualsAndHashCode
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

    private Set<PlayerDTO> players;
}
