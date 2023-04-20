package com.bbw.restapp.config;

import org.openspaces.core.GigaSpace;
import org.openspaces.core.GigaSpaceConfigurer;
import org.openspaces.core.space.SpaceProxyConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;

import java.util.Map;

@Configuration
public class SpaceConfiguration {

    @Value("${space.name}")
    private String spaceName;

    @Value("${manager.host}")
    private String managerHost;

    @Value("${lookup.group}")
    private String lookupGroup;

    @Bean
    public GigaSpace gigaSpace(){
        String lookupLocators =managerHost+":4174";
        return new GigaSpaceConfigurer(new SpaceProxyConfigurer(spaceName)
                .lookupGroups(lookupGroup)
                .lookupLocators(lookupLocators)
                ).create();
    }

    /*private String getLookupGroup(){
        String lookupGroup = "";

        RestTemplate template = new RestTemplate();
        String obj  = template.getForObject(managerInfoURL,String.class);
        String strResponse = obj.toString();
        JsonObject jsonObject = new Gson().fromJson(strResponse, JsonObject.class);
        lookupGroup = jsonObject.get("lookupGroups").getAsString();
        return lookupGroup;
    }*/

}

