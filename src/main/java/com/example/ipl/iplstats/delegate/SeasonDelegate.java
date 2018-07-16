package com.example.ipl.iplstats.delegate;

import com.example.ipl.iplstats.data.PlayerDTO;
import com.example.ipl.iplstats.data.SeasonDTO;
import com.example.ipl.iplstats.data.SeasonStatisticsDTO;
import com.example.ipl.iplstats.exception.IPLStatException;
import com.example.ipl.iplstats.service.SeasonInterfaceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class SeasonDelegate {

    @Autowired
    private SeasonInterfaceImpl seasonInterface;


    public void answerSeasonQuery(String season, String result, String statistics, List<String> answers,Map<SeasonDTO,
            SeasonStatisticsDTO> seasonStatisticsDTOMap) throws IPLStatException {

        int year = new Integer(season);
        SeasonDTO seasonDTO = seasonInterface.getSeason(year);
        SeasonStatisticsDTO pointsDTO = seasonStatisticsDTOMap.get(seasonDTO);
        if(result!=null)
            answerSeasonResults(result,pointsDTO,answers);

        answerSeasonStatistics(seasonDTO,statistics,pointsDTO,answers);
    }

    private void answerSeasonStatistics(SeasonDTO seasonDTO, String statistics,
                                         SeasonStatisticsDTO pointsDTO,List<String> answers) {
        if( statistics !=null) {
            int year = seasonDTO.getYear();
            if(statistics.equals("Mom")){
                String mom = pointsDTO.getPlayerOfMatch();
                answers.add( mom + " was man of the match in finals of season " + year + " by scoring " +pointsDTO.getManOfTheMatch().getTotalRuns()
                        + " runs and taking " + pointsDTO.getManOfTheMatch().getTotalWickets()+ " wickets");
            }
            if(statistics.equals("OrangeCap")){
                PlayerDTO playerDTO1 = seasonInterface.orangeCapPlayer(seasonDTO);
                answers.add(playerDTO1.getName() + " got the orange cap for scoring "+ playerDTO1.getTotalRuns() + " runs");
            }

            if(statistics.equals("PurpleCap")){
                PlayerDTO playerDTO1 = seasonInterface.purpleCapPlayer(seasonDTO);
                answers.add(playerDTO1.getName() + " got the purple cap for getting "+ playerDTO1.getTotalWickets() + " wickets");
            }
        }

    }

    private void answerSeasonResults(String resultType, SeasonStatisticsDTO pointsDTO,List<String> answers){
        String winner = pointsDTO.getWinner();
        String loser = pointsDTO.getLoser();
        String runsWickets ="";
        if(pointsDTO.getRuns() == 0)
            runsWickets = pointsDTO.getWickets() + " wickets";
        else
            runsWickets = pointsDTO.getRuns() + " runs";

        if(resultType.equals("won")){
            answers.add(winner + " won the championship for season  "
                    + pointsDTO.getSeason() + " beating " + pointsDTO.getLoser()
                    + " by " + runsWickets);
        }else if(resultType.equals("lost")){
            answers.add(loser + " lost the championship for season "
                    + pointsDTO.getSeason() + " to " + pointsDTO.getWinner() + " by " + runsWickets);
        }
    }

}
