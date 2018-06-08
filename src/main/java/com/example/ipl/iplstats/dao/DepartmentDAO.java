package com.example.ipl.iplstats.dao;

import com.example.ipl.iplstats.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Repository
public interface DepartmentDAO extends JpaRepository<Department, Integer> {
}
