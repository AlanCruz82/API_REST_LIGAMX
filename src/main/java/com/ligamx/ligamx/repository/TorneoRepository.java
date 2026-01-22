package com.ligamx.ligamx.repository;

import com.ligamx.ligamx.entity.Torneo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TorneoRepository extends JpaRepository<Torneo,Long> {
}
