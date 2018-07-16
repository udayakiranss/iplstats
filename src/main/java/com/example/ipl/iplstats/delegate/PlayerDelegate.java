package com.example.ipl.iplstats.delegate;

import com.example.ipl.iplstats.data.PlayerDTO;
import com.example.ipl.iplstats.data.SeasonDTO;
import com.example.ipl.iplstats.exception.IPLStatException;
import com.example.ipl.iplstats.service.PlayerInterface;
import com.example.ipl.iplstats.service.SeasonInterfaceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class PlayerDelegate {

    @Autowired
    private SeasonInterfaceImpl seasonInterface;
    @Autowired
    private PlayerInterface playerInterface;

    public void answerPlayerQuery(String player, String season, String statistics,
                                  String category, List<String> answers) throws IPLStatException {


        SeasonDTO seasonDTO = null;
        PlayerDTO playerDTO = null;

        if(season!=null && season.length()>0){
            int year = new Integer(season);
            seasonDTO=seasonInterface.getSeason(year);
        }

        if(player!=null&& player.length() > 0) {

            String playerName = player;

            if (playerName.length() < 4) {
                answers.add("Not a valid playerValue, Player name should be have at least 4 chars");
            }
            List<PlayerDTO> playerDTOList = getPlayer(playerName, seasonDTO);


            if (playerDTOList != null) {

                if (playerDTOList.size() == 1) {
                    playerDTO = playerDTOList.get(0);
                } else {
                    answers.add("We have multiple players with that name, Please send a playerValue again");
                    for (PlayerDTO playerDTO1 : playerDTOList) {
                        answers.add(playerDTO1.getName());
                    }
                }
            }
        }

        answerPlayerStatistics(playerDTO,category,statistics,seasonDTO,answers);


    }

    private void answerPlayerStatistics(PlayerDTO playerDTO,String category,String stats,
                                        SeasonDTO seasonDTO,List<String> answers) throws IPLStatException {
        if(playerDTO!=null){
            if(category!=null){
                String categoryType = category;
                answers.add(answerPlayerRecords(playerDTO,categoryType));
            }

            if(stats!=null){
//                playerDTO = getPlayer(playerDTO.getName(),seasonDTO);

                if(stats.equals("Details")){
                    answers.add(answerPlayerRecords(playerDTO,"Matches"));
                    answers.add(answerPlayerRecords(playerDTO,"Runs"));
                    answers.add(answerPlayerRecords(playerDTO,"Wickets"));
                }

                if(stats.equals("OrangeCap")){
                    answers.add(answerPlayerRecords(playerDTO,"Runs"));
                }

                if(stats.equals("PurpleCap")){
                    answers.add(answerPlayerRecords(playerDTO,"Wickets"));
                }
            }
        }

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

    private List<PlayerDTO> getPlayer(String playerName,SeasonDTO seasonDTO)throws IPLStatException {

        String[] players = playerName.split("\\s");
        playerName=players[players.length-1];
        playerName = playerName.substring(0,1).toUpperCase() + playerName.substring(1).toLowerCase();
        if(playerName.endsWith("'s")){
            playerName=playerName.substring(0,playerName.indexOf("'"));
        }
        List<PlayerDTO> playerDTOList = null;
        if(seasonDTO == null){
            PlayerDTO playerDTO= playerInterface.getPlayerStatistics(playerName,null);
            playerDTOList = new ArrayList<>();
            playerDTOList.add(playerDTO);
        }else{
            playerDTOList= playerInterface.getPlayerInfo(playerName,seasonDTO.getYear());
        }

        return playerDTOList;

    }

    private String getPlayerStat(String playerName,Integer totalRuns,Integer totalWickets){
        StringBuilder builder = new StringBuilder();
        if(totalRuns!=null && totalWickets!=null){
            builder.append(playerName + " have scored "+totalRuns + " runs and taken " + totalWickets
                    + " wickets in IPL");
        }else if(totalRuns!=null){
            builder.append(playerName + " have scored "+totalRuns + " runs");
        }else if(totalWickets!=null){
            builder.append(playerName + " have taken "+totalWickets + " wickets");
        }



        return builder.toString();
    }
}
