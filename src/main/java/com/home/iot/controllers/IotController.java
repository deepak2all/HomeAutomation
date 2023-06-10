package com.home.iot.controllers;

import com.home.iot.domains.DeviceDTO;
import com.home.iot.services.IotService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Controller methods are the final destination point that a web request can reach.
 * After being invoked, the controller method starts to process the web request by
 * interacting with the service layer to complete the work that needs to be done.
 * This IotController is used to operate the IOT devices remotely
 */
@RestController
@RequestMapping("iot/api/v1")
@Validated
@Api(value = "Iot Service", description = "To operate the IOT devices remotely")
public class IotController {

    @Autowired
    private IotService service;

    @PutMapping(value = "/operateDevice/slots/{slotId}/devices/{deviceId}/{userAction}"
            , consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Operate a device specifying slotId, deviceId and action (ON / OFF)")
    public ResponseEntity<DeviceDTO> operateDevice (@RequestBody DeviceDTO deviceDTO,
                                                    @PathVariable("slotId") String slotId,
                                                    @PathVariable("userAction") String userAction){
        return ResponseEntity.ok().body(service.operateDevice(deviceDTO, slotId, userAction));
    }

    @PutMapping(value = "/operateDevice/undoOperation/{undoCount}")
    @ApiOperation(value = "To undo Nth operation by specifying the undoCount")
    public ResponseEntity<String> undoLastNthOperation (
            @PathVariable("undoCount")
            @Min(value = 1, message = "id must be greater than or equal to 1")
            @Max(value = 10, message = "id must be lower than or equal to 10")
            String undoCount){
        return ResponseEntity.ok().body(service.undoLastNthOperation(Long.parseLong(undoCount)));
    }

    @PutMapping(value = "/operateDevice/undoOperations/{undoCount}")
    @ApiOperation(value = "To undo N operations by specifying the undoCount")
    public ResponseEntity<String> undoLastXOperations (
            @PathVariable("undoCount")
            @Min(value = 1, message = "id must be greater than or equal to 1")
            @Max(value = 10, message = "id must be lower than or equal to 10")
            String undoCount){
        return ResponseEntity.ok().body(service.undoLastXOperations(Long.parseLong(undoCount)));
    }
}
