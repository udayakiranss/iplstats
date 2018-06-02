package com.example.ipl.iplstats.data;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class PlayerDTO {

    private Long id;
    private String name;
    private int NoOfMatches;
    private int totalRuns;
    private int totalWickets;

}
