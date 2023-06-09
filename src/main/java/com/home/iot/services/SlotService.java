package com.home.iot.services;

import com.home.iot.domains.Slot;

import java.util.List;

public interface SlotService {

	// Find all slots
    List<Slot> findAll();

    // Find slot by Id
    Slot findSlotById(long slotId) throws Exception;

	// Find empty slots
    List<Slot> findVacantSlots();

	// Add slots
    Slot save(Slot slot);

    // Update slot
    Slot update(Slot slot);

    String deleteSlot(long id);

    // Remove slots

}
