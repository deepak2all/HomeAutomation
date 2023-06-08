package com.home.iot.services;

import com.home.iot.domains.Slot;
import com.home.iot.repositories.SlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * SlotServiceImpl is service layer is a layer in an application that facilitates communication between
 * the SlotController and the persistence layer, SlotRepository.
 * Additionally, business logic to configure devices, is stored in the service layer.
 */
@Service
public class SlotServiceImpl implements SlotService {

    @Autowired
    private SlotRepository slotRepository;

    @Override
    public Slot save(Slot slot) {
        return slotRepository.save(slot);
    }

    @Override
    public Slot update(Slot slot) {
        return slotRepository.update(slot);
    }

    @Override
    public List<Slot> findAll(){
        return slotRepository.findAll();
    }

    @Override
    public Slot findSlotById(long slotId) throws Exception {
        return slotRepository.findSlotById(slotId);
    }

    @Override
    public List<Slot> findVacantSlots(){
        return slotRepository.findVacantSlots();
    }
}
