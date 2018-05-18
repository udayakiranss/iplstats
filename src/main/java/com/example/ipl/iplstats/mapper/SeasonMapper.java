package com.example.ipl.iplstats.mapper;

import com.example.ipl.iplstats.data.SeasonDTO;
import com.example.ipl.iplstats.entity.Season;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN, componentModel = "spring")
public interface SeasonMapper {

    Season dtoToDomain(final SeasonDTO seasonDTO);

    SeasonDTO domainToDTO(final  Season season);
}
