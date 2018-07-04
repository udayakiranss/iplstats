package com.example.ipl.iplstats.service;

import com.example.ipl.iplstats.dao.PlayerDAO;
import com.example.ipl.iplstats.dao.SeasonDAO;
import com.example.ipl.iplstats.dao.TeamDAO;
import com.example.ipl.iplstats.data.PlayerDTO;
import com.example.ipl.iplstats.data.SeasonDTO;
import com.example.ipl.iplstats.entity.Player;
import com.example.ipl.iplstats.entity.Season;
import com.example.ipl.iplstats.entity.Team;
import com.example.ipl.iplstats.exception.IPLStatException;
import com.example.ipl.iplstats.mapper.PlayerMapper;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

@Slf4j
@Component
public class PlayerInterfaceImpl implements PlayerInterface {

    private  final PlayerMapper mapper = Mappers.getMapper(PlayerMapper.class);

    @Autowired
    private PlayerDAO playerDAO;
    @Autowired
    private SeasonDAO seasonDAO;
    @Autowired
    private TeamDAO teamDAO;

    @Override
    public PlayerDTO getPlayerInfo(String playerName, int year) throws IPLStatException {

        Season season = seasonDAO.findByYear(String.valueOf(year));
        PlayerDTO playerDTO=null;
        Optional<Player> player = playerDAO.findByNameAndSeason(playerName,season);

        if(player.isPresent()){
            log.debug(player.toString());
            playerDTO= mapper.playerToPlayerDTO(player.get());
        }else{
            throw new IPLStatException("IPL100","Player Not found");
        }
        return playerDTO;
    }

    @Override
    public List<PlayerDTO> getPlayerInfo(String playerName) throws IPLStatException {
        List<PlayerDTO> playerDTOList=null;
        List<Player> playerList = playerDAO.findByName(playerName);

        if(playerList!=null){
            playerDTOList = new ArrayList<>();
            ListIterator<Player> iter = playerList.listIterator();
            while (iter.hasNext()){
                Player player = (Player) iter.next();
                PlayerDTO playerDTO = mapper.playerToPlayerDTO(player);
                playerDTOList.add(playerDTO);
            }

        }else{
            throw new IPLStatException("IPL100","Player Not found");
        }
        return playerDTOList;
    }

    @Override
    public List<PlayerDTO> getPlayerList(String teamName, int year) throws IPLStatException {
        Season season = seasonDAO.findByYear(String.valueOf(year));
        Team team  = teamDAO.findBySeasonAndName(season,teamName);
        List<PlayerDTO> playerDTOList=null;
        List<Player> playerList = playerDAO.findBySeasonAndTeam(season,team);

        if(playerList!=null){
            playerDTOList = new ArrayList<>();
            ListIterator<Player> iter = playerList.listIterator();
            while (iter.hasNext()){
                Player player = (Player) iter.next();
                PlayerDTO playerDTO = mapper.playerToPlayerDTO(player);
                playerDTOList.add(playerDTO);
            }

        }else{
            throw new IPLStatException("IPL100","Player Not found");
        }
        return playerDTOList;
    }

    @Override
    public boolean isPlayerValid(String name, String team, SeasonDTO seasonDTO) throws IPLStatException {
        return false;
    }


}
