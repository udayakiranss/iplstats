package com.example.ipl.iplstats.entity;

import lombok.Getter;
import lombok.Setter;

public enum DismissalKind {

    BOWLED("bowled"),
    CAUGHT("caught"),
    RUN_OUT("run out"),
    CAUGHT_BOWLED("caught and bowled"),
    LBW("lbw"),
    STUMPED("stumped"),
    HIT_WICKET("hit wicket"),
    OBSTRUCTING_FIELDER("obstructing the field"),
    RETIRED_HURT("retired hurt");

    @Getter
    @Setter
    public String kind;


    public String getKind(){
        return this.kind;
    }

    DismissalKind(String kind){
        this.kind = kind;
    }



}
