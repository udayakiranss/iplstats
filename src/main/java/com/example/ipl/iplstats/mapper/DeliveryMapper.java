package com.example.ipl.iplstats.mapper;

import com.example.ipl.iplstats.data.DeliveryDetailsDTO;
import com.example.ipl.iplstats.data.MatchDetailsDTO;
import com.example.ipl.iplstats.entity.MatchDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

/**
 * Created by kiranuda on 6/6/2018.
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN, componentModel = "spring")
public interface DeliveryMapper {


    @Mappings({
            @Mapping(source = "deliveryDetailsDTO.match_id", target = "matchID"),
            @Mapping(source = "deliveryDetailsDTO.inning", target = "innings"),
            @Mapping(source = "deliveryDetailsDTO.batting_team", target = "battingTeam"),
            @Mapping(source = "deliveryDetailsDTO.bowling_team", target = "bowlingTeam"),
            @Mapping(source = "deliveryDetailsDTO.over", target = "over"),
            @Mapping(source = "deliveryDetailsDTO.ball", target = "ball"),
            @Mapping(source = "deliveryDetailsDTO.batsman", target = "batsman"),
            @Mapping(source = "deliveryDetailsDTO.non_striker", target = "nonStriker"),
            @Mapping(source = "deliveryDetailsDTO.bowler", target = "bowler"),
            @Mapping(source = "deliveryDetailsDTO.is_super_over", target = "superOver"),
            @Mapping(source = "deliveryDetailsDTO.wide_runs", target = "wideRuns"),
            @Mapping(source = "deliveryDetailsDTO.bye_runs", target = "byeRuns"),
            @Mapping(source = "deliveryDetailsDTO.legbye_runs", target = "legbyeRuns"),
            @Mapping(source = "deliveryDetailsDTO.noball_runs", target = "noballRuns"),
            @Mapping(source = "deliveryDetailsDTO.penalty_runs", target = "penaltyRuns"),
            @Mapping(source = "deliveryDetailsDTO.batsman_runs", target = "batsmanRuns"),
            @Mapping(source = "deliveryDetailsDTO.extra_runs", target = "extraRuns"),
            @Mapping(source = "deliveryDetailsDTO.total_runs", target = "totalRuns"),
            @Mapping(source = "deliveryDetailsDTO.player_dismissed", target = "playerDismissed"),
            @Mapping(source = "deliveryDetailsDTO.dismissal_kind", target = "dismissalKind"),
            @Mapping(source = "deliveryDetailsDTO.fielder", target = "fielder"),

    })
    MatchDetails deliveriesToMatchDetails(DeliveryDetailsDTO deliveryDetailsDTO);
}
