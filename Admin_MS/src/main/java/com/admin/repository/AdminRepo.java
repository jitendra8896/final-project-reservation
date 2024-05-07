package com.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.admin.model.Bus;

@Repository
public interface AdminRepo extends JpaRepository<Bus, Integer> {

	Bus findByBusNumber(String busNumber);
}
