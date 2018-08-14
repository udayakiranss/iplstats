package com.example.ipl.iplstats.service;

import com.example.ipl.iplstats.data.PlayerDTO;
import com.example.ipl.iplstats.data.SeasonDTO;
import com.example.ipl.iplstats.exception.IPLStatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kiranuda on 8/13/2018.
 */
@Slf4j
@Component
public class PythonPlayerInterfaceImpl implements PlayerInterface {
    @Override
    public List<PlayerDTO> getPlayerInfo(String playerName, int season) throws IPLStatException {
        String url = String.format("https://flask-api-py.herokuapp.com/iplstats/season/%s/player/%s",season,playerName);
        RestTemplate template = new RestTemplate();
        try {
            ResponseEntity<PlayerDTO> player = template.getForEntity(url,PlayerDTO.class);
            List<PlayerDTO> players = new ArrayList<>();
            players.add(player.getBody());
            return players;
        } catch (RestClientException e) {
            throw new IPLStatException("123", e);
        }
    }

    @Override
    public List<PlayerDTO> getPlayerInfo(String name) throws IPLStatException {
        String url = String.format("https://flask-api-py.herokuapp.com/iplstats/player/%s",name);
        RestTemplate template = new RestTemplate();
        try {
            ResponseEntity<PlayerDTO> player = template.getForEntity(url,PlayerDTO.class);
            List<PlayerDTO> players = new ArrayList<>();
            players.add(player.getBody());
            return players;
        } catch (RestClientException e) {
            throw new IPLStatException("123", e);
        }
    }

    @Override
    public List<PlayerDTO> getPlayerList(String teamName, int season) throws IPLStatException {
        return null;
    }

    @Override
    public PlayerDTO getPlayerStatistics(String playerName, Long seasonId) throws IPLStatException {
        List<PlayerDTO> playerDTOs = getPlayerInfo(playerName,seasonId.intValue());
        if(playerDTOs.size() > 0){
            return playerDTOs.get(0);
        }else{
            return null;
        }
    }

    @Override
    public boolean isPlayerValid(String name, String team, SeasonDTO seasonDTO) throws IPLStatException {
        return false;
    }
}
