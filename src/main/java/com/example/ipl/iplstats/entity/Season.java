package com.example.ipl.iplstats.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@EqualsAndHashCode(of = "year")
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"year"})})
public class Season {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String year;
    private String description;

    @ManyToMany(cascade = {CascadeType.ALL},fetch = FetchType.EAGER)
    @JoinTable(name = "season_team",
            joinColumns = @JoinColumn(name = "season_id"),
            inverseJoinColumns = @JoinColumn(name = "team_id"))
    private Set<Team> teams;

    @Override
    public String toString() {
        return "Season{" +
                "id=" + id +
                ", year='" + year + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public void addTeam(Team team) {
        if(teams == null){
            teams = new HashSet<Team>();
        }
        teams.add(team);
    }


}


