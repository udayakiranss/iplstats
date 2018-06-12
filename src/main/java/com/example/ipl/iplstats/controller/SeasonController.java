package com.example.ipl.iplstats.controller;


import com.example.ipl.iplstats.data.SeasonDTO;
import com.example.ipl.iplstats.data.SeasonPointsDTO;
import com.example.ipl.iplstats.exception.IPLStatException;
import com.example.ipl.iplstats.service.SeasonInterface;
import com.example.ipl.iplstats.util.RestResponse;
import com.example.ipl.iplstats.utility.SeasonLoader;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@Slf4j
@RequestMapping(value = "/iplstats/")
@Api(
        value = "IPL Statistics Operations"
)
@ApiResponses(value = {
        @ApiResponse(code = 415, message = "Content type not supported.")
})
public class SeasonController {


    @Autowired
    private SeasonInterface seasonService;

    @Autowired
    private ResourceLoader resourceLoader;

    @ApiOperation(notes = "Add a new season",
            value = "To add a new season")
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


        return new ResponseEntity("Successfully uploaded - " +
                uploadfile.getOriginalFilename(), new HttpHeaders(), HttpStatus.OK);

    }

    @ApiOperation(notes = "Fetch all season details",
            value = "To get all season details"
    )
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

    @GetMapping("/principal")
    @ApiIgnore
    public Principal user(Principal principal) {
        return principal;
    }

    @ApiOperation(notes = "Load IPL Data from the resources",
            value = "Load IPL Data from the resources"
    )
    @GetMapping("/loadData")
    public RestResponse loadData(){
        boolean isLoadSuccessful = false;
        RestResponse<String> restResponse = new RestResponse<String>();
        try {
//        File matchFile = new File(SeasonLoader.class.getClassLoader().getResource("matches.csv").getFile());
//        File deliveriesFile = new File(SeasonLoader.class.getClassLoader().getResource("deliveries-sample.csv").getFile());
            File matchFile = ResourceUtils.getFile("classpath:matches.csv");
            File deliveriesFile = ResourceUtils.getFile("classpath:deliveries-sample.csv");
            seasonService.loadMatches(matchFile);
            seasonService.loadDeliveryDetails(deliveriesFile);
            isLoadSuccessful = true;
            restResponse.setResponse("Loaded Successfully");
        } catch (IPLStatException e) {
            restResponse.setErrorCode(e.getErrorCode());
            restResponse.setError(true);
            restResponse.setErrorMessage(e.getErrorMessage());
        } catch (IOException e){
            restResponse.setErrorCode("IPL200");
            restResponse.setError(true);
            restResponse.setErrorMessage("IO Exception while parsing the data file");
        }
        return restResponse;
    }



    @PostMapping(value = "/pointsTable",consumes = "application/json")
    @ApiOperation(notes = "Fetch the points table for a season",
            value = "To get the points table for a season")
    @ApiResponses(value = {
            @ApiResponse(code = 415, message = "Content type not supported.")
    })
    public RestResponse<SeasonPointsDTO> fetchPointsTable(@RequestBody SeasonDTO seasonDTO){
        RestResponse<SeasonPointsDTO> responseDTO = new RestResponse<>();
        try {
            SeasonPointsDTO pointsDTO=seasonService.fetchPointsTable(seasonDTO);
            responseDTO.setResponse(pointsDTO);
        } catch (IPLStatException e) {

            responseDTO.setErrorCode(e.getErrorCode());
            responseDTO.setError(true);
            responseDTO.setErrorMessage(e.getMessage());
        }
        return responseDTO;
    }
}
