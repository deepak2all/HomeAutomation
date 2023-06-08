package com.home.iot.services;

import com.home.iot.domains.Device;
import com.home.iot.exceptions.IncorrectInputException;
import com.home.iot.repositories.IotRepository;
import com.home.iot.util.ExecutionStateRecorder;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Arrays;

/**
 * IotServiceImpl is service layer is a layer in an application that facilitates communication between
 * the IotController and the persistence layer, IotRepository.
 * Additionally, business logic to operate IOT devices, is stored in the service layer.
 */
@Service
public class IotServiceImpl  implements IotService{

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private IotRepository iotRepository;

    @Override
    public Device operateDevice(Device device, String slotId, String userAction) {
        // Use RestTemplate to get check the slot
        Device deviceInDB = getCorrespondingDevice(slotId, device.getDeviceId());

        // If device is found, update the device operation by modifying the status and description
        if( deviceInDB != null ) {
            String description = userAction.equalsIgnoreCase("ON")
                    ? device.getDeviceName() + " is ON" : device.getDeviceName() + " is OFF";
            deviceInDB.setDeviceStatus(userAction);
            deviceInDB.setDeviceInfo(description);
            updateCorrespondingDevice(slotId, deviceInDB);
            return iotRepository.operateDevice(slotId, device);
        } else {
            throw new IncorrectInputException("Slot Id provided :: " + slotId + " is either occupied or not present (invalid)");
        }
    }

    @Override
    public String undoLastNthOperation(long undoOperationCount) {
        Device oldStateDevice = performUndoOperation(undoOperationCount);
        updateCorrespondingDevice(String.valueOf(oldStateDevice.getSlotId()), oldStateDevice);
        return "Success";
    }

    @Override
    public String undoLastXOperations(long undoOperationCount) {
        undoXOperations(undoOperationCount);
        return "Success";
    }

    @SneakyThrows
    private void updateCorrespondingDevice(String slotId, Device updatedDevice) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        URI uri = new URI("http://localhost:8500/iot/api/v1/slots/" + slotId + "/devices/" + updatedDevice.getDeviceId());

        RequestEntity<Device> requestEntity = new RequestEntity<>(updatedDevice, headers, HttpMethod.PUT, uri);
        ResponseEntity<Device> responseEntity = restTemplate.exchange(requestEntity, Device.class);
    }

    public Device getCorrespondingDevice(String slotId, long deviceId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Eg localhost:8500/iot/api/v1/slots/5/devices/10
        ResponseEntity<Device> exchange = restTemplate.exchange("http://localhost:8500/iot/api/v1/slots/" + slotId + "/devices/" + deviceId, HttpMethod.GET, entity, Device.class);
        Device deviceInDB = exchange.getBody();
        return deviceInDB;
    }

    private Device performUndoOperation(long undoCount) {
        Device oldStateDevice = null;
        if(undoCount > ExecutionStateRecorder.operationsRegister.size()) {
            throw new IncorrectInputException("Incorrect undo count specified :: " + undoCount + "" +
                    ", should be <= " + ExecutionStateRecorder.operationsRegister.size());
        } else {
            for(long i=0; i<undoCount; i++) {
                ExecutionStateRecorder deviceState = ExecutionStateRecorder.operationsRegister.getLast();
                if(i+1 == undoCount) {
                    oldStateDevice = deviceState.getDevice();
                }
                ExecutionStateRecorder.operationsRegister.pollLast();
            }
        }
        return oldStateDevice;
    }

    private Device undoXOperations(long undoCount) {
        Device oldStateDevice = null;
        if(undoCount > ExecutionStateRecorder.operationsRegister.size()) {
            throw new IncorrectInputException("Incorrect undo count specified :: " + undoCount + "" +
                    ", should be <= " + ExecutionStateRecorder.operationsRegister.size());
        } else {
            for(long i=0; i<undoCount; i++) {
                ExecutionStateRecorder deviceState = ExecutionStateRecorder.operationsRegister.getLast();
                oldStateDevice = deviceState.getDevice();
                updateCorrespondingDevice(String.valueOf(oldStateDevice.getSlotId()), oldStateDevice);
                ExecutionStateRecorder.operationsRegister.pollLast();
            }
        }
        return oldStateDevice;
    }
}
