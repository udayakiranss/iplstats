package com.example.ipl.iplstats.service;

import com.example.ipl.iplstats.data.TeamDTO;
import com.example.ipl.iplstats.exception.IPLStatException;

import java.util.List;

public interface TeamInterface {

    List<TeamDTO> getTeams() throws IPLStatException;

    TeamDTO getTeam(String name) throws IPLStatException;

}
