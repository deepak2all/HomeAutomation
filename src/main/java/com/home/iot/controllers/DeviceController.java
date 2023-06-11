package com.home.iot.controllers;

import com.home.iot.domains.DeviceDTO;
import com.home.iot.exceptions.IncorrectInputException;
import com.home.iot.services.DeviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.extern.log4j.Log4j2;
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
@Log4j2
public class DeviceController {

    @Autowired
    private DeviceService service;

    /**
     * Used to add a device (Although not assigned to any slot)
     * @param deviceDTO
     * @return
     */
    @PostMapping(value = "/slots/devices/", consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint to add a remote device")
    public ResponseEntity<DeviceDTO> addDevice(@RequestBody DeviceDTO deviceDTO){
        return ResponseEntity.ok().body(service.save(deviceDTO));
    }

    /**
     * New device added and assigned to the specified slot
     *
     * @param slotId
     * @param deviceDTO
     * @return
     */
    @PostMapping(value = "/slots/{slotId}/devices/", consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint to add a remote device and registering it to a slot (if slotId is not 0)")
    public ResponseEntity<DeviceDTO> addDeviceToSlot(@PathVariable("slotId") String slotId, @RequestBody DeviceDTO deviceDTO){
        long sId = Long.parseLong(slotId);
        try {
            return ResponseEntity.ok().body(service.addDeviceToSlot(sId, deviceDTO));
        } catch (Exception e) {
            String errorMsg = String.format("Incorrect slotId# %s provided", slotId);
            log.error(errorMsg);
            throw new IncorrectInputException(errorMsg);
        }
    }

    /**
     * Endpoint used to get all the remote devices along with their configurations
     *
     * @return
     */
    @GetMapping("/slots/devices")
    @ApiOperation(value = "Endpoint to get all the remote devices (Both registered and unregistered)")
    public ResponseEntity<Collection<DeviceDTO>> findAllDevices(){
        return ResponseEntity.ok().body(service.findAll());
    }

    /**
     * Endpoint for getting a particular device
     * @param slotId
     * @param deviceId
     * @return
     */
    @GetMapping("/slots/{slotId}/devices/{deviceId}")
    @ApiOperation(value = "Endpoint to get a particular remote device by specifying it's id")
    public ResponseEntity<DeviceDTO> findDeviceById(
            @PathVariable("slotId") @Min(value = 1, message = "id must be greater than or equal to 1") @Max(value = 1000, message = "id must be lower than or equal to 1000") String slotId,
            @PathVariable("deviceId") @Min(value = 1, message = "id must be greater than or equal to 1") @Max(value = 1000, message = "id must be lower than or equal to 1000") String deviceId){
        try {
            return ResponseEntity.ok().body(service.findDeviceById(Long.parseLong(slotId), Long.parseLong(deviceId)));
        } catch (Exception e) {
            String errorMsg = String.format("Incorrect slotId# %s and/or deviceID# %s provided", slotId, deviceId);
            log.error(errorMsg);
            throw new IncorrectInputException(errorMsg);
        }
    }

    /**
     * Endpoint to update a remote device by specifying the deviceId and slotId
     *
     * @param slotId
     * @param deviceDTO
     * @return
     */
    @PutMapping(value = "/slots/{slotId}/devices/{deviceId}", consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Endpoint to update a remote device by specifying the deviceId and slotId; " +
            "Specify sloId as 0 to unregister")
    public ResponseEntity<DeviceDTO> updateDevice(@PathVariable("slotId") String slotId,
                                                  @RequestBody DeviceDTO deviceDTO){
        try {
            return ResponseEntity.ok().body(service.updateDevice(Long.parseLong(slotId), deviceDTO));
        } catch (Exception e) {
            String errorMsg = String.format("Incorrect slotId# %s provided", slotId);
            log.error(errorMsg);
            throw new IncorrectInputException(errorMsg);
        }
    }

    /**
     * Endpoint to delete a remote device by specifying the deviceId and slotId
     * @param slotId
     * @param deviceId
     * @return
     */
    @DeleteMapping(value = "/slots/{slotId}/devices/{deviceId}")
    @ApiOperation(value = "Endpoint to delete a remote device by specifying the deviceId and slotId")
    public ResponseEntity<String> deleteDevice(@PathVariable("slotId") String slotId, @PathVariable("deviceId") String deviceId){
        try {
            return ResponseEntity.ok().body(service.deleteDevice(Long.parseLong(slotId), Long.parseLong(deviceId)));
        } catch (Exception e) {
            String errorMsg = String.format("Incorrect slotId# %s and/or deviceID# %s provided", slotId, deviceId);
            log.error(errorMsg);
            throw new IncorrectInputException(errorMsg);
        }
    }
}
