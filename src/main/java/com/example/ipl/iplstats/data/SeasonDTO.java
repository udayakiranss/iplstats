package com.example.ipl.iplstats.data;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class SeasonDTO {

    private Long id;
    private String year;
    private String description;
    private Set<TeamDTO> teams;

    private Set<MatchSummaryDTO> matches;


//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getYear() {
//        return year;
//    }
//
//    public void setYear(String year) {
//        this.year = year;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        SeasonDTO seasonDTO = (SeasonDTO) o;
//        return getYear() == seasonDTO.getYear() &&
//                Objects.equals(getId(), seasonDTO.getId()) &&
//                Objects.equals(getDescription(), seasonDTO.getDescription());
//    }
//
//    @Override
//    public int hashCode() {
//
//        return Objects.hash(getId(), getYear(), getDescription());
//    }
//
//    @Override
//    public String toString() {
//        return "SeasonDTO{" +
//                "id=" + id +
//                ", year=" + year +
//                ", description='" + description + '\'' +
//                ", teams=" + teams +
//                '}';
//    }
}
