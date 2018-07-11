package com.example.ipl.iplstats.entity;

import javax.persistence.CascadeType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Set;

public class MatchInnings {
    @Id
    private Long id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="summary_id")
    private MatchSummary summary;

    private int innings;

    private int totalScore;

    private int lostWickets;

    private int totalBalls;
}
