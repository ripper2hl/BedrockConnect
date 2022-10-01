package com.pyratron.pugmatt.bedrockconnect.repository;

import com.pyratron.pugmatt.bedrockconnect.model.Servers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServerRepository extends JpaRepository<Servers, Integer> {
    List<Servers> findServersByUuid(String uuid);
}
