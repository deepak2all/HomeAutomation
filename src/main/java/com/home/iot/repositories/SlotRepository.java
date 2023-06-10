package com.home.iot.repositories;

import com.home.iot.domains.SlotDTO;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class SlotRepository {

    HashMap<Long, SlotDTO> slots = new HashMap<>();

    public SlotDTO save(SlotDTO slotDTO) {
        return slots.putIfAbsent(slotDTO.getSlotId(), slotDTO);
    }

    public SlotDTO update(SlotDTO slotDTO) {
        return slots.put(slotDTO.getSlotId(), slotDTO);
    }

    public List<SlotDTO> findAll() {
        return slots.values().stream()
                .collect(Collectors.toList());
    }

    public SlotDTO findSlotById(long slotId) throws Exception {
        return slots.values().stream()
                .filter(x -> x.getSlotId() == slotId)
                .findAny()
                .get();
    }

    public List<SlotDTO> findVacantSlots() {
        return slots.values().stream()
                .filter(x -> x.getDeviceDTO() == null)
                .collect(Collectors.toList());
    }

    public String deleteSlot(long slotId) {
        slots.remove(slotId);
        return String.format("Slot %d is deleted",slotId);
    }
}
