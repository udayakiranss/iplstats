package com.example.ipl.iplstats.service;

import com.example.ipl.iplstats.data.PlayerDTO;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

public class PythonService {


    static final String URL_EMPLOYEES = "http://localhost:5000/iplstats/player/V Kohli";

    public static void main(String[] args) {

        try {
            // HttpHeaders
            HttpHeaders headers = new HttpHeaders();

            headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
            // Request to return XML format
            headers.setContentType(MediaType.APPLICATION_JSON);
//            headers.set("my_other_key", "my_other_value");

            // HttpEntity<Employee[]>: To get result as Employee[].
            HttpEntity<PlayerDTO> entity = new HttpEntity<PlayerDTO>(headers);

            // RestTemplate
            RestTemplate restTemplate = new RestTemplate();

            // Send request with GET method, and Headers.
            ResponseEntity<PlayerDTO> response = restTemplate.exchange(URL_EMPLOYEES, //
                    HttpMethod.GET, entity, PlayerDTO.class);

            HttpStatus statusCode = response.getStatusCode();
            System.out.println("Response Satus Code: " + statusCode);

            // Status Code: 200
            if (statusCode == HttpStatus.OK) {
                // Response Body Data
                PlayerDTO playerDTO = response.getBody();

                System.out.println(playerDTO);
            }
        } catch (RestClientException e) {
            e.printStackTrace();
        }

    }
}
