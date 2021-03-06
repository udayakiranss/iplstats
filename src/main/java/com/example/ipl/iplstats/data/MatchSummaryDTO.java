package com.example.ipl.iplstats.data;

import com.example.ipl.iplstats.entity.MatchResult;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode
@NoArgsConstructor
public class MatchSummaryDTO {
    private Long id;
    private Long matchID;
    private Date date;
    private String city;
    private String venue;
    private TeamDTO teamA;
    private TeamDTO teamB;
    private TeamDTO tossWinner;
    private String tossDecision;
    private String result;
    private TeamDTO winner;
    private int winByRuns;
    private int winByWickets;
    private PlayerDTO playerOfMatch;
    private SeasonDTO seasonDTO;

    private List<MatchDetailsDTO> matchDetails;



    public MatchSummaryDTO(String id,String city, String venue, TeamDTO teamA, TeamDTO teamB, TeamDTO tossWinner,
                           String tossDecision,String result,
                           TeamDTO winner, String winByRuns, String winByWickets){
        this.city= city;
        this.venue = venue;
        this.teamA = teamA;
        this.teamB = teamB;
        this.tossWinner = tossWinner;
        this.tossDecision = tossDecision;
        this.result= result;
        this.winner = winner;
        this.winByRuns = new Integer(winByRuns).intValue();
        this.winByWickets = new Integer(winByWickets).intValue();;
        this.matchID = new Long(id).longValue();
    }



    public String getResult(){
        return result;
    }

    public void setResult(String result){

        if(result == MatchResult.NORMAL.getResult()){
            result = MatchResult.NORMAL.getResult();
        }else if(result == MatchResult.TIE.getResult()){
            result = MatchResult.TIE.getResult();
        }else{
            result = MatchResult.NO_RESULT.getResult();
        }

    }



    public static MatchSummaryDTO copy(MatchSummaryDTO matchSummaryDTO){
        MatchSummaryDTO matchSummaryDTO1 = new MatchSummaryDTO();

        matchSummaryDTO1.setMatchID(matchSummaryDTO.getMatchID());
        matchSummaryDTO1.setTeamA(matchSummaryDTO.getTeamA());
        matchSummaryDTO1.setTeamB(matchSummaryDTO.getTeamB());

        return  matchSummaryDTO1;
    }

    @Override
    public String toString() {
        return "\nMatchSummaryDTO[" +
                "matchID=" + matchID +
                ", date='" + date + '\'' +
                ", teamA=" + teamA +
                ", teamB=" + teamB +
                ", winner=" + winner +
                ']';
    }
}
