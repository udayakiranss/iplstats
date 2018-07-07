package com.example.ipl.iplstats.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Created by kiranuda on 6/6/2018.
 */
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class DeliveryDetailsDTO {


    private String match_id;
    private String inning;
    private String batting_team;
    private String bowling_team;
    private String over;
    private String ball;
    private String batsman;
    private String non_striker;
    private String bowler;
    private String is_super_over;
    private String wide_runs;
    private String bye_runs;
    private String legbye_runs;
    private String noball_runs;
    private String penalty_runs;
    private String batsman_runs;
    private String extra_runs;
    private String total_runs;
    private String player_dismissed;
    private String dismissal_kind;
    private String fielder;

}   
