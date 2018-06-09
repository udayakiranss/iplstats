package com.example.ipl.iplstats.service;

import com.example.ipl.iplstats.dao.MatchDAO;
import com.example.ipl.iplstats.dao.MatchDetailsDAO;
import com.example.ipl.iplstats.dao.SeasonDAO;
import com.example.ipl.iplstats.dao.TeamDAO;
import com.example.ipl.iplstats.data.*;
import com.example.ipl.iplstats.entity.*;
import com.example.ipl.iplstats.exception.IPLStatException;
import com.example.ipl.iplstats.loader.IPLDataLoader;
import com.example.ipl.iplstats.mapper.SeasonMapper;
import com.example.ipl.iplstats.utility.SeasonLoader;
import lombok.Getter;
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
    private MatchDetailsDAO matchDetailsDAO;
    @Autowired
    private IPLDataLoader dataLoader;
    @Getter
    private Set<SeasonTeamPointsDTO> teamPoints = new HashSet<SeasonTeamPointsDTO>();


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

    @Transactional
    public void loadDeliveryDetails(File file) throws IPLStatException{

        dataLoader.parseDeliveriesFile(file);

        Iterable<MatchDetails> detailsList = dataLoader.getDetailsList();
        matchDetailsDAO.save(detailsList);

    }

    public SeasonPointsDTO fetchPointsTable(SeasonDTO seasonDTO)throws IPLStatException{
        SeasonPointsDTO pointsDTO = new SeasonPointsDTO();


        String year = String.valueOf(seasonDTO.getYear());
        pointsDTO.setSeason(year);

        Season season = seasonRepo.findByYear(year);
        if(season == null){
            throw new IPLStatException("IPL2","Not a valid season");
        }
        Set<MatchSummary> summaries = matchDAO.findBySeason(season);

        if(summaries!=null && summaries.size() > 0){
            summaries.forEach(matchSummary -> {

                SeasonTeamPointsDTO teamAPointsDTO = new SeasonTeamPointsDTO(matchSummary.getTeamA().getName());
                teamPoints.add(teamAPointsDTO);
                SeasonTeamPointsDTO teamBPointsDTO = new SeasonTeamPointsDTO(matchSummary.getTeamB().getName());
                teamPoints.add(teamBPointsDTO);
                String result = matchSummary.getResult();
                Iterator<SeasonTeamPointsDTO> teamPointsDTOIterator = teamPoints.iterator();

                if(result!=null){
                    while(teamPointsDTOIterator.hasNext()){
                        SeasonTeamPointsDTO next = teamPointsDTOIterator.next();
                        if(result!=null && next.getTeamName().equals(matchSummary.getTeamA().getName())
                                || next.getTeamName().equals(matchSummary.getTeamB().getName())){
                            next.incrementMatches();
                            if((result.equals(MatchResult.NORMAL.getResult())
                                    ||result.equals(MatchResult.TIE.getResult())) &&
                                    next.getTeamName().equals(matchSummary.getWinner().getName())) {
                                next.addPoints(2);
                            }else if(result.equals(MatchResult.NO_RESULT.getResult())) {
                                next.addPoints(1);
                            }

                        }

                    }
                }

            });
            pointsDTO.setTeams(teamPoints);
        }else{
            throw new IPLStatException("IPL2","Not a valid season");
        }
        return pointsDTO;
    }




}
