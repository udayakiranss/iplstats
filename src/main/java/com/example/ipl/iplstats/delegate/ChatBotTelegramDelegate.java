package com.example.ipl.iplstats.delegate;

import com.example.ipl.iplstats.data.PlayerDTO;
import com.example.ipl.iplstats.data.SeasonDTO;
import com.example.ipl.iplstats.data.SeasonStatisticsDTO;
import com.example.ipl.iplstats.exception.IPLStatException;
import com.example.ipl.iplstats.service.PlayerInterface;
import com.example.ipl.iplstats.service.SeasonInterface;
import com.example.ipl.iplstats.service.TeamInterface;
import com.google.cloud.dialogflow.v2.QueryResult;
import com.google.protobuf.ListValue;
import com.google.protobuf.Struct;
import com.google.protobuf.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class ChatBotTelegramDelegate {

    @Autowired
    private SeasonInterface seasonInterface;
    @Autowired
    private PlayerInterface playerInterface;
    @Autowired
    private TeamInterface teamInterface;


    public List<String>  answerQuery(QueryResult queryResult,
                                     Map<SeasonDTO, SeasonStatisticsDTO> seasonStatisticsDTOMap) throws IPLStatException {
        List<String> answers = new ArrayList<String>();

        Struct parameters = queryResult.getParameters();

        Map<String, Value> fieldsMap = parameters.getFieldsMap();
        ListValue seasonValue = fieldsMap.get("Season").getListValue();
        Value statistics =  fieldsMap.get("Statistics");
        Value result = fieldsMap.get("Result");
        Value category = fieldsMap.get("Category");
        Value player = fieldsMap.get("any");

        int year = new Integer(seasonValue.getValues(0).getStringValue());
        SeasonDTO seasonDTO = getSeasonDetails(year);
        SeasonStatisticsDTO pointsDTO = seasonStatisticsDTOMap.get(seasonDTO);


        if(result.getStringValue()!=null && result.getStringValue().length()>0){
            String resultType = result.getStringValue();
            answers.add(answerSeasonResults(resultType,pointsDTO));

            if(statistics.getStringValue()!=null && statistics.getStringValue().length() >0 &&
                    statistics.getStringValue().equals("Mom")){
                String mom = pointsDTO.getPlayerOfMatch();
                answers.add( mom + " was man of the match in finals of season " + year);

            }

        }else if(player!=null && player.getListValue()!=null && player.getListValue().getValuesCount()>0){

            String playerName = player.getListValue().getValues(0).getStringValue();

            if(playerName.length() < 4){
                answers.add("Not a valid player, Player name should be have at least 4 chars");
                return answers;
            }
            List<PlayerDTO> playerDTOList = getPlayer(playerName,year);
            PlayerDTO playerDTO = null;

            if(playerDTOList!=null){

                if(playerDTOList.size() == 1) {
                    playerDTO = playerDTOList.get(0);
                }else{
                    answers.add("We have multiple players with that name, Please send a player again");
                    for(PlayerDTO playerDTO1 : playerDTOList){
                        answers.add(playerDTO1.getName());
                    }
                }

                if(playerDTO!=null){
                    if(category.getListValue()!=null &&category.getListValue().getValuesCount() >0){
                        String categoryType = category.getListValue().getValues(0).getStringValue();
                        answers.add(answerPlayerRecords(playerDTO,categoryType));
                    }

                    if(statistics.getStringValue()!=null && statistics.getStringValue().length() >0){

                        String stats = statistics.getStringValue();
                        if(stats.equals("Details")){
                            answers.add(answerPlayerRecords(playerDTO,"Matches"));
                            answers.add(answerPlayerRecords(playerDTO,"Runs"));
                            answers.add(answerPlayerRecords(playerDTO,"Wickets"));
                        }

                    }
                }
            }

        }

        return answers;
    }


    private SeasonDTO getSeasonDetails(int season){
        SeasonDTO seasonDTO = new SeasonDTO();
        seasonDTO.setYear(season);
        return  seasonDTO;
    }

    private List<PlayerDTO> getPlayer(String playerName,int year)throws IPLStatException {

        String[] players = playerName.split("\\s");
        playerName=players[players.length-1];
        playerName = playerName.substring(0,1).toUpperCase() + playerName.substring(1).toLowerCase();
        if(playerName.endsWith("'s")){
            playerName=playerName.substring(0,playerName.indexOf("'"));
        }


        List<PlayerDTO> playerDTOList= playerInterface.getPlayerInfo(playerName,year);


        return playerDTOList;

    }


    private String answerSeasonResults(String resultType, SeasonStatisticsDTO pointsDTO){
        String winner = pointsDTO.getWinner();
        String loser = pointsDTO.getLoser();
        String answer="";
        if(resultType.equals("won")){
            answer = winner + " won the championship for season "
                    + pointsDTO.getSeason();
        }else if(resultType.equals("lost")){
            answer = loser + " lost the championship for season "
                    + pointsDTO.getSeason();
        }
        return  answer;
    }


    private String answerPlayerRecords(PlayerDTO playerDTO, String categoryType){

        String answer="";

        if(playerDTO!=null && categoryType!=null){

            String name = playerDTO.getName();

            if(categoryType.equals("Matches")){
                int noOfMatches = playerDTO.getNoOfMatches();
                answer  = name  + " played " + noOfMatches + " matches";
            }

            if(categoryType.equals("Runs")){
                int seasonRuns = playerDTO.getTotalRuns();
                answer = name  + " scored " + seasonRuns + " runs" ;
            }

            if(categoryType.equals("Wickets")){
                int seasonWickets = playerDTO.getTotalWickets();
                answer = name  + " got " + seasonWickets + " wickets";
            }

        }else{
            answer="Not a valid player in the season, Please send a valid player";
        }

        return  answer;
    }
}
