package com.example.transactionaltestintegration.repository;

import com.example.transactionaltestintegration.entity.Sector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SectorRepository extends JpaRepository<Sector, Long> {

}
