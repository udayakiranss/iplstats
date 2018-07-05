package com.example.ipl.iplstats.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by kiranuda on 6/7/2018.
 */
@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "name")
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"name"})})
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "teams",fetch = FetchType.LAZY)
    private Set<Season> season;

}
