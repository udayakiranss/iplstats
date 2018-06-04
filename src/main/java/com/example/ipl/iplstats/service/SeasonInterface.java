package com.example.ipl.iplstats.service;

import com.example.ipl.iplstats.data.SeasonDTO;
import com.example.ipl.iplstats.entity.Season;
import com.example.ipl.iplstats.exception.IPLStatException;

import java.util.List;

public interface SeasonInterface {

    SeasonDTO addSeason(SeasonDTO season) throws IPLStatException;

    List<SeasonDTO> getSeasons() throws IPLStatException;


}
