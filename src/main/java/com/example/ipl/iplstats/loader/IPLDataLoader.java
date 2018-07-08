package com.example.ipl.iplstats.loader;

import com.example.ipl.iplstats.config.ApplicationConfig;
import com.example.ipl.iplstats.dao.MatchDAO;
import com.example.ipl.iplstats.data.DeliveryDetailsDTO;
import com.example.ipl.iplstats.data.MatchesDTO;
import com.example.ipl.iplstats.entity.*;
import com.example.ipl.iplstats.exception.IPLStatException;
import com.example.ipl.iplstats.mapper.DeliveryMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.simpleflatmapper.csv.CsvMapperFactory;
import org.simpleflatmapper.csv.CsvParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
public class IPLDataLoader {
    @Getter
    private Set<MatchesDTO> matches=new HashSet<MatchesDTO>();
    @Getter
    private Set<Season> seasons = new HashSet<Season>();
    @Getter
    private Set<Team> teams = new HashSet<Team>();
    @Getter
    private Set<Player> players = new HashSet<Player>();
    @Getter
//    private List<MatchSummary> summaryList1 = new ArrayList<MatchSummary>();

    private Map<String, MatchSummary> summaryMap = new HashMap<String, MatchSummary>();

    private Set<DeliveryDetailsDTO> deliveryInfoList=new HashSet<DeliveryDetailsDTO>();
    @Getter
    private List<MatchDetails> detailsList = new ArrayList<MatchDetails>();
    @Autowired
    private MatchDAO matchDAO;
    @Autowired
    private ApplicationConfig config;


    public void parseMatches(String matchesFile)throws  IPLStatException{

        try {
            CsvParser
                    .mapWith(CsvMapperFactory
                            .newInstance()
                            .defaultDateFormat("yyyy-MM-dd")
                            .newMapper(MatchesDTO.class))
                    .forEach(matchesFile,matchesDTO -> matches.add(matchesDTO));

            createSeason();
            matches=null;
        } catch (IOException e) {
            e.printStackTrace();
            throw new IPLStatException("IPLS111","Not able to parse matches files");
        }

    }

    public Set<DeliveryDetailsDTO> parseDeliveriesFile(String csvFile)throws  IPLStatException {
        try {
            String [] seasons= config.getSeasonLoaded().split(",");
            List<String> seasonsToBeLoaded = new ArrayList<String>(Arrays.asList(seasons));

            CsvParser.skip(1).mapTo(DeliveryDetailsDTO.class)
                    .addMapping("match_id")
                    .addMapping("inning")
                    .addMapping("batting_team")
                    .addMapping("bowling_team")
                    .addMapping("over")
                    .addMapping("ball")
                    .addMapping("batsman")
                    .addMapping("non_striker")
                    .addMapping("bowler")
                    .addMapping("is_super_over")
                    .addMapping("wide_runs")
                    .addMapping("bye_runs")
                    .addMapping("legbye_runs")
                    .addMapping("noball_runs")
                    .addMapping("penalty_runs")
                    .addMapping("batsman_runs")
                    .addMapping("extra_runs")
                    .addMapping("total_runs")
                    .addMapping("player_dismissed")
                    .addMapping("dismissal_kind")
                    .addMapping("fielder")
                    .stream(csvFile).forEach(matchDetailsDTO -> deliveryInfoList.add(matchDetailsDTO));



//            CsvParser
//                    .mapWith(CsvMapperFactory
//                            .newInstance()
//                            .defaultDateFormat("yyyymmdd")
//                            .newBuilder(DeliveryDetailsDTO.class)
//                            .addMapping("match_id",0)
//                            .addMapping("inning",1)
//                            .addMapping("batting_team",2)
//                            .addMapping("bowling_team",3)
//                            .addMapping("batsman",6)
//                            .addMapping("non_striker",7)
//                            .addMapping("bowler",8)
//                            .addMapping("batsman_runs",15)
//                            .addMapping("total_runs",17)
//                            .addMapping("player_dismissed",18)
//                            .addMapping("dismissal_kind",19)
//                            .addMapping("fielder",20)
//                            .mapper())
//                    .forEach(csvFile, matchDetailsDTO -> deliveryInfoList.add(matchDetailsDTO));

            log.debug("matches = " + deliveryInfoList.size());



            DeliveryMapper mapper = Mappers.getMapper(DeliveryMapper.class);
            deliveryInfoList.forEach(deliveryDetails-> {
                String batsman = deliveryDetails.getBatsman();
                String nonStriker = deliveryDetails.getNon_striker();
                String bowler = deliveryDetails.getBowler();
                String runs= deliveryDetails.getBatsman_runs();

                String matchId = deliveryDetails.getMatch_id();
                String dismissalKind = deliveryDetails.getDismissal_kind();
                if(!matchId.equals("match_id")){

//                    summaryList.get
//                    MatchSummary summary = matchDAO.getOne(new Long(deliveryDetails.getMatch_id()));
                    MatchSummary summary = summaryMap.get(matchId);
                    if(summary!=null) {
                        Season season = summary.getSeason();
                        Team battingTeam = null;
                        Team fieldingTeam = null;
                        if (summary.getTeamA().getName().equals(deliveryDetails.getBatting_team())) {
                            battingTeam = summary.getTeamA();
                            fieldingTeam = summary.getTeamB();
                        } else {
                            battingTeam = summary.getTeamB();
                            fieldingTeam = summary.getTeamA();
                        }

                        boolean dismissalType = isWicketToBowler(dismissalKind);

                        getPlayer(matchId, batsman, Integer.parseInt(runs), dismissalType, season, battingTeam,false);
                        getPlayer(matchId, nonStriker, 0, dismissalType, season, battingTeam,false);
                        getPlayer(matchId, bowler, 0, dismissalType, season, fieldingTeam,true);
                        getPlayer(matchId, deliveryDetails.getFielder(), 0, dismissalType, season, fieldingTeam,false);
                        getPlayer(matchId, deliveryDetails.getPlayer_dismissed(), 0, dismissalType, season, battingTeam,false);

//                        MatchDetails matchDetails = mapper.deliveriesToMatchDetails(deliveryDetails);
//                        boolean dismissalType = matchDetails.isWicketToBowler();
//                        matchDetails.setMatchSummary(summary);
//                        matchDetails.setBatsman(getPlayer(matchId, batsman, Integer.parseInt(runs), dismissalType, season, battingTeam,false));
//                        matchDetails.setNonStriker(getPlayer(matchId, nonStriker, 0, dismissalType, season, battingTeam,false));
//                        matchDetails.setBowler(getPlayer(matchId, bowler, 0, dismissalType, season, fieldingTeam,true));
//                        matchDetails.setFielder(getPlayer(matchId, deliveryDetails.getFielder(), 0, dismissalType, season, fieldingTeam,false));
//                        matchDetails.setPlayerDismissed(getPlayer(matchId,
//                                deliveryDetails.getPlayer_dismissed(), 0, dismissalType, season, battingTeam,false));
//
//                        if(seasonsToBeLoaded.contains(summary.getSeason().getYear())){
//                            detailsList.add(matchDetails);
//                        }else{
//                            matchDetails=null;
//                        }

                    }


                }

            });

            deliveryInfoList = null;
        } catch (IOException e) {
            e.printStackTrace();
            throw new IPLStatException("IPLS112","Not able to parse deliveries files");
        }
        return deliveryInfoList;

    }

    public boolean isWicketToBowler(String dismissalKind){
        if(dismissalKind!=null && (dismissalKind.equals(DismissalKind.BOWLED.getKind()) ||
                dismissalKind.equals(DismissalKind.CAUGHT.getKind()) ||
                dismissalKind.equals(DismissalKind.CAUGHT_BOWLED.getKind()) ||
                dismissalKind.equals(DismissalKind.LBW.getKind()) ||
                dismissalKind.equals(DismissalKind.HIT_WICKET.getKind()) ||
                dismissalKind.equals(DismissalKind.STUMPED.getKind()))){

            return true;

        }
        return false;
    }



    private Player getPlayer( String matchId,String name,int runs, boolean isWicketToBowler, Season season,Team team, boolean isBowler) {
        Player player = null;
        boolean found = false;

        Iterator<Player> playerIterator = players.iterator();

        while(playerIterator.hasNext()){
            player = playerIterator.next();
            if(player.getName().equals(name) &&  player.getSeason().equals(season) &&
                    player.getTeam().equals(team)){
                player.addMatch(matchId);
                found = true;
                break;
            }
        }

        if(!found && name.length()>0 && !name.endsWith("(sub)")){
            player = new Player();
            player.setName(name);
            player.setSeason(season);
            player.setTeam(team);
            player.addMatch(matchId);
            players.add(player);
        }

//        if(player.getName().equals("YS Chahal") && season.getYear().equals("2017")){
//            kohli_Runs_2017+=runs;
//        }

        player.addRuns(runs);
        if(isWicketToBowler && isBowler){
            player.addWickets();
        }
        return player;
    }


    private void createSeason() {

        matches.forEach(matchesDTO -> {

            Season season = getSeason(matchesDTO);
            season.addTeam(getTeam(matchesDTO.getTeam1()));
            season.addTeam(getTeam(matchesDTO.getTeam2()));
            addMatchSummary(season,matchesDTO);


        });

    }


    private Season getSeason(MatchesDTO match){
        Season season = null;
        boolean found = false;
        Iterator<Season> seasonIter = seasons.iterator();
        while(seasonIter.hasNext()){
            season = seasonIter.next();
            if(season.getYear().equals(match.getSeason())){
                found = true;
                break;
            }
        }

        if(!found){
            season = new Season();
            season.setYear(match.getSeason());
            seasons.add(season);
        }
        return season;
    }

    private Team getTeam(String name){
        Team team =null;
        boolean found = false;
        Iterator<Team> teamIter = teams.iterator();
        while(teamIter.hasNext()){
            team = teamIter.next();
            if(team.getName().equals(name)){
                found= true;
                break;
            }
        }
        if(!found && name!=null && !name.isEmpty()){
            team = new Team();
            team.setName(name);
            teams.add(team);
        }
        return team;

    }


    private MatchSummary addMatchSummary(Season season, MatchesDTO match){

        Team teamA = getTeam(match.getTeam1());
        Team teamB = getTeam(match.getTeam2());
        Team tossWinner = getTeam(match.getToss_winner());
        Team winner = getTeam(match.getWinner());
        String playerOfMatch = match.getPlayer_of_match();

        MatchSummary matSummary = new MatchSummary(match.getId(),match.getCity(),match.getVenue(),teamA,
                teamB,tossWinner,
                match.getToss_decision(),winner,match.getWin_by_runs(),match.getWin_by_wickets());

        matSummary.setResult(match.getResult());
        matSummary.setPlayerOfMatch(playerOfMatch);

        matSummary.setDate(match.getDate());
        matSummary.setSeason(season);
        summaryMap.put(match.getId(),matSummary);

//        int summaryIndex = summaryList.indexOf(matSummary);
//
//        if(summaryIndex!=-1){
//            return  summaryList.get(summaryIndex);
//        }else{
//            summaryList.add(matSummary);
//        }
        return matSummary;

    }

    public List<MatchSummary> getSummaryList(){
        return new ArrayList<MatchSummary>(summaryMap.values());
    }


    public List<MatchDetails> processInputFile(File inputFile) {
        List<MatchDetails> inputList = new ArrayList<>();
        log.debug("File:", inputFile.getName());
        try{
//            File inputF = new File(inputFilePath);
            InputStream inputFS = new FileInputStream(inputFile);
            log.debug("File Exists");
            BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));
            // skip the header of the csv
            inputList = br.lines().skip(1).map(mapToItem).collect(Collectors.toList());
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputList ;
    }


    private Function<String, MatchDetails> mapToItem = (line) -> {
        String[] p = line.split(",");// a CSV has comma separated lines
        String matchId = p[0];
//        String battingTeam = p[2];
//        String fieldingTeam = p[3];
        String batsman = p[6];
        String nonStriker = p[7];
        String bowler = p[8];
        String runs= p[15];
        String dismissalKind = "";
        String playerDismissed = "";
        String fielder = "";
        if(p.length > 18){
            playerDismissed = p[18];
        }
        if(p.length > 19){
            dismissalKind = p[19];
        }
        if(p.length > 20){
            fielder = p[20];
        }


        MatchSummary summary = summaryMap.get(matchId);
        if(summary!=null) {
            Season season = summary.getSeason();
            Team battingTeam = null;
            Team fieldingTeam = null;
            if (summary.getTeamA().getName().equals(p[2])) {
                battingTeam = summary.getTeamA();
                fieldingTeam = summary.getTeamB();
            } else {
                battingTeam = summary.getTeamB();
                fieldingTeam = summary.getTeamA();
            }

            boolean dismissalType = isWicketToBowler(dismissalKind);

            getPlayer(matchId, batsman, Integer.parseInt(runs), dismissalType, season, battingTeam, false);
            getPlayer(matchId, nonStriker, 0, dismissalType, season, battingTeam, false);
            getPlayer(matchId, bowler, 0, dismissalType, season, fieldingTeam, true);
            getPlayer(matchId, fielder, 0, dismissalType, season, fieldingTeam, false);
            getPlayer(matchId, playerDismissed, 0, dismissalType, season, battingTeam, false);
        }
        MatchDetails item = new MatchDetails();
//        item.setItemNumber(p[0]);//<-- this is the first column in the csv file
//        if (p[3] != null && p[3].trim().length() > 0) {
//            item.setSomeProeprty(p[3]);
//        }
        //more initialization goes here
        return item;
    };

}
