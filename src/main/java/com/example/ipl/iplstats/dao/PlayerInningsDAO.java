package com.example.ipl.iplstats.dao;

import com.example.ipl.iplstats.entity.MatchSummary;
import com.example.ipl.iplstats.entity.Player;
import com.example.ipl.iplstats.entity.PlayerInnings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
@Component
public interface PlayerInningsDAO extends JpaRepository<PlayerInnings, Long> {

    PlayerInnings findByPlayerAndSummary(Player player, MatchSummary summary);
}
