package com.example.ipl.iplstats.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
public class Employee  {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Integer id;

        private String name;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name="department_id")
        private Department department;
}
