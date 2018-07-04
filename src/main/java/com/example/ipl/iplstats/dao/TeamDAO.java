package com.example.ipl.iplstats.dao;

import com.example.ipl.iplstats.entity.Season;
import com.example.ipl.iplstats.entity.SeasonTeam;
import com.example.ipl.iplstats.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
@Component
public interface TeamDAO extends JpaRepository<Team, Long> {

    Set<Team> findBySeason(Season season);

    Team findBySeasonAndName(Season season, String name);
}
