package com.example.ipl.iplstats.service;

import com.example.ipl.iplstats.dao.*;
import com.example.ipl.iplstats.data.*;
import com.example.ipl.iplstats.entity.*;
import com.example.ipl.iplstats.exception.IPLStatException;
import com.example.ipl.iplstats.loader.IPLDataLoader;
import com.example.ipl.iplstats.mapper.PlayerInningsMapper;
import com.example.ipl.iplstats.mapper.SeasonMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.io.File;
import java.io.InputStream;
import java.util.*;

@Slf4j
public class SeasonInterfaceImpl implements SeasonInterface {

    List<SeasonDTO> seasonList = new ArrayList<SeasonDTO>();

    private  final SeasonMapper mapper = Mappers.getMapper(SeasonMapper.class);
    private  final PlayerInningsMapper inningsMapper = Mappers.getMapper(PlayerInningsMapper.class);
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
    private PlayerInningsDAO playerInningsDAO;
    @Autowired
    private IPLDataLoader dataLoader;
//    @Getter
//    private Set<SeasonTeamPointsDTO> teamPoints1 = new HashSet<SeasonTeamPointsDTO>();


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
    public void loadDeliveryDetails(String file, InputStream filePath) throws IPLStatException{

//        dataLoader.parseDeliveriesFile(file);

        dataLoader.processInputFile(filePath);

        Iterable<Player> players = dataLoader.getPlayers();
        playerDAO.save(players);

//        Iterable<PlayerInnings> detailsList = dataLoader.getPlayerInningsSet();
        Map<MatchSummary, Set<PlayerInnings>> map = dataLoader.getSummaryPlayerMap();
        map.forEach((summary, playerInnings) -> {
            playerInningsDAO.save(playerInnings);
        });



    }

    public SeasonStatisticsDTO fetchPointsTable(SeasonDTO seasonDTO)throws IPLStatException{
        SeasonStatisticsDTO pointsDTO = new SeasonStatisticsDTO();
        Set<SeasonTeamPointsDTO> teamPoints = new HashSet<SeasonTeamPointsDTO>();

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
                    pointsDTO.setManOfTheMatch(getManOfTheMatchDetails(matchSummary.getPlayerOfMatch(),
                            season,matchSummary));
                    if(matchSummary.getTeamB().getName().equals(matchSummary.getWinner().getName())){
                        pointsDTO.setLoser(matchSummary.getTeamA().getName());
                    }else{
                        pointsDTO.setLoser(matchSummary.getTeamB().getName());
                    }
                    pointsDTO.setRuns(matchSummary.getWinByRuns());
                    pointsDTO.setWickets(matchSummary.getWinByWickets());
//                    matchSummary.getI

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
                            boolean leagueMatch = next.incrementMatches();
                            if(leagueMatch){
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

    @Override
    public PlayerDTO orangeCapPlayer(SeasonDTO seasonDTO) {
        List<Object[]> playerDetails = playerDAO.findByMaxRuns(seasonDTO.getId());
        PlayerDTO playerDTO = new PlayerDTO();

        if(playerDetails!=null && playerDetails.size() == 1){
            Object[] object  = playerDetails.get(0);
            String name = (String)object[0];
            int runs = (Integer)object[1];
            playerDTO.setName(name);
            playerDTO.setTotalRuns(runs);
        }
        return playerDTO;

    }

    @Override
    public PlayerDTO purpleCapPlayer(SeasonDTO seasonDTO) {
        List<Object[]> playerDetails = playerDAO.findByMaxWickets(seasonDTO.getId());
        PlayerDTO playerDTO = new PlayerDTO();

        if(playerDetails!=null && playerDetails.size() == 1){
            Object[] object  = playerDetails.get(0);
            String name = (String)object[0];
            int wickets = (Integer)object[1];
            playerDTO.setName(name);
            playerDTO.setTotalWickets(wickets);
        }
        return playerDTO;
    }


    private PlayerInningsDTO getManOfTheMatchDetails(String playerName,Season season,MatchSummary summary) {

        Optional<Player> player = playerDAO.findByNameAndSeason(playerName,season);
        PlayerInningsDTO manOfTheMatchDetails = null;
        if(player.isPresent()){
            PlayerInnings playerInnings = playerInningsDAO.findByPlayerAndSummary(player.get(),summary);
            manOfTheMatchDetails=inningsMapper.playerToPlayerDTO(playerInnings);
        }

        return manOfTheMatchDetails;
    }
}
