package com.devsu.hackerearth.backend.client.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class ClientSyncPublisher {
    
    private static final Logger log = LoggerFactory.getLogger(ClientSyncPublisher.class);

    @Value("${account.sync.url:http://localhost:8000/internal/clients}")
    private String syncUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @Async
    public void publish (Long id, String name, boolean active){

        Map<String, Object> data = new HashMap<>();
        data.put("id",id);
        data.put("name",name);
        data.put("active",active);

        try{
            restTemplate.postForEntity(syncUrl, data, Void.class);
        }catch (RestClientException ex){
            log.error("Error sincornizando cliente {}",id,ex);
        }
    }
}
