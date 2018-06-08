package com.example.ipl.iplstats.service;

import com.example.ipl.iplstats.dao.MatchDAO;
import com.example.ipl.iplstats.dao.SeasonDAO;
import com.example.ipl.iplstats.dao.TeamDAO;
import com.example.ipl.iplstats.data.MatchSummaryDTO;
import com.example.ipl.iplstats.data.SeasonDTO;
import com.example.ipl.iplstats.data.TeamDTO;
import com.example.ipl.iplstats.entity.*;
import com.example.ipl.iplstats.exception.IPLStatException;
import com.example.ipl.iplstats.loader.IPLDataLoader;
import com.example.ipl.iplstats.mapper.SeasonMapper;
import com.example.ipl.iplstats.utility.SeasonLoader;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.io.File;
import java.util.*;

@Component
@Slf4j
public class SeasonInterfaceImpl implements SeasonInterface {

    List<SeasonDTO> seasonList = new ArrayList<SeasonDTO>();

    private  final SeasonMapper mapper = Mappers.getMapper(SeasonMapper.class);
    @Autowired
    private SeasonDAO seasonRepo;
    @Autowired
    private TeamDAO teamDAO;
    @Autowired
    private MatchDAO matchDAO;
    @Autowired
    private SeasonLoader loader;
    @Autowired
    private IPLDataLoader dataLoader;



    @Override
     public SeasonDTO addSeason(SeasonDTO season) throws IPLStatException {
        System.out.println("Season Details:" + season);
        if(season!=null){
            Season seasonEntity = mapper.dtoToDomain(season);
            seasonRepo.save(seasonEntity);
            return mapper.domainToDTO(seasonEntity);
        }
        return null;
    }


    @Override
    public List<SeasonDTO> getSeasons() throws IPLStatException {
        Iterable<Season> seasonPage = seasonRepo.findAll();
        final List<SeasonDTO> seasonDTOList = new ArrayList<SeasonDTO>();
        if(seasonPage!=null){

            seasonPage.forEach(season -> {
                SeasonDTO seasonDTO = mapper.domainToDTO(season);
                Set<Team> teams = teamDAO.findBySeason(season);
                log.debug("No of teams in season "+ season.getYear() + " is "+teams.size());
                Set<MatchSummary> matches = matchDAO.findBySeason(season);
                log.debug("No of matches in season "+ season.getYear() + " is "+matches.size());
                teams.forEach(team-> {
                    TeamDTO teamDTO = mapper.seasonTeamToTeamDTO(team);
                    seasonDTO.addTeamDTO(teamDTO);
                });

                matches.forEach(match-> {
                    MatchSummaryDTO matchDTO = mapper.matchSummaryToMatchDTO(match);
                    seasonDTO.addMatchDTO(matchDTO);
                });

                seasonDTOList.add(seasonDTO);
            } );
        }
        return  seasonDTOList;
    }



    @Transactional
    public void loadMatches(File file)throws IPLStatException{

        dataLoader.parseMatches(file);

        Iterable<Team> teams = dataLoader.getTeams();
        teamDAO.save(teams);

        Iterable<Season> seasons = dataLoader.getSeasons();
        seasonRepo.save(seasons);

        Iterable<MatchSummary> summaries = dataLoader.getSummaryList();
        matchDAO.save(summaries);

    }


    public void loadDeliveryDetails(File file) throws IPLStatException{

        dataLoader.parseDeliveriesFile(file);

        List<MatchDetails> detailsList = dataLoader.getDetailsList();

        detailsList.forEach(matchDetails -> {
            MatchSummary summary = matchDAO.getOne(matchDetails.getMatchID());

        });

    }

}
