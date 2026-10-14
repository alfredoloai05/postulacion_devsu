package com.devsu.hackerearth.backend.account.model;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "client_snapshot")
public class ClientSnapshot {
    
    @Id
    private Long id;

    private String name;

    private boolean active;
}
