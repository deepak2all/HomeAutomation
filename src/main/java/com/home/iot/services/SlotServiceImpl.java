package com.home.iot.services;

import com.home.iot.domains.SlotDTO;
import com.home.iot.exceptions.IncorrectInputException;
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
    public SlotDTO save(SlotDTO slotDTO) {
        return slotRepository.save(slotDTO);
    }

    @Override
    public SlotDTO update(SlotDTO slotDTO) {
        return slotRepository.update(slotDTO);
    }

    @Override
    public String deleteSlot(long slotId) {
        if(slotRepository.findVacantSlots().stream().anyMatch(x -> x.getSlotId() == slotId)) {
            return slotRepository.deleteSlot(slotId);
        } else {
            throw new IncorrectInputException(String.format("Slot %d is not empty",slotId));
        }
    }

    @Override
    public List<SlotDTO> findAll(){
        return slotRepository.findAll();
    }

    @Override
    public SlotDTO findSlotById(long slotId) throws Exception {
        return slotRepository.findSlotById(slotId);
    }

    @Override
    public List<SlotDTO> findVacantSlots(){
        return slotRepository.findVacantSlots();
    }
}
