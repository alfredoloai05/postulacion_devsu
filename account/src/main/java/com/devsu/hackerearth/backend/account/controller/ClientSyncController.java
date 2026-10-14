package com.devsu.hackerearth.backend.account.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devsu.hackerearth.backend.account.model.ClientSnapshot;
import com.devsu.hackerearth.backend.account.repository.ClientSnapshotRepository;


@RestController
@RequestMapping("/internal/clients")
public class ClientSyncController {
 
    private final ClientSnapshotRepository repository;

    public ClientSyncController(ClientSnapshotRepository repository){
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<Void> synchronize(@RequestBody ClientSnapshot client){
        
        if(
            client.getId() == null ||
            client.getName() == null ||   
            client.getName().trim().isEmpty()){
                return ResponseEntity.badRequest().build();
            }

        repository.save(client);

        return ResponseEntity.noContent().build();  
    }
}
