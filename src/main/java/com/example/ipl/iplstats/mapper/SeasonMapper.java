package com.example.ipl.iplstats.mapper;

import com.example.ipl.iplstats.data.PlayerDTO;
import com.example.ipl.iplstats.data.SeasonDTO;
import com.example.ipl.iplstats.data.TeamDTO;
import com.example.ipl.iplstats.entity.Player;
import com.example.ipl.iplstats.entity.Season;
import com.example.ipl.iplstats.entity.SeasonTeam;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN, componentModel = "spring")
public interface SeasonMapper {

    Season dtoToDomain(final SeasonDTO seasonDTO);

    SeasonDTO domainToDTO(final  Season season);

    SeasonTeam teamDTOToSeasonTeam(TeamDTO team);

    TeamDTO seasonTeamToTeamDTO(SeasonTeam sTeam);

    PlayerDTO playerToPlayerDTO(Player player);

    Player playerDTOToPlayer(PlayerDTO playerDTO);
}
