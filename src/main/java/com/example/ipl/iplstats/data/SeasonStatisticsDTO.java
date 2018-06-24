package com.example.ipl.iplstats.data;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of ="year")
@NoArgsConstructor
@AllArgsConstructor
public class SeasonStatisticsDTO {

    private int year;
    private String winner;
    private SeasonPointsDTO pointsDTO;

}
