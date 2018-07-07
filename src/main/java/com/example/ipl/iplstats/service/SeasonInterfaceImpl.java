package com.example.ipl.iplstats.service;

import com.example.ipl.iplstats.dao.*;
import com.example.ipl.iplstats.data.*;
import com.example.ipl.iplstats.entity.*;
import com.example.ipl.iplstats.exception.IPLStatException;
import com.example.ipl.iplstats.loader.IPLDataLoader;
import com.example.ipl.iplstats.mapper.SeasonMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
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
    private PlayerDAO playerDAO;
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

    @Override
    public SeasonDTO getSeason(int year) throws IPLStatException {
        SeasonDTO seasonDTO = null;
        if(year >= 2008 || year <2018){

            Season season=seasonRepo.findByYear(String.valueOf(year));
            seasonDTO=mapper.domainToDTO(season);

        }else{
            throw new IPLStatException("IPL1","Not a valid year for IPL");
        }

        return seasonDTO;
    }


    @Transactional
    public void loadMatches(String file)throws IPLStatException{

        dataLoader.parseMatches(file);

        Iterable<Team> teams = dataLoader.getTeams();
        teamDAO.save(teams);

        Iterable<Season> seasons = dataLoader.getSeasons();
        seasonRepo.save(seasons);

        Iterable<MatchSummary> summaries = dataLoader.getSummaryList();
        matchDAO.save(summaries);



    }

    @Transactional
    public void loadDeliveryDetails(String file) throws IPLStatException{

        dataLoader.parseDeliveriesFile(file);

        Iterable<Player> players = dataLoader.getPlayers();
        playerDAO.save(players);

        Iterable<MatchDetails> detailsList = dataLoader.getDetailsList();
        matchDetailsDAO.save(detailsList);

    }

    public SeasonStatisticsDTO fetchPointsTable(SeasonDTO seasonDTO)throws IPLStatException{
        SeasonStatisticsDTO pointsDTO = new SeasonStatisticsDTO();


        int year = seasonDTO.getYear();
        pointsDTO.setSeason(year);

        Season season = seasonRepo.findByYear(String.valueOf(year));
        if(season == null){
            throw new IPLStatException("IPL2","Not a valid season");
        }
        Set<MatchSummary> summaries = matchDAO.findBySeason(season);

        if(summaries!=null && summaries.size() > 0){
            int matchCounter=0;
            for(MatchSummary matchSummary:summaries){
                matchCounter++;
                if(matchCounter == summaries.size()){
                    pointsDTO.setWinner(matchSummary.getWinner().getName());
                    pointsDTO.setPlayerOfMatch(matchSummary.getPlayerOfMatch());
                    if(matchSummary.getTeamB().getName().equals(matchSummary.getWinner().getName())){
                        pointsDTO.setLoser(matchSummary.getTeamA().getName());
                    }else{
                        pointsDTO.setLoser(matchSummary.getTeamB().getName());
                    }

                }
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
                            boolean success = next.incrementMatches();
                            if(success){
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
                }
            }
//            summaries.forEach(matchSummary -> {
//
//
//            });
            pointsDTO.setTeams(teamPoints);
        }else{
            throw new IPLStatException("IPL2","Not a valid season");
        }
        return pointsDTO;
    }




}
