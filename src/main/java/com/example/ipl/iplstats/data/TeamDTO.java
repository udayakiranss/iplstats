package com.example.ipl.iplstats.data;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(of = "name")
@NoArgsConstructor
public class TeamDTO {

    private Long teamID;
    private String name;

//    private SeasonDTO seasonDTO;

    public TeamDTO(String name){
        this.name = name;
    }


}
