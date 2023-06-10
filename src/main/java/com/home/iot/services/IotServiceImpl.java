package com.home.iot.services;

import com.home.iot.domains.DeviceDTO;
import com.home.iot.exceptions.IncorrectInputException;
import com.home.iot.repositories.IotRepository;
import com.home.iot.util.ExecutionStateRecorder;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
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
@Log4j2
public class IotServiceImpl  implements IotService{

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private IotRepository iotRepository;

    @Override
    public DeviceDTO operateDevice(DeviceDTO deviceDTO, String slotId, String userAction) {
        log.info(String.format("Device %d in slot %s is being turned %s remotely"
                , deviceDTO.getDeviceId(), slotId, userAction));

        if (userAction.equalsIgnoreCase("ON") || userAction.equalsIgnoreCase("OFF") ) {
            // Use RestTemplate to get check the slot
            DeviceDTO deviceDTOInDB = getCorrespondingDevice(slotId, deviceDTO.getDeviceId());

            // If device is found, update the device operation by modifying the status and description
            if( deviceDTOInDB != null ) {
                String description = userAction.equalsIgnoreCase("ON")
                        ? deviceDTO.getDeviceName() + " is ON" : deviceDTO.getDeviceName() + " is OFF";
                deviceDTOInDB.setDeviceStatus(userAction);
                deviceDTOInDB.setDeviceInfo(description);
                updateCorrespondingDevice(slotId, deviceDTOInDB);
                return iotRepository.operateDevice(slotId, deviceDTOInDB);
            } else {
                throw new IncorrectInputException("Slot Id provided :: " + slotId + " is either occupied or not present (invalid)");
            }
        } else {
            throw new IncorrectInputException("Specify the device state as 'ON' / 'OFF'");
        }
    }

    @Override
    public String undoLastNthOperation(long undoOperationCount) {
        DeviceDTO oldStateDeviceDTO = performUndoOperation(undoOperationCount);
        updateCorrespondingDevice(String.valueOf(oldStateDeviceDTO.getSlotId()), oldStateDeviceDTO);
        return "Success";
    }

    @Override
    public String undoLastXOperations(long undoOperationCount) {
        undoXOperations(undoOperationCount);
        return "Success";
    }

    @SneakyThrows
    private void updateCorrespondingDevice(String slotId, DeviceDTO updatedDeviceDTO) {
        log.info("Device Data updated " + updatedDeviceDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        URI uri = new URI("http://localhost:8500/iot/api/v1/slots/" + slotId + "/devices/" + updatedDeviceDTO.getDeviceId());

        RequestEntity<DeviceDTO> requestEntity = new RequestEntity<>(updatedDeviceDTO, headers, HttpMethod.PUT, uri);
        ParameterizedTypeReference<DeviceDTO> typeRef = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<DeviceDTO> responseEntity = restTemplate.exchange(requestEntity, typeRef);
    }

    public DeviceDTO getCorrespondingDevice(String slotId, long deviceId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Eg localhost:8500/iot/api/v1/slots/5/devices/10
        ResponseEntity<DeviceDTO> exchange = restTemplate.exchange("http://localhost:8500/iot/api/v1/slots/" + slotId + "/devices/" + deviceId, HttpMethod.GET, entity, DeviceDTO.class);
        DeviceDTO deviceDTOInDB = exchange.getBody();
        return deviceDTOInDB;
    }

    /**
     * This will undo only the Nth operation
     * All operations done after N should be erased
     * It's used for restoring a particular state
     * @param undoCount
     * @return
     */
    private DeviceDTO performUndoOperation(long undoCount) {
        DeviceDTO oldStateDeviceDTO = null;
        if(undoCount > ExecutionStateRecorder.operationsRegister.size() - 1) {
            throw new IncorrectInputException("Incorrect undo count specified :: " + undoCount + "" +
                    ", should be < " + ExecutionStateRecorder.operationsRegister.size());
        } else {
            ExecutionStateRecorder.operationsRegister.pollLast();
            for(long i=0; i<undoCount; i++) {
                ExecutionStateRecorder deviceState = ExecutionStateRecorder.operationsRegister.getLast();
                if(i+1 == undoCount) {
                    oldStateDeviceDTO = deviceState.getDevice();
                }
                // Removing the data of previous state as they are no longer needed
                ExecutionStateRecorder.operationsRegister.pollLast();
            }
        }
        return oldStateDeviceDTO;
    }

    /**
     * This will undo all operations until N
     * It's like going backward by N steps / replay operation
     * @param undoCount
     * @return
     */
    private DeviceDTO undoXOperations(long undoCount) {
        DeviceDTO oldStateDeviceDTO = null;
        log.info(String.format("Undo operation count is %d and cacheSize is %d", undoCount, ExecutionStateRecorder.operationsRegister.size()));
        if(undoCount > ExecutionStateRecorder.operationsRegister.size() - 1) {
            throw new IncorrectInputException("Incorrect undo count specified :: " + undoCount + "" +
                    ", should be <= " + ExecutionStateRecorder.operationsRegister.size());
        } else {
            // Removing the last state as it's no longer needed
            ExecutionStateRecorder.operationsRegister.pollLast();
            for(long i=0; i<undoCount; i++) {
                ExecutionStateRecorder deviceState = ExecutionStateRecorder.operationsRegister.getLast();
                oldStateDeviceDTO = deviceState.getDevice();
                updateCorrespondingDevice(String.valueOf(oldStateDeviceDTO.getSlotId()), oldStateDeviceDTO);
                // Removing the data from cache once the device's state is restored to previous state
                ExecutionStateRecorder.operationsRegister.pollLast();
            }
        }
        return oldStateDeviceDTO;
    }
}
