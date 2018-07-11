package com.example.ipl.iplstats.data;

import com.example.ipl.iplstats.entity.Player;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class PlayerInningsDTO {

    private String name;

    private Long matchId;

    private int ballsFaced;

    private int boundaries;

    private int sixers;

    private int totalRuns;

    private int totalWickets;

    private int catches;

    private boolean notOut;

    private String dismissalType;


}
