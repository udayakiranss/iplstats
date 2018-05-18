package com.example.ipl.iplstats.data;

import lombok.Data;

@Data
public class TeamDTO {

    private Long teamID;
    private String name;
    private String description;
    private String baseLocation;
}
