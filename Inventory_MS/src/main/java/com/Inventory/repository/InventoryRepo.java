package com.Inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Inventory.model.BusInventory;

public interface InventoryRepo extends JpaRepository<BusInventory, Integer>{

	public BusInventory findByBusNumber(String busNumber);
}
