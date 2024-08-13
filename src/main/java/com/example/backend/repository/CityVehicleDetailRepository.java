package com.example.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.backend.model.CityVehicleDetail;

@Repository
public interface CityVehicleDetailRepository extends JpaRepository<CityVehicleDetail, Long> {
}
