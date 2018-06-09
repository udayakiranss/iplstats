package com.example.ipl.iplstats.service;

import com.example.ipl.iplstats.data.SeasonDTO;
import com.example.ipl.iplstats.data.SeasonPointsDTO;
import com.example.ipl.iplstats.entity.Season;
import com.example.ipl.iplstats.exception.IPLStatException;

import java.io.File;
import java.util.List;

public interface SeasonInterface {

    SeasonDTO addSeason(SeasonDTO season) throws IPLStatException;

    List<SeasonDTO> getSeasons() throws IPLStatException;

    void loadDeliveryDetails(File file) throws  IPLStatException;

    void loadMatches(File file) throws  IPLStatException;

    SeasonPointsDTO fetchPointsTable(SeasonDTO seasonDTO) throws  IPLStatException;


}
