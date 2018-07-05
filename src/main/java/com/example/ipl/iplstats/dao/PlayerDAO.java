package com.example.ipl.iplstats.dao;

import com.example.ipl.iplstats.entity.Player;
import com.example.ipl.iplstats.entity.Season;
import com.example.ipl.iplstats.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Component
public interface PlayerDAO extends JpaRepository<Player, Long> {

    Optional<Player> findByNameAndSeason(String name, Season season);
    @Query("select c from Player c where c.name like ?1 and c.season=?2")
    List<Player> findByNameAndSeasonLikeQuery(String name, Season season);

    @Query("select c from Player c where c.name like ?1")
    List<Player> findByNameLikeQuery(String searchTerm);

    List<Player> findByName(String name);

    List<Player> findBySeasonAndTeam(Season season, Team team);
}
