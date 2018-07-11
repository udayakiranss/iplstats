package com.example.ipl.iplstats.mapper;

import com.example.ipl.iplstats.data.PlayerDTO;
import com.example.ipl.iplstats.data.PlayerInningsDTO;
import com.example.ipl.iplstats.entity.Player;
import com.example.ipl.iplstats.entity.PlayerInnings;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;


@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN, componentModel = "spring")
public interface PlayerInningsMapper {
    @Mappings({
            @Mapping(source = "playerInnings.player.name", target = "name"),
            @Mapping(source = "playerInnings.summary.matchID", target = "matchId")
    })
    PlayerInningsDTO playerToPlayerDTO(PlayerInnings playerInnings);

}