package com.example.ipl.iplstats.mapper;

import com.example.ipl.iplstats.data.MatchSummaryDTO;
import com.example.ipl.iplstats.data.SeasonDTO;
import com.example.ipl.iplstats.data.TeamDTO;
import com.example.ipl.iplstats.entity.MatchSummary;
import com.example.ipl.iplstats.entity.Season;
import com.example.ipl.iplstats.entity.Team;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

import java.util.Set;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN, componentModel = "spring")
public interface SeasonMapper {

    Season dtoToDomain(final SeasonDTO seasonDTO);

    SeasonDTO domainToDTO(final  Season season);

    Team teamDTOToTeam(TeamDTO team);

    Iterable<Team> teamDTOToTeamSet(Iterable<TeamDTO> teamDTOSet);

    TeamDTO seasonTeamToTeamDTO(Team sTeam);
    @Mappings({
            @org.mapstruct.Mapping(source = "summaryDTO.teamA", target = "teamA"),
            @org.mapstruct.Mapping(source = "summaryDTO.teamB", target = "teamB"),
            @org.mapstruct.Mapping(source = "summaryDTO.tossWinner", target = "tossWinner"),
            @org.mapstruct.Mapping(source = "summaryDTO.winner", target = "winner"),

    })
    MatchSummary matchSummaryDTOToMatch(MatchSummaryDTO summaryDTO);

    @Mappings({
            @org.mapstruct.Mapping(target = "teamA", source = "summary.teamA"),
            @org.mapstruct.Mapping(target = "teamB", source = "summary.teamB"),
            @org.mapstruct.Mapping(target = "tossWinner", source = "summary.tossWinner"),
            @org.mapstruct.Mapping(target = "winner", source = "summary.winner"),

    })
    MatchSummaryDTO matchSummaryToMatchDTO(MatchSummary summary);

}
