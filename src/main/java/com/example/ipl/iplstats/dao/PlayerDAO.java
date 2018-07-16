package com.example.ipl.iplstats.dao;

import com.example.ipl.iplstats.data.SeasonDTO;
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
    @Query(value="select name, max(TOTAL_RUNS ) from PLAYER where season_id=?1 group by name order by total_runs desc limit 1\n",nativeQuery = true)
    List<Object[]>  findByMaxRuns(Long seasonId);

    @Query(value = "select name, max(TOTAL_wickets ) from PLAYER where season_id=?1 group by name order by total_wickets desc limit 1\n",nativeQuery = true)
    List<Object[]>  findByMaxWickets(Long seasonId);

    @Query(value = "SELECT sum(TOTAL_RUNS ),sum(TOTAL_WICKETS ) FROM PLAYER where name like ?1",nativeQuery = true)
    List<Object[]>  findByStats(String playerName);

    @Query(value = "SELECT sum(TOTAL_RUNS ),sum(TOTAL_WICKETS ) FROM PLAYER where name like ?1 and SEASON_ID=?2 ",nativeQuery = true)
    List<Object[]>  findBySeasonStats(String playerName,Long seasonId);
}
