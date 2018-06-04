package com.example.ipl.iplstats.utility;

import com.example.ipl.iplstats.data.MatchSummaryDTO;
import com.example.ipl.iplstats.data.MatchesDTO;
import com.example.ipl.iplstats.data.SeasonDTO;
import com.example.ipl.iplstats.data.TeamDTO;
import com.example.ipl.iplstats.exception.IPLStatException;
import com.example.ipl.iplstats.service.SeasonInterface;
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

    private Set<MatchesDTO> matches=new HashSet<MatchesDTO>();;

    private List<TeamDTO> teams = new ArrayList<TeamDTO>();

    private List<SeasonDTO> seasons = new ArrayList<SeasonDTO>();

    private List<MatchSummaryDTO> summaryDTOS = new ArrayList<MatchSummaryDTO>();

    public SeasonLoader(){
//        File matchFile = new File(SeasonLoader.class.getClassLoader().getResource("matches.csv").getFile());
//
//        try {
//            parseMatchesFile(matchFile);
//        } catch (IPLStatException e) {
//            e.printStackTrace();
//        }

    }

    public void parseMatchesFile(File csvFile)throws  IPLStatException{
        try {

//            CsvParser
//                    .forEach(csvFile, row -> System.out.println(row));

//             CsvParser
//                     .mapWith(CsvMapperFactory
//                     .newInstance()
//                     .newBuilder(MatchesDTO.class)
//                     .addMapping("id")
//                     .addMapping("season")
//                     .mapper()).forEach(csvFile, matchesDTO -> matches.add(matchesDTO));

            CsvParser
                    .mapWith(CsvMapperFactory
                            .newInstance()
                            .defaultDateFormat("yyyy-MM-dd")
                            .newMapper(MatchesDTO.class))
                    .forEach(csvFile,matchesDTO -> matches.add(matchesDTO));

            log.debug("matches = " + matches.size());

            loadSeasons();



            for (Iterator<SeasonDTO> iterator = seasons.iterator(); iterator.hasNext(); ) {
                SeasonDTO next =  iterator.next();
                seasonInterface.addSeason(next);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadSeasons()throws IPLStatException{
        matches.forEach(match-> {
            try{
                createSeason(match);
            }catch (IPLStatException ex){
                ex.printStackTrace();
            }

        });


        seasons.forEach(System.out::println);


    }


    private void createSeason(MatchesDTO match)throws IPLStatException {

        SeasonDTO seasonDTO = new SeasonDTO(match.getSeason());

        int seasonIndex = seasons.indexOf(seasonDTO);

        if(seasonIndex !=-1){
            seasonDTO = seasons.get(seasonIndex);
        }else{
            seasons.add(seasonDTO);
        }
        seasonDTO.addTeamDTO(getTeam(match.getTeam1()));
        seasonDTO.addTeamDTO(getTeam(match.getTeam2()));

        seasonDTO.addMatchDTO(getMatchSummary(match));

//        return seasonInterface.addSeason(seasonDTO);

    }


    private TeamDTO getTeam(String name){
        TeamDTO team = new TeamDTO(name);
        int teamIndex = teams.indexOf(team);

        if(teamIndex!=-1){
            return  teams.get(teamIndex);
        }else{
            teams.add(team);
        }
        return team;

    }

    private MatchSummaryDTO getMatchSummary(MatchesDTO match){

        TeamDTO teamA = getTeam(match.getTeam1());
        TeamDTO teamB = getTeam(match.getTeam2());
        TeamDTO tossWinner = getTeam(match.getToss_winner());
        TeamDTO winner = getTeam(match.getWinner());

        MatchSummaryDTO matSummary = new MatchSummaryDTO(match.getCity(),match.getVenue(),teamA,teamB,tossWinner,
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

    public static void main(String[] args) {
        File matches = new File(SeasonLoader.class.getClassLoader().getResource("matches.csv").getFile());

        SeasonLoader delegate = new SeasonLoader();
        try {
            delegate.parseMatchesFile(matches);
        } catch (IPLStatException e) {
            e.printStackTrace();
        }

    }



}
