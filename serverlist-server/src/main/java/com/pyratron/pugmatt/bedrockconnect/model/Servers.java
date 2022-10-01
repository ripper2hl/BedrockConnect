package com.pyratron.pugmatt.bedrockconnect.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "servers")
public class Servers {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    
    @Column(name = "uuid")
    private String uuid;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "servers")
    private String servers;
    
    @Column(name = "serverLimit")
    private Integer serverLimit;
}
