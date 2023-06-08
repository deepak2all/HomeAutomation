package com.home.iot.controllers;

import com.home.iot.domains.Device;
import com.home.iot.services.IotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("iot/api/v1")
public class IotController {

    @Autowired
    private IotService service;

    @PutMapping(value = "/operateDevice/slots/{slotId}/devices/{deviceId}/{userAction}"
            , consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Device> operateDevice (@RequestBody Device device,
                                                 @PathVariable("slotId") String slotId,
                                                 @PathVariable("userAction") String userAction){
        return ResponseEntity.ok().body(service.operateDevice(device, slotId, userAction));
    }

    @PutMapping(value = "/operateDevice/undoOperation/{undoCount}")
    public ResponseEntity<String> undoLastNthOperation (
                                                 @PathVariable("undoCount") String undoCount){
        return ResponseEntity.ok().body(service.undoLastNthOperation(Long.parseLong(undoCount)));
    }

    @PutMapping(value = "/operateDevice/undoOperations/{undoCount}")
    public ResponseEntity<String> undoLastNOperations (
            @PathVariable("undoCount") String undoCount){
        return ResponseEntity.ok().body(service.undoLastXOperations(Long.parseLong(undoCount)));
    }
}
