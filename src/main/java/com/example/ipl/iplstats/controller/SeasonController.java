package com.example.ipl.iplstats.controller;


import com.example.ipl.iplstats.data.SeasonDTO;
import com.example.ipl.iplstats.data.SeasonPointsDTO;
import com.example.ipl.iplstats.exception.IPLStatException;
import com.example.ipl.iplstats.service.SeasonInterface;
import com.example.ipl.iplstats.util.RestResponse;
import com.example.ipl.iplstats.utility.SeasonLoader;
import com.google.cloud.dialogflow.v2.Intent;
import com.google.cloud.dialogflow.v2.QueryResult;
import com.google.cloud.dialogflow.v2.WebhookRequest;
import com.google.cloud.dialogflow.v2.WebhookResponse;
import com.google.gson.Gson;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.Struct;
import com.google.protobuf.Value;
import com.google.protobuf.util.JsonFormat;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
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
    @Autowired
    private Gson gson;

    private static Gson gsonStatic ;

    @PostConstruct
    public void init() {
        SeasonController.gsonStatic = gson;
    }

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
        if(resourceLoader!=null){
            log.debug("ResourceLoader:"+resourceLoader);
        }
        RestResponse<String> restResponse = new RestResponse<String>();
        try {

            ClassPathResource cpr = new ClassPathResource("matches.csv");
            ClassPathResource cprd = new ClassPathResource("deliveries-sample.csv");

            byte[] matchData = FileCopyUtils.copyToByteArray(cpr.getInputStream());
            String matchContent = new String(matchData, StandardCharsets.UTF_8);

            byte[] deliveriesData = FileCopyUtils.copyToByteArray(cprd.getInputStream());
            String deiveryContent = new String(deliveriesData, StandardCharsets.UTF_8);


            seasonService.loadMatches(matchContent);
            seasonService.loadDeliveryDetails(deiveryContent);
            isLoadSuccessful = true;
            restResponse.setResponse("Loaded Successfully");
        }
        catch (IPLStatException e) {
            restResponse.setErrorCode(e.getErrorCode());
            restResponse.setError(true);
            restResponse.setErrorMessage(e.getErrorMessage());
        }
        catch (IOException e){
            e.printStackTrace();
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

    @PostMapping(value = "/chatbotaction", produces="application/json", consumes="application/json")
    @ResponseBody
    @ApiIgnore
    public WebhookResponse getChatBotResponse(@RequestBody WebhookRequest request){
        WebhookResponse response = null;
        log.debug("request:"+request);
        try {
//            WebhookRequest.Builder builder = WebhookRequest.newBuilder();
//            JsonFormat.parser().merge(request,builder);
//            WebhookRequest request2 = builder.build();

            if(request!=null){
                QueryResult result = request.getQueryResult();
                String action = result.getAction();

                WebhookResponse.Builder responseBuilder = WebhookResponse.newBuilder();
                responseBuilder.setFulfillmentText("Acknowledged the message");
                responseBuilder.addFulfillmentMessages(
                        Intent.Message.newBuilder().setText(Intent.Message.Text.newBuilder().addText("gfhgfhfh").build()).build());

                response = responseBuilder.build();
                if(action.equals("SeasonResults")){

                }else{

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }





        return response;
    }


    public static void main(String[] args) {

        String request = " {\n" +
                "  \"responseId\": \"ea3d77e8-ae27-41a4-9e1d-174bd461b68c\",\n" +
                "  \"session\": \"projects/your-agents-project-id/agent/sessions/88d13aa8-2999-4f71-b233-39cbf3a824a0\",\n" +
                "  \"queryResult\": {\n" +
                "    \"queryText\": \"user's original query to your agent\",\n" +
                "    \"parameters\": {\n" +
                "      \"param\": \"param value\"\n" +
                "    },\n" +
                "    \"allRequiredParamsPresent\": true,\n" +
                "    \"fulfillmentText\": \"Text defined in Dialogflows console for the intent that was matched\",\n" +
                "    \"fulfillmentMessages\": [\n" +
                "      {\n" +
                "        \"text\": {\n" +
                "          \"text\": [\n" +
                "            \"Text defined in Dialogflows console for the intent that was matched\"\n" +
                "          ]\n" +
                "        }\n" +
                "      }\n" +
                "    ],\n" +
                "    \"outputContexts\": [\n" +
                "      {\n" +
                "        \"name\": \"projects/your-agents-project-id/agent/sessions/88d13aa8-2999-4f71-b233-39cbf3a824a0/contexts/generic\",\n" +
                "        \"lifespanCount\": 5,\n" +
                "        \"parameters\": {\n" +
                "          \"param\": \"param value\"\n" +
                "        }\n" +
                "      }\n" +
                "    ],\n" +
                "    \"intent\": {\n" +
                "      \"name\": \"projects/your-agents-project-id/agent/intents/29bcd7f8-f717-4261-a8fd-2d3e451b8af8\",\n" +
                "      \"displayName\": \"Matched Intent Name\"\n" +
                "    },\n" +
                "    \"intentDetectionConfidence\": 1,\n" +
                "    \"diagnosticInfo\": {},\n" +
                "    \"languageCode\": \"en\"\n" +
                "  },\n" +
                "  \"originalDetectIntentRequest\": {}\n" +
                "}";

        InputStream stream  = new ByteArrayInputStream(request.getBytes(StandardCharsets.US_ASCII));
//        WebhookRequest request1 = WebhookRequest.(request.getBytes());
        try {
            WebhookRequest.Builder builder = WebhookRequest.newBuilder();
            JsonFormat.parser().merge(request,builder);
            WebhookRequest request2 = builder.build();
            System.out.println("Req:"+request2);
//            builder.mergeFrom(stream);
            WebhookResponse.Builder responseBuilder = WebhookResponse.newBuilder();
            responseBuilder.addFulfillmentMessages(
                    Intent.Message.newBuilder().setText(Intent.Message.Text.newBuilder().addText("gfhgfhfh").build()).build());

            WebhookResponse response = responseBuilder.build();
            System.out.println(gsonStatic.toJson(response));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
