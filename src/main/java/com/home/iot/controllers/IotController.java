package com.home.iot.controllers;

import com.home.iot.domains.DeviceDTO;
import com.home.iot.services.IotService;
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
@Log4j2
public class IotController {

    @Autowired
    private IotService service;

    /**
     * The end-point used for turning ON / OFF a remote device
     *
     * @param deviceDTO
     * @param slotId
     * @param userAction
     * @return
     */
    @PutMapping(value = "/operateDevice/slots/{slotId}/devices/{deviceId}/{userAction}"
            , consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Operate a device specifying slotId, deviceId and action (ON / OFF)")
    public ResponseEntity<DeviceDTO> operateDevice (@RequestBody DeviceDTO deviceDTO,
                                                    @PathVariable("slotId") String slotId,
                                                    @PathVariable("deviceId") String deviceId,
                                                    @PathVariable("userAction") String userAction){
        return ResponseEntity.ok().body(service.operateDevice(deviceDTO, slotId, deviceId, userAction));
    }

    /**
     * The "Undo" feature to revert back (either intentionally or if user happens to make changes by mistake)
     * The Multi-level UNDO operation allows you to go back by N steps
     * The application will go back straight to the Nth step backward and all operations done
     * between N and current operation will be deleted
     *
     * @param undoCount
     * @return
     */
    @PutMapping(value = "/operateDevice/undoOperation/{undoCount}")
    @ApiOperation(value = "To undo Nth operation by specifying the undoCount")
    public ResponseEntity<String> undoLastNthOperation (
            @PathVariable("undoCount")
            @Min(value = 1, message = "id must be greater than or equal to 1")
            @Max(value = 10, message = "id must be lower than or equal to 10")
            String undoCount){
        return ResponseEntity.ok().body(service.undoLastNthOperation(Long.parseLong(undoCount)));
    }

    /**
     * The "Undo" feature to revert back (either intentionally or if user happens to make changes by mistake)
     * The Multi-level UNDO operation allows you to go back by N steps, restoring data step-by-step,
     * similar to replay mechanism
     *
     * @param undoCount
     * @return
     */
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
