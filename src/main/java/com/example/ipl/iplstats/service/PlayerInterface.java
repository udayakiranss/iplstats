package com.example.ipl.iplstats.service;

import com.example.ipl.iplstats.data.PlayerDTO;
import com.example.ipl.iplstats.data.SeasonDTO;
import com.example.ipl.iplstats.exception.IPLStatException;

import java.util.List;

public interface PlayerInterface {

    PlayerDTO getPlayerInfo(String playerName, int season) throws IPLStatException;

    List<PlayerDTO> getPlayerInfo(String name) throws IPLStatException;

    List<PlayerDTO> getPlayerList(String teamName,int season) throws IPLStatException;


    boolean isPlayerValid(String name, String team, SeasonDTO seasonDTO) throws IPLStatException;
}
