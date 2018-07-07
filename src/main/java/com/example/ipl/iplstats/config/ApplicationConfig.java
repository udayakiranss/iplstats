package com.example.ipl.iplstats.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties
public class ApplicationConfig {
    @Getter
    private Map<String, String> configValues;
    @Getter
    @Setter
    private String seasonLoaded;

    public ApplicationConfig(){
        configValues = new HashMap<String, String>();

        configValues.put("SeasonsTobeLoaded","2017,2016");
    }


}
