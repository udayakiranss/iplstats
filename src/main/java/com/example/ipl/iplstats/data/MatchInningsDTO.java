package com.example.ipl.iplstats.data;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.util.Set;
@Component
@Getter
@Setter
@ToString
public class MatchInningsDTO {

    private int innings;

    private Set<PlayerInningsDTO> playerInningsDTOS;

    private int totalScore;

    private int lostWickets;

    private int totalBalls;



}
