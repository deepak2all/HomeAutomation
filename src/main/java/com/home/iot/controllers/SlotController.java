package com.home.iot.controllers;

import com.home.iot.domains.SlotDTO;
import com.home.iot.exceptions.IncorrectInputException;
import com.home.iot.services.SlotService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.util.Collection;

/**
 * Controller methods are the final destination point that a web request can reach.
 * After being invoked, the controller method starts to process the web request by
 * interacting with the service layer to complete the work that needs to be done.
 * This SlotController is used to configure the slots remotely
 */
@RestController
@RequestMapping("iot/api/v1")
@Validated
@Api(value = "Slot Service", description = "To configure the slots remotely")
@Log4j2
public class SlotController {

    @Autowired
    private SlotService service;

    /**
     * Endpoint to add a slot
     * @param slotDTO
     * @return
     */
    @PostMapping(value = "/slots", consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint to add a slot")
    public ResponseEntity<SlotDTO> addSlot(@RequestBody SlotDTO slotDTO){
        return ResponseEntity.ok().body(service.save(slotDTO));
    }

    /**
     * Endpoint to update a remote slot by specifying the slotId
     * @param slotDTO
     * @return
     */
    @PutMapping(value = "/slots/{slotId}", consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint to update a particular slot specifying the slotId")
    public ResponseEntity<SlotDTO> updateSlot(@RequestBody SlotDTO slotDTO){
        return ResponseEntity.ok().body(service.update(slotDTO));
    }

    /**
     * Endpoint to get all the slots along with the configurations
     * @return
     */
    @GetMapping("/slots")
    @ApiOperation(value = "Endpoint to get all the slots' info")
    public ResponseEntity<Collection<SlotDTO>> findAllSlots(){
        return ResponseEntity.ok().body(service.findAll());
    }

    @GetMapping("/slots/{slotId}")
    @ApiOperation(value = "Endpoint to get a particular slot's info by specifying it's slotId")
    public ResponseEntity<SlotDTO> findSlotById(
            @PathVariable("slotId") @Min(value = 1, message = "id must be greater than or equal to 1") @Max(value = 1000, message = "id must be lower than or equal to 1000") String slotId){
        try {
            long id = Long.parseLong(slotId);
            return ResponseEntity.ok().body(service.findSlotById(id));
        } catch (Exception e) {
            String errorMsg = String.format("Incorrect slotId# %s provided", slotId);
            log.error(errorMsg);
            throw new IncorrectInputException(errorMsg);
        }
    }

    @DeleteMapping("/slots/{slotId}")
    @ApiOperation(value = "Endpoint to get a delete a particular slot (only if it's empty)")
    public ResponseEntity<String> deleteSlotById(
            @PathVariable("slotId") @Min(value = 1, message = "id must be greater than or equal to 1") @Max(value = 1000, message = "id must be lower than or equal to 1000") String slotId){
        try {
            long id = Long.parseLong(slotId);
            return ResponseEntity.ok().body(service.deleteSlot(id));
        } catch (Exception e) {
            throw new IncorrectInputException("Either Incorrect input provided Or slot not vacant :: " + slotId);
        }
    }

    @GetMapping("/slots/vacantSlots")
    @ApiOperation(value = "Endpoint to get all the vacant slots' info")
    public ResponseEntity<Collection<SlotDTO>> findVacantSlots(){
        return ResponseEntity.ok().body(service.findVacantSlots());
    }

}
