package com.pyratron.pugmatt.bedrockconnect.service;

import com.pyratron.pugmatt.bedrockconnect.model.Servers;
import com.pyratron.pugmatt.bedrockconnect.repository.ServerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServerService {
    @Autowired
    private ServerRepository serverRepository;
    
    public Servers findByUuid(String uuid){
        List<Servers> serversList = serverRepository.findServersByUuid(uuid);
        return serversList.get(0);
    }
}
