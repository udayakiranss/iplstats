package com.example.ipl.iplstats.service;

import com.example.ipl.iplstats.dao.TeamDAO;
import com.example.ipl.iplstats.data.TeamDTO;
import com.example.ipl.iplstats.entity.Team;
import com.example.ipl.iplstats.exception.IPLStatException;
import com.example.ipl.iplstats.mapper.SeasonMapper;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
@Slf4j
public class TeamInterfaceImpl implements TeamInterface {
    @Autowired
    TeamDAO teamRepository;

    private  final SeasonMapper mapper = Mappers.getMapper(SeasonMapper.class);

    @Override
    public List<TeamDTO> getTeams() throws IPLStatException {

        List<TeamDTO> teamDTOList = null;
        List<Team> teamList = teamRepository.findAll();
        if(teamList!=null && teamList.size() > 0){
            teamDTOList = new ArrayList<TeamDTO>();
            Iterator<Team> teamIterator = teamList.listIterator();
            while (teamIterator.hasNext()){
                Team team = teamIterator.next();
                TeamDTO teamDTO = mapper.seasonTeamToTeamDTO(team);
                teamDTOList.add(teamDTO);
            }
        }
        return teamDTOList;
    }

    @Override
    public TeamDTO getTeam(String name) throws IPLStatException {
        TeamDTO teamDTO = null;
        if(name!=null && name.length() > 0){
            Team team = teamRepository.findByName(name);
            if(team !=null){
                teamDTO= mapper.seasonTeamToTeamDTO(team);
            }else{
                throw new IPLStatException("IPL2","Not a valid team");
            }
        }else{
            throw new IPLStatException("IPL2","Not a valid team");

        }

        return teamDTO;
    }
}
