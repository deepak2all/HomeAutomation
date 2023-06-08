package com.home.iot.controllers;

import com.home.iot.domains.Slot;
import com.home.iot.exceptions.IncorrectInputException;
import com.home.iot.services.SlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.util.Collection;

@RestController
@RequestMapping("iot/api/v1")
@Validated
public class SlotController {

    @Autowired
    private SlotService service;

    @PostMapping(value = "/slots", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Slot> addSlot(@RequestBody Slot slot){
        return ResponseEntity.ok().body(service.save(slot));
    }

    @PutMapping(value = "/slots/{slotId}", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Slot> updateSlot(@RequestBody Slot slot){
        return ResponseEntity.ok().body(service.update(slot));
    }

    @GetMapping("/slots")
    public ResponseEntity<Collection<Slot>> findAllSlots(){
        return ResponseEntity.ok().body(service.findAll());
    }

    @GetMapping("/slots/{slotId}")
    public ResponseEntity<Slot> findSlotById(
            @PathVariable("slotId") @Min(value = 1, message = "id must be greater than or equal to 1") @Max(value = 1000, message = "id must be lower than or equal to 1000") String slotId){
        try {
            long id = Long.parseLong(slotId);
            return ResponseEntity.ok().body(service.findSlotById(id));
        } catch (Exception e) {
            throw new IncorrectInputException("Incorrect input provided :: " + slotId);
        }
    }

    @GetMapping("/slots/vacantSlots")
    public ResponseEntity<Collection<Slot>> findVacantSlots(){
        return ResponseEntity.ok().body(service.findVacantSlots());
    }

}
