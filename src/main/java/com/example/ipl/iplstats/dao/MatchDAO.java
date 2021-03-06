package com.example.ipl.iplstats.dao;

import com.example.ipl.iplstats.entity.MatchSummary;
import com.example.ipl.iplstats.entity.Season;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
@Component
public interface MatchDAO extends JpaRepository<MatchSummary, Long> {

    Set<MatchSummary>  findBySeason(Season season);
}
