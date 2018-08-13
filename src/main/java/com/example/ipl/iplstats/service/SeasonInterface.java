package com.example.ipl.iplstats.service;

import com.example.ipl.iplstats.data.PlayerDTO;
import com.example.ipl.iplstats.data.SeasonDTO;
import com.example.ipl.iplstats.data.SeasonStatisticsDTO;
import com.example.ipl.iplstats.exception.IPLStatException;

import java.io.File;
import java.io.InputStream;
import java.util.List;

public interface SeasonInterface {

    SeasonDTO addSeason(SeasonDTO season) throws IPLStatException;

    List<SeasonDTO> getSeasons() throws IPLStatException;

    SeasonDTO getSeason(int year) throws IPLStatException;

    void loadDeliveryDetails(String file,InputStream filePath) throws  IPLStatException;

    void loadMatches(String file) throws  IPLStatException;

    SeasonStatisticsDTO fetchPointsTable(SeasonDTO seasonDTO) throws  IPLStatException;

    PlayerDTO orangeCapPlayer(SeasonDTO seasonDTO) throws IPLStatException;

    PlayerDTO purpleCapPlayer(SeasonDTO seasonDTO)throws  IPLStatException;


}
