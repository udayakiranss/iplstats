package com.example.ipl.iplstats;

import com.example.ipl.iplstats.dao.SeasonDAO;
import com.example.ipl.iplstats.entity.*;
import com.example.ipl.iplstats.dao.DepartmentDAO;
import com.example.ipl.iplstats.dao.EmployeeDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class sampleApp implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(sampleApp.class);

    @Autowired
    private SeasonDAO seasonDAO;
    @Autowired
    private  DepartmentDAO departmentDAO;
    @Autowired
    private EmployeeDAO employeeDAO;

    public static void main(String[] args) {
        SpringApplication.run(sampleApp.class, args);
    }


    @Override
    @Transactional
    public void run(String... strings) throws Exception {
//        Season season = new Season();
        Season season = new Season();
        season.setYear("100");



        SeasonTeam team = new SeasonTeam();
        team.setName("IIII");
        Set<SeasonTeam> teams = new HashSet<SeasonTeam>();
        teams.add(team);
        season.setTeams(teams);

        seasonDAO.save(season);


        Season season2 = new Season();
        season2.setYear("200");

        SeasonTeam team2 = new SeasonTeam();
        team2.setName("JJJJJJ");
        Set<SeasonTeam> teams2 = new HashSet<SeasonTeam>();
        teams2.add(team2);
        season2.setTeams(teams2);

        seasonDAO.save(season2);

        System.out.println("Season data:"+seasonDAO.findAll());

        Employee emp1 = new Employee();
        emp1.setName("U");

        Employee emp2 = new Employee();
        emp2.setName("K");

        Department dept = new Department();
        dept.setName("Maths");
        dept.getEmployees().add(emp1);
        departmentDAO.save(dept);

        Department dept1 = new Department();
        dept1.setName("PH");
        dept1.getEmployees().add(emp2);
        departmentDAO.save(dept1);



//        emp1.setDepartment(dept);
//        employeeDAO.save(emp1);


//        emp2.setDepartment(dept1);
//        employeeDAO.save(emp2);


        System.out.println("Departments:"+ departmentDAO.findAll());



    }
}
