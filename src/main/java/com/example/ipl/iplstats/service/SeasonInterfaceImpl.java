package com.example.ipl.iplstats.service;

import com.example.ipl.iplstats.dao.MatchDAO;
import com.example.ipl.iplstats.dao.SeasonDAO;
import com.example.ipl.iplstats.dao.TeamDAO;
import com.example.ipl.iplstats.data.MatchSummaryDTO;
import com.example.ipl.iplstats.data.SeasonDTO;
import com.example.ipl.iplstats.data.TeamDTO;
import com.example.ipl.iplstats.entity.MatchSummary;
import com.example.ipl.iplstats.entity.Season;
import com.example.ipl.iplstats.entity.SeasonTeam;
import com.example.ipl.iplstats.exception.IPLStatException;
import com.example.ipl.iplstats.mapper.SeasonMapper;
import com.example.ipl.iplstats.utility.SeasonLoader;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Component
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

    @Override
    @Transactional
    public SeasonDTO addSeason(SeasonDTO season) throws IPLStatException {
        System.out.println("Season Details:"+season);
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

//            System.out.println("Season Size:"+ seasonList.size());
//            return seasonList;

            seasonPage.forEach(season -> {
                SeasonDTO seasonDTO = mapper.domainToDTO(season);
                Set<SeasonTeam> teams = teamDAO.findBySeason(season);
                Set<MatchSummary> matches = matchDAO.findBySeason(season);
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
    public void loadSeasons(File file) throws IPLStatException {

       List<SeasonDTO> seasons = loader.parseMatchesFile(file);


        for (Iterator<SeasonDTO> iterator = seasons.iterator(); iterator.hasNext(); ) {
            SeasonDTO next =  iterator.next();
            SeasonDTO seasonDTO= addSeason(next);

            Set<TeamDTO> teams = next.getTeams();
            teams.forEach(teamDTO -> {
                SeasonTeam team = mapper.teamDTOToSeasonTeam(teamDTO);
                team.setSeason(mapper.dtoToDomain(seasonDTO));
                teamDAO.save(team);
            });

            Set<MatchSummaryDTO> matches = next.getMatches();
            matches.forEach(matchSummaryDTO -> {
                MatchSummary matchSummary = mapper.matchSummaryDTOToMatch(matchSummaryDTO);
                matchSummary.setSeason(mapper.dtoToDomain(seasonDTO));
                matchDAO.save(matchSummary);
            });

        }
    }
}
