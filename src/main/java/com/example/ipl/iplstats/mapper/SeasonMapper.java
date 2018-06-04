package com.example.ipl.iplstats.mapper;

import com.example.ipl.iplstats.data.MatchSummaryDTO;
import com.example.ipl.iplstats.data.SeasonDTO;
import com.example.ipl.iplstats.data.TeamDTO;
import com.example.ipl.iplstats.entity.MatchSummary;
import com.example.ipl.iplstats.entity.Season;
import com.example.ipl.iplstats.entity.SeasonTeam;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN, componentModel = "spring")
public interface SeasonMapper {

    Season dtoToDomain(final SeasonDTO seasonDTO);

    SeasonDTO domainToDTO(final  Season season);

    SeasonTeam teamDTOToSeasonTeam(TeamDTO team);

    TeamDTO seasonTeamToTeamDTO(SeasonTeam sTeam);
    @Mappings({
            @org.mapstruct.Mapping(source = "summaryDTO.teamA.name", target = "teamA"),
            @org.mapstruct.Mapping(source = "summaryDTO.teamB.name", target = "teamB"),
            @org.mapstruct.Mapping(source = "summaryDTO.tossWinner.name", target = "tossWinner"),
            @org.mapstruct.Mapping(source = "summaryDTO.winner.name", target = "winner"),

    })
    MatchSummary matchSummaryDTOToMatch(MatchSummaryDTO summaryDTO);

    @Mappings({
            @org.mapstruct.Mapping(target = "teamA.name", source = "summary.teamA"),
            @org.mapstruct.Mapping(target = "teamB.name", source = "summary.teamB"),
            @org.mapstruct.Mapping(target = "tossWinner.name", source = "summary.tossWinner"),
            @org.mapstruct.Mapping(target = "winner.name", source = "summary.winner"),

    })
    MatchSummaryDTO matchSummaryToMatchDTO(MatchSummary summary);

}
