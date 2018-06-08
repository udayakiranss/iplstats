package com.example.ipl.iplstats.controller;


import com.example.ipl.iplstats.data.SeasonDTO;
import com.example.ipl.iplstats.exception.IPLStatException;
import com.example.ipl.iplstats.service.SeasonInterface;
import com.example.ipl.iplstats.util.RestResponse;
import com.example.ipl.iplstats.utility.SeasonLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@Slf4j
@RequestMapping(value = "/iplstats/")
public class SeasonController {
    @Autowired
    private SeasonInterface seasonService;
    @Autowired
    private SeasonLoader loader;

    @RequestMapping(value = "/season", method = RequestMethod.POST)
    public RestResponse<Map<String, String>> addSeason(@RequestBody SeasonDTO season){
        log.debug("Season DTO", season);
        RestResponse<Map<String, String>> response = new RestResponse<>();
        Map<String, String> responseMap = new HashMap<String,String>();
        try{
            seasonService.addSeason(season);
            responseMap.put("Season",season.getDescription());
            response.setResponse(responseMap);
            response.setError(false);
        }catch(IPLStatException ex){
            ex.printStackTrace();
            response.setError(true);
        }catch (Exception ex){
            ex.printStackTrace();
            response.setError(true);
        }

        return response;
    }

    @RequestMapping(value="/season/upload", method = RequestMethod.POST)
    public ResponseEntity<?> uploadSeasonDetails(
            @RequestParam("file") MultipartFile uploadfile) {

        log.debug("Single file upload!");

        if (uploadfile.isEmpty()) {
            return new ResponseEntity("please select a file!", HttpStatus.OK);
        }
        log.debug("File Name:" + uploadfile.getOriginalFilename());
//        try {
//
//
//
//        } catch (IOException e) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }

        return new ResponseEntity("Successfully uploaded - " +
                uploadfile.getOriginalFilename(), new HttpHeaders(), HttpStatus.OK);

    }

    @RequestMapping(value = "/season", method = RequestMethod.GET)
//    @PreAuthorize("#oauth2.hasScope('custom_mod')")
    public RestResponse<Map<String, List<SeasonDTO>>> getSeasons(){

        RestResponse<Map<String, List<SeasonDTO>>> response = new RestResponse<>();
        try{
            List<SeasonDTO> seasons=seasonService.getSeasons();
            Map<String, List<SeasonDTO>> seasonMap= new HashMap<String, List<SeasonDTO>>();
            //seasons.forEach(season -> seasonMap.put(season.getYear(), season));
            seasonMap.put("SeasonList",seasons);
            response.setResponse(seasonMap);
        }catch(IPLStatException ex){
            ex.printStackTrace();
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return response;
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public SeasonDTO getTestSeason(){
        SeasonDTO season = new SeasonDTO();
        season.setDescription("Test");
        season.setId(new Long(1));
        season.setYear(2001);
        return season;
    }

    @GetMapping("/principal")
    public Principal user(Principal principal) {
        return principal;
    }

    @GetMapping("/loadData")
    public boolean loadData(){
        boolean isLoadSuccessful = false;
        File matchFile = new File(SeasonLoader.class.getClassLoader().getResource("matches.csv").getFile());
        File deliveriesFile = new File(SeasonLoader.class.getClassLoader().getResource("deliveries-sample.csv").getFile());

        try {
            seasonService.loadMatches(matchFile);
            seasonService.loadDeliveryDetails(deliveriesFile);
            isLoadSuccessful = true;
        } catch (IPLStatException e) {
            e.printStackTrace();
        }
        return isLoadSuccessful;
    }
}
