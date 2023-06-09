package com.home.iot.controllers;

import com.home.iot.domains.Device;
import com.home.iot.exceptions.IncorrectInputException;
import com.home.iot.services.DeviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * Controller methods are the final destination point that a web request can reach.
 * After being invoked, the controller method starts to process the web request by
 * interacting with the service layer to complete the work that needs to be done.
 * This DeviceController is used to configure the IOT devices remotely
 */
@RestController
@RequestMapping("iot/api/v1")
@Validated
@Api(value = "Device Service", description = "To configure the IOT devices remotely")
public class DeviceController {

    @Autowired
    private DeviceService service;

    @PostMapping(value = "/slots/devices/", consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint to add a remote device")
    public ResponseEntity<Device> addDevice(@RequestBody Device device){
        return ResponseEntity.ok().body(service.save(device));
    }

    @PostMapping(value = "/slots/{slotId}/devices/", consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint to add a remote device / registering it to a slot (if slotId is not 0)")
    public ResponseEntity<Device> addDeviceToSlot(@PathVariable("slotId") String slotId, @RequestBody Device device){
        long sId = Long.parseLong(slotId);
        return ResponseEntity.ok().body(service.addDeviceToSlot(sId, device));
    }

    @GetMapping("/slots/devices")
    @ApiOperation(value = "Endpoint to get all the remote devices (Both registered and unregistered)")
    public ResponseEntity<Collection<Device>> findAllDevices(){
        return ResponseEntity.ok().body(service.findAll());
    }

    @GetMapping("/slots/{slotId}/devices/{deviceId}")
    @ApiOperation(value = "Endpoint to get a particular remote device by specifying it's id")
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
    @ApiOperation(value = "Endpoint to update a remote device by specifying the deviceId and slotId; " +
            "Specify sloId as 0 to unregister")
    public ResponseEntity<Device> updateDevice(@PathVariable("slotId") String slotId, @RequestBody Device device){
        return ResponseEntity.ok().body(service.updateDevice(Long.parseLong(slotId), device));
    }

    @DeleteMapping(value = "/slots/{slotId}/devices/{deviceId}")
    @ApiOperation(value = "Endpoint to delete a remote device by specifying the deviceId and slotId")
    public ResponseEntity<String> deleteDevice(@PathVariable("slotId") String slotId, @PathVariable("deviceId") String deviceId){
        return ResponseEntity.ok().body(service.deleteDevice(Long.parseLong(slotId), Long.parseLong(deviceId)));
    }
}
