package com.home.iot.repositories;

import com.home.iot.domains.Slot;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class SlotRepository {

    HashMap<Long, Slot> slots = new HashMap<>();

    public Slot save(Slot slot) {
        return slots.putIfAbsent(slot.getSlotId(), slot);
    }

    public Slot update(Slot slot) {
        return slots.put(slot.getSlotId(), slot);
    }

    public List<Slot> findAll() {
        return slots.values().stream()
                .collect(Collectors.toList());
    }

    public Slot findSlotById(long slotId) throws Exception {
        return slots.values().stream()
                .filter(x -> x.getSlotId() == slotId)
                .findAny()
                .get();
    }

    public List<Slot> findVacantSlots() {
        return slots.values().stream()
                .filter(x -> x.getDevice() == null)
                .collect(Collectors.toList());
    }

    public String deleteSlot(long slotId) {
        slots.remove(slotId);
        return String.format("Slot %d is deleted",slotId);
    }
}
