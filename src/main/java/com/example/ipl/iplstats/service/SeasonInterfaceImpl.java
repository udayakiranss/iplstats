package com.example.ipl.iplstats.service;

import com.example.ipl.iplstats.dao.SeasonDAO;
import com.example.ipl.iplstats.data.SeasonDTO;
import com.example.ipl.iplstats.data.TeamDTO;
import com.example.ipl.iplstats.entity.Season;
import com.example.ipl.iplstats.entity.SeasonTeam;
import com.example.ipl.iplstats.exception.IPLStatException;
import com.example.ipl.iplstats.mapper.SeasonMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class SeasonInterfaceImpl implements SeasonInterface {

    List<SeasonDTO> seasonList = new ArrayList<SeasonDTO>();

    private  final SeasonMapper mapper = Mappers.getMapper(SeasonMapper.class);
    @Autowired
    private SeasonDAO seasonRepo;

    @Override
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
                Set<SeasonTeam> teams = season.getTeams();
                SeasonDTO seasonDTO = mapper.domainToDTO(season);

                teams.forEach(team-> {
                    TeamDTO teamDTO = mapper.seasonTeamToTeamDTO(team);
                    seasonDTO.addTeamDTO(teamDTO);
                });

                seasonDTOList.add(seasonDTO);
            } );
        }
        return  seasonDTOList;
    }
}
