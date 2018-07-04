package com.example.ipl.iplstats.mapper;

import com.example.ipl.iplstats.data.PlayerDTO;
import com.example.ipl.iplstats.entity.Player;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN, componentModel = "spring")
public interface PlayerMapper {
    @Mappings({
            @Mapping(source = "player.name", target = "name"),
            @Mapping(source = "player.noOfMatches", target = "noOfMatches"),
            @Mapping(source = "player.totalRuns", target = "totalRuns"),
            @Mapping(source = "player.totalWickets", target = "totalWickets"),
            @Mapping(source = "player.season.year", target = "season"),
            @Mapping(source = "player.team.name", target = "teamName")
    })
    PlayerDTO playerToPlayerDTO(Player player);

}
