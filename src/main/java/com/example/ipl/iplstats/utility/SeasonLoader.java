package com.example.ipl.iplstats.utility;

import com.example.ipl.iplstats.data.*;
import com.example.ipl.iplstats.exception.IPLStatException;
import com.example.ipl.iplstats.service.SeasonInterface;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.simpleflatmapper.csv.CsvMapperFactory;
import org.simpleflatmapper.csv.CsvParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Component
@Slf4j
public class SeasonLoader {
    @Autowired
    private SeasonInterface seasonInterface;

    private Set<MatchesDTO> matches=new HashSet<MatchesDTO>();

private Set<DeliveryDetailsDTO> deliveryInfoList=new HashSet<DeliveryDetailsDTO>();
    @Getter
    private Set<TeamDTO> teams = new HashSet<TeamDTO>();
    private List<SeasonDTO> seasons = new ArrayList<SeasonDTO>();
    private List<MatchSummaryDTO> summaryDTOS = new ArrayList<MatchSummaryDTO>();


    public List<SeasonDTO> parseMatchesFile(File csvFile)throws  IPLStatException{
        try {
            CsvParser
                    .mapWith(CsvMapperFactory
                            .newInstance()
                            .defaultDateFormat("yyyy-MM-dd")
                            .newMapper(MatchesDTO.class))
                    .forEach(csvFile,matchesDTO -> matches.add(matchesDTO));

//            log.debug("matches = " + matches.size());

            loadSeasons();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  seasons;
    }

    private void loadSeasons()throws IPLStatException{
        matches.forEach(match-> {
            try{
                createSeason(match);
            }catch (IPLStatException ex){
                ex.printStackTrace();
            }

        });
//        seasons.forEach(System.out::println);
    }


    private void createSeason(MatchesDTO match)throws IPLStatException {

//        SeasonDTO seasonDTO = new SeasonDTO(match.getSeason());
////        int seasonIndex = seasons.indexOf(seasonDTO);
//        if(!seasons.contains(seasonDTO)){
//            seasons.add(seasonDTO);
//        }

        SeasonDTO seasonDTO =null;
        boolean found = false;
        Iterator<SeasonDTO> seasonDTOIterator = seasons.iterator();
        while(seasonDTOIterator.hasNext()){
            seasonDTO = seasonDTOIterator.next();
            if(String.valueOf(seasonDTO.getYear()).equals(match.getSeason())){
                found = true;
                break;
            }
        }

        if(!found){
            seasonDTO = new SeasonDTO(match.getSeason());
            seasons.add(seasonDTO);
        }
        seasonDTO.addTeamDTO(getTeam(seasonDTO,match.getTeam1()));
        seasonDTO.addTeamDTO(getTeam(seasonDTO,match.getTeam2()));
        seasonDTO.addMatchDTO(getMatchSummary(seasonDTO,match));
    }


    private TeamDTO getTeam(SeasonDTO seasonDTO, String name){
        TeamDTO team =null;
        boolean found = false;
        Iterator<TeamDTO> teamIter = teams.iterator();
        while(teamIter.hasNext()){
            team = teamIter.next();
            if(team.getName().equals(name)){
                found= true;
                break;
            }
        }
        if(!found){
            team = new TeamDTO(name);
            teams.add(team);
        }
        return team;

    }

    private MatchSummaryDTO getMatchSummary(SeasonDTO seasonDTO,MatchesDTO match){

        TeamDTO teamA = getTeam(seasonDTO,match.getTeam1());
        TeamDTO teamB = getTeam(seasonDTO,match.getTeam2());
        TeamDTO tossWinner = getTeam(seasonDTO,match.getToss_winner());
        TeamDTO winner = getTeam(seasonDTO,match.getWinner());

        MatchSummaryDTO matSummary = new MatchSummaryDTO(match.getId(),match.getCity(),match.getVenue(),teamA,teamB,tossWinner,
                match.getToss_decision(),winner,match.getWin_by_runs(),match.getWin_by_wickets());
        matSummary.setDate(match.getDate());
        int summaryIndex = summaryDTOS.indexOf(matSummary);

        if(summaryIndex!=-1){
            return  summaryDTOS.get(summaryIndex);
        }else{
            summaryDTOS.add(matSummary);
        }
        return matSummary;

    }

    public Set<DeliveryDetailsDTO> parseDeliveriesFile(File csvFile)throws  IPLStatException{
        try {
            CsvParser
                    .mapWith(CsvMapperFactory
                            .newInstance()
                            .defaultDateFormat("yyyy-MM-dd")
                            .newMapper(DeliveryDetailsDTO.class))
                    .forEach(csvFile,matchDetailsDTO -> deliveryInfoList.add(matchDetailsDTO));

            log.debug("matches = " + deliveryInfoList.size());


        } catch (IOException e) {
            e.printStackTrace();
        }
        return deliveryInfoList;
    }

    public static void main(String[] args) {
        SeasonLoader delegate = new SeasonLoader();
        File matches = new File(SeasonLoader.class.getClassLoader().getResource("matches.csv").getFile());
        try {
            List<SeasonDTO> seasonDTOList = delegate.parseMatchesFile(matches);
            seasonDTOList.forEach(seasonDTO-> {

                System.out.println(seasonDTO);
            });
        } catch (IPLStatException e) {
            e.printStackTrace();
        }

    }



}
