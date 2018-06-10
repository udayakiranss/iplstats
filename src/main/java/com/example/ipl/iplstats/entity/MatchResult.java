package com.example.ipl.iplstats.entity;

public enum MatchResult {


    // This will call enum constructor with one
    // String argument
    NORMAL("normal"), TIE("tie"), NO_RESULT("no result");

    // declaring private variable for getting values
    private String result;

    // getter method
    public String getResult()
    {
        return this.result;
    }

    // enum constructor - cannot be public or protected
     MatchResult(String result)
    {
        this.result = result;
    }
}