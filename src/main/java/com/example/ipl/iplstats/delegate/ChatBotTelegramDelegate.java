package com.example.ipl.iplstats.delegate;

import com.example.ipl.iplstats.data.SeasonDTO;
import com.example.ipl.iplstats.data.SeasonStatisticsDTO;
import com.example.ipl.iplstats.exception.IPLStatException;
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
    private SeasonDelegate seasonDelegate;
    @Autowired
    private PlayerDelegate playerDelegate;


    public List<String>  answerQuery(QueryResult queryResult,
                                     Map<SeasonDTO, SeasonStatisticsDTO> seasonStatisticsDTOMap)
            throws IPLStatException {
        List<String> answers = new ArrayList<String>();

        Struct parameters = queryResult.getParameters();

        Map<String, Value> fieldsMap = parameters.getFieldsMap();
        ListValue seasonValue = fieldsMap.get("Season").getListValue();
        Value statisticsValue = fieldsMap.get("Statistics");
        Value resultValue = fieldsMap.get("Result");
        Value categoryValue = fieldsMap.get("Category");
        Value playerValue = fieldsMap.get("any");


        String player = null;
        String result = null;
        String category = null;
        String statistics = null;
        String season = null;

        if (playerValue != null && playerValue.getListValue() != null
                && playerValue.getListValue().getValuesCount() > 0) {
            player = playerValue.getListValue().getValues(0).getStringValue();
        }
        if (resultValue.getStringValue() != null && resultValue.getStringValue().length() > 0) {
            result = resultValue.getStringValue();
        }
        if (categoryValue.getListValue() != null && categoryValue.getListValue().getValuesCount() > 0) {
            category = categoryValue.getListValue().getValues(0).getStringValue();

        }
        if (statisticsValue.getStringValue() != null && statisticsValue.getStringValue().length() > 0) {
            statistics = statisticsValue.getStringValue();
        }
        if (seasonValue != null && seasonValue.getValuesCount() > 0) {
            season = seasonValue.getValues(0).getStringValue();
        }

        if (player != null && statistics != null) {
            playerDelegate.answerPlayerQuery(player, season, statistics, category, answers);
        }else if (season != null && (result != null || statistics!=null)) {
            seasonDelegate.answerSeasonQuery(season, result, statistics, answers, seasonStatisticsDTOMap);
        }
        return answers;
    }


//        Player + stat + categoryValue + season
//        Season + resultValue + stat



//        if(season!=null){
//            int year = new Integer(seasonValue.getValues(0).getStringValue());
//            SeasonDTO seasonDTO = getSeasonDetails(year);
//            SeasonStatisticsDTO pointsDTO = seasonStatisticsDTOMap.get(seasonDTO);
//            if(player!=null&& player.length() > 0){
//
//                String playerName = player;
//
//                if(playerName.length() < 4){
//                    answers.add("Not a valid playerValue, Player name should be have at least 4 chars");
//                    return answers;
//                }
//                List<PlayerDTO> playerDTOList = getPlayer(playerName,year);
//                PlayerDTO playerDTO = null;
//
//                if(playerDTOList!=null){
//
//                    if(playerDTOList.size() == 1) {
//                        playerDTO = playerDTOList.get(0);
//                    }else{
//                        answers.add("We have multiple players with that name, Please send a playerValue again");
//                        for(PlayerDTO playerDTO1 : playerDTOList){
//                            answers.add(playerDTO1.getName());
//                        }
//                    }
//
//                    if(playerDTO!=null){
//                        if(category!=null){
//                            String categoryType = category;
//                            answers.add(answerPlayerRecords(playerDTO,categoryType));
//                        }
//
//                        if(statistics!=null){
//
//                            String stats = statistics;
//                            if(stats.equals("Details")){
//                                answers.add(answerPlayerRecords(playerDTO,"Matches"));
//                                answers.add(answerPlayerRecords(playerDTO,"Runs"));
//                                answers.add(answerPlayerRecords(playerDTO,"Wickets"));
//                            }
//
//                            if(stats.equals("OrangeCap")){
//                                PlayerDTO playerDTO1 = seasonInterface.orangeCapPlayer(seasonDTO);
//                                answers.add(playerDTO1.getName() + " got the orange cap for scoring "+ playerDTO1.getTotalRuns() + " runs");
//                            }
//
//                            if(stats.equals("PurpleCap")){
//                                PlayerDTO playerDTO1 = seasonInterface.purpleCapPlayer(seasonDTO);
//                                answers.add(playerDTO1.getName() + " got the purple cap for getting "+ playerDTO1.getTotalWickets() + " wickets");
//                            }
//                        }
//                    }
//                }
//
//            }
//        }else{
//            if(player!=null){
//                String playerName = player;
//
////                List<PlayerDTO> players = getPlayer(playerName,0);
//                PlayerDTO playerDTO = playerInterface.getPlayerStatistics(playerName);
//                if(playerDTO == null){
//                    answers.add("Player doesn't exists in system, Please enter valid playerValue");
//                }else{
//                    String cat =  category;
//
//                    String stat = statistics;
//
//                    switch (stat){
//                        case "OrangeCap":{
//                            if(cat.equals("Runs")){
//                                answers.add(getPlayerStat(playerDTO.getName(),playerDTO.getTotalRuns(),null) );
//                            }
//                            break;
//                        }
//                        case "Details":{
//
//                            answers.add(getPlayerStat(playerDTO.getName(),playerDTO.getTotalRuns(),playerDTO.getTotalWickets()));
//                            break;
//                        }
//                        case "Total":{
//                            if(cat.equals("Runs")){
//                                answers.add(getPlayerStat(playerDTO.getName(),playerDTO.getTotalRuns(),null) );
//                            }else if(cat.equals("Wickets")){
//                                answers.add(getPlayerStat(playerDTO.getName(),null,playerDTO.getTotalWickets()) );
//                            }
//                            break;
//                        }
//                        case "PurpleCap":{
//                            if(cat.equals("Wickets")){
//                                answers.add(getPlayerStat(playerDTO.getName(),null,playerDTO.getTotalWickets()) );
//                            }
//                            break;
//                        }
//                    }
//                }
//            }
//        }
//
//
//
//
//        return answers;
//    }


//    private SeasonDTO getSeasonDetails(int year) throws IPLStatException {
////        SeasonDTO seasonDTO = new SeasonDTO();
////        seasonDTO.setYear(season);
//
//        SeasonDTO seasonDTO = seasonInterface.getSeason(year);
//
//        return  seasonDTO;
//    }

//    private List<PlayerDTO> getPlayer(String playerName,int year)throws IPLStatException {
//
//        String[] players = playerName.split("\\s");
//        playerName=players[players.length-1];
//        playerName = playerName.substring(0,1).toUpperCase() + playerName.substring(1).toLowerCase();
//        if(playerName.endsWith("'s")){
//            playerName=playerName.substring(0,playerName.indexOf("'"));
//        }
//        List<PlayerDTO> playerDTOList = null;
//        if(year == 0){
//            playerDTOList= playerInterface.getPlayerInfo(playerName);
//        }else{
//            playerDTOList= playerInterface.getPlayerInfo(playerName,year);
//        }
//
//        return playerDTOList;
//
//    }
//
//
//
//
//
//    private String answerPlayerRecords(PlayerDTO playerDTO, String categoryType){
//
//        String answer="";
//
//        if(playerDTO!=null && categoryType!=null){
//
//            String name = playerDTO.getName();
//
//            if(categoryType.equals("Matches")){
//                int noOfMatches = playerDTO.getNoOfMatches();
//                answer  = name  + " played " + noOfMatches + " matches";
//            }
//
//            if(categoryType.equals("Runs")){
//                int seasonRuns = playerDTO.getTotalRuns();
//                answer = name  + " scored " + seasonRuns + " runs" ;
//            }
//
//            if(categoryType.equals("Wickets")){
//                int seasonWickets = playerDTO.getTotalWickets();
//                answer = name  + " got " + seasonWickets + " wickets";
//            }
//
//        }else{
//            answer="Not a valid player in the season, Please send a valid player";
//        }
//
//        return  answer;
//    }
//
//
//    private String getPlayerStat(String playerName,Integer totalRuns,Integer totalWickets){
//        StringBuilder builder = new StringBuilder();
//        if(totalRuns!=null && totalWickets!=null){
//            builder.append(playerName + " have scored "+totalRuns + " runs and taken " + totalWickets
//                    + " wickets in IPL");
//        }else if(totalRuns!=null){
//            builder.append(playerName + " have scored "+totalRuns + " runs in IPL");
//        }else if(totalWickets!=null){
//            builder.append(playerName + " have taken "+totalWickets + " wickets in IPL");
//        }
//
//
//
//        return builder.toString();
//    }


}
