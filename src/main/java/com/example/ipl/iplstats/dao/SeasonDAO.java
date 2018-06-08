package com.example.ipl.iplstats.dao;

import com.example.ipl.iplstats.entity.Season;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
@Component
public interface SeasonDAO extends JpaRepository<Season, Long> {
}
