package com.example.ipl.iplstats.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

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


    public TeamDTO(String name){
        this.name = name;
    }


}
