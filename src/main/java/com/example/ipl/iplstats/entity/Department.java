package com.example.ipl.iplstats.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.engine.internal.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
public class Department {

    private String name;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
//    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
//    @JoinColumn(name = "dept_id")
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "department",orphanRemoval = true)
    private List<Employee> employees = new ArrayList<Employee>();
}
