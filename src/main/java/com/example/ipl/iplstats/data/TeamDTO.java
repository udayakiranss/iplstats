package com.example.ipl.iplstats.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(of = "name")
@NoArgsConstructor
@ApiModel(value = "TeamDTO" , description = "IPL Team Details Object")
public class TeamDTO {

    private Long teamID;
    @ApiModelProperty(value = "Year" , position = 1 , required = true)
    private String name;
    private Set<PlayerDTO> playerDTOList;


    public TeamDTO(String name){
        this.name = name;
    }


    public void addPlayer(PlayerDTO playerDTO) {
        if(playerDTOList == null){
            playerDTOList = new HashSet<>();
        }
        playerDTOList.add(playerDTO);
    }
}
