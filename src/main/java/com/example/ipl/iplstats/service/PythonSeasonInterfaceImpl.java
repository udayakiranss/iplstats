package com.example.ipl.iplstats.service;

import com.example.ipl.iplstats.config.ApplicationConfig;
import com.example.ipl.iplstats.data.PlayerDTO;
import com.example.ipl.iplstats.data.SeasonDTO;
import com.example.ipl.iplstats.data.SeasonStatisticsDTO;
import com.example.ipl.iplstats.exception.IPLStatException;
import com.example.ipl.iplstats.util.RestIntegration;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kiranuda on 8/10/2018.
 */
@Slf4j
@Component
public class PythonSeasonInterfaceImpl implements SeasonInterface {
    @Autowired
    private ApplicationConfig applicationConfig;

    @Override
    public SeasonDTO addSeason(SeasonDTO season) throws IPLStatException {
        log.debug("Adding  a season not supported");
        return null;
    }

    @Override
    public List<SeasonDTO> getSeasons() throws IPLStatException {
        List<SeasonDTO> seasonDTOList = new ArrayList<SeasonDTO>();
        for(int i=2009; i<=2018;i++){
            SeasonDTO seasonDTO = new SeasonDTO();
            seasonDTO.setYear(i);
            seasonDTOList.add(seasonDTO);
        }

        return seasonDTOList;
    }

    @Override
    public SeasonDTO getSeason(int year) throws IPLStatException {

        String url = String.format(applicationConfig.getPythonStatURL()+"/iplstats/season/%s",year);
        RestTemplate template = new RestTemplate();
        SeasonDTO seasonDTO = null;
        try {
            ResponseEntity<SeasonStatisticsDTO> statisticsDTO = template.getForEntity(url,SeasonStatisticsDTO.class);
            SeasonStatisticsDTO stats = statisticsDTO.getBody();
            seasonDTO = new SeasonDTO();
            seasonDTO.setYear(year);
            return seasonDTO;
        } catch (RestClientException e) {
           throw new IPLStatException("123", e);
        }
    }

    @Override
    public void loadDeliveryDetails(String file, InputStream filePath) throws IPLStatException {

    }



    @Override
    public void loadMatches(String file) throws IPLStatException {

    }

    @Override
    public SeasonStatisticsDTO fetchPointsTable(SeasonDTO seasonDTO) throws IPLStatException {
        String url = String.format(applicationConfig.getPythonStatURL()+"/iplstats/season/%s",
                seasonDTO.getYear());
        RestTemplate template = new RestTemplate();
        try {
            ResponseEntity<SeasonStatisticsDTO> statisticsDTO = template.getForEntity(url,SeasonStatisticsDTO.class);



            return  statisticsDTO.getBody();
        } catch (RestClientException e) {
            throw new IPLStatException("123", e);
        }
    }

    @Override
    public PlayerDTO orangeCapPlayer(SeasonDTO seasonDTO)throws IPLStatException {
        String url = String.format(applicationConfig.getPythonStatURL()+"/iplstats/season/%s/orangecap",
                seasonDTO.getYear());
        RestTemplate template = new RestTemplate();
        try {
            ResponseEntity<PlayerDTO> player = template.getForEntity(url,PlayerDTO.class);
            return  player.getBody();
        } catch (RestClientException e) {
            throw new IPLStatException("123", e);
        }
    }

    @Override
    public PlayerDTO purpleCapPlayer(SeasonDTO seasonDTO)throws IPLStatException {
        String url = String.format(applicationConfig.getPythonStatURL()+"/iplstats/season/%s/purplecap",
                seasonDTO.getYear());
        RestTemplate template = new RestTemplate();
        try {
            ResponseEntity<PlayerDTO> player = template.getForEntity(url,PlayerDTO.class);
            return  player.getBody();
        } catch (RestClientException e) {
            throw new IPLStatException("123", e);
        }
    }
}
