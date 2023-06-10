package com.home.iot.services;

import com.home.iot.domains.SlotDTO;

import java.util.List;

public interface SlotService {

	// Find all slots
    List<SlotDTO> findAll();

    // Find slot by Id
    SlotDTO findSlotById(long slotId) throws Exception;

	// Find empty slots
    List<SlotDTO> findVacantSlots();

	// Add slots
    SlotDTO save(SlotDTO slotDTO);

    // Update slot
    SlotDTO update(SlotDTO slotDTO);

    String deleteSlot(long id);

    // Remove slots

}
