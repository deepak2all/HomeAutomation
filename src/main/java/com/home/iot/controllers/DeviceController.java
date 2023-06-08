package com.home.iot.controllers;

import com.home.iot.domains.Device;
import com.home.iot.exceptions.IncorrectInputException;
import com.home.iot.services.DeviceService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("iot/api/v1")
@Validated
public class DeviceController {

    @Autowired
    private DeviceService service;

    @PostMapping(value = "/slots/devices/", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Device> addDevice(@RequestBody Device device){
        return ResponseEntity.ok().body(service.save(device));
    }

    @PostMapping(value = "/slots/{slotId}/devices/", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Device> addDeviceToSlot(@PathVariable("slotId") String slotId, @RequestBody Device device){
        long sId = Long.parseLong(slotId);
        return ResponseEntity.ok().body(service.addDeviceToSlot(sId, device));
    }

    @GetMapping("/slots/devices")
    public ResponseEntity<Collection<Device>> findAllDevices(){
        return ResponseEntity.ok().body(service.findAll());
    }

    @GetMapping("/slots/{slotId}/devices/{deviceId}")
    public ResponseEntity<Device> findDeviceById(
            @PathVariable("slotId") @Min(value = 1, message = "id must be greater than or equal to 1") @Max(value = 1000, message = "id must be lower than or equal to 1000") String slotId,
            @PathVariable("deviceId") @Min(value = 1, message = "id must be greater than or equal to 1") @Max(value = 1000, message = "id must be lower than or equal to 1000") String deviceId){
        try {
            return ResponseEntity.ok().body(service.findDeviceById(Long.parseLong(slotId), Long.parseLong(deviceId)));
        } catch (Exception e) {
            throw new IncorrectInputException("Incorrect input provided :: " + slotId);
        }
    }

    @PutMapping(value = "/slots/{slotId}/devices/{deviceId}", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Device> updateDevice(@PathVariable("slotId") String slotId, @RequestBody Device device){
        return ResponseEntity.ok().body(service.updateDevice(Long.parseLong(slotId), device));
    }
}
