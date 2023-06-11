package com.home.iot.services;

import com.home.iot.domains.DeviceDTO;
import com.home.iot.domains.SlotDTO;
import com.home.iot.exceptions.IncorrectInputException;
import com.home.iot.repositories.DeviceRepository;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DeviceServiceImpl is service layer is a layer in an application that facilitates communication between
 * the DeviceController and the persistence layer, DeviceRepository.
 * Additionally, business logic to configure devices, is stored in the service layer.
 */
@Service
@Log4j2
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private DeviceRepository deviceRepository;

    @Override
    public DeviceDTO save(DeviceDTO deviceDTO) {
        return deviceRepository.save(deviceDTO);
    }

    @Override
    public DeviceDTO updateDevice(long slotId, DeviceDTO deviceDTO) {
        SlotDTO origSlotDTO = getCorrespondingSlots(slotId);
        origSlotDTO.setDeviceDTO(deviceDTO);
        updateSlot(slotId, origSlotDTO);
        return deviceRepository.updateDevice(deviceDTO);
    }

    @SneakyThrows
    @Override
    public String deleteDevice(long slotId, long deviceId) {
        String infoMsg = String.format("Deleting device with slotId# %d and deviceID# %d ", slotId, deviceId);
        log.info(infoMsg);
        DeviceDTO deviceDTO = deviceRepository.findDeviceById(slotId, deviceId);
        if(deviceDTO.getSlotId() != 0) {
            SlotDTO origSlotDTO = getCorrespondingSlots(slotId);
            origSlotDTO.setDeviceDTO(null);
            updateSlot(slotId, origSlotDTO);
        }
        return deviceRepository.deleteDevice(deviceId);
    }

    @Override
    public List<DeviceDTO> findAll(){
        return deviceRepository.findAll();
    }

    @Override
    public DeviceDTO findDeviceById(long slotId, long deviceId) throws Exception {
        return deviceRepository.findDeviceById(slotId, deviceId);
    }

    @Override
    public DeviceDTO addDeviceToSlot(long slotId, DeviceDTO deviceDTO) {
        if(slotId == 0) {
            // Device added but not assigned to any slot
            return deviceRepository.addDeviceToSlot(slotId, deviceDTO);
        }
        // Use RestTemplate to get check the slot
        List<Long> emptySlotIds = getEmptySlots().stream().map(SlotDTO::getSlotId).collect(Collectors.toList());

        // If slot is found, assign the device
        if( emptySlotIds.contains(slotId) ) {
            SlotDTO origSlotDTO = getEmptySlots().stream()
                    .filter(s -> s.getSlotId() == slotId)
                    .findAny().get();
            origSlotDTO.setDeviceDTO(deviceDTO);
            updateSlot(slotId, origSlotDTO);
            return deviceRepository.addDeviceToSlot(slotId, deviceDTO);
        } else {
            String errorMsg = String.format("SlotId# %d provided is either incorrect or occupied", slotId );
            log.error(errorMsg);
            throw new IncorrectInputException(errorMsg);
        }
    }

    @SneakyThrows
    private void updateSlot(long slotId, SlotDTO updSlotDTO) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        URI uri = new URI("http://localhost:8500/iot/api/v1/slots/" + slotId);

        RequestEntity<SlotDTO> requestEntity = new RequestEntity<>(updSlotDTO, headers, HttpMethod.PUT, uri);
        ParameterizedTypeReference<SlotDTO> typeRef = new ParameterizedTypeReference<>() {};
        ResponseEntity<SlotDTO> responseEntity = restTemplate.exchange(requestEntity, typeRef);
    }

    public List<SlotDTO> getEmptySlots() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ParameterizedTypeReference<List<SlotDTO>>  parameterizedTypeReference = new ParameterizedTypeReference<>(){};

        ResponseEntity<List<SlotDTO>> exchange = restTemplate.exchange("http://localhost:8500/iot/api/v1/slots/vacantSlots", HttpMethod.GET, entity, parameterizedTypeReference);
        List<SlotDTO> vacantSlotDTOS = exchange.getBody();
        return vacantSlotDTOS;
    }

    public SlotDTO getCorrespondingSlots(long slotId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<SlotDTO> exchange = restTemplate.exchange("http://localhost:8500/iot/api/v1/slots/" + slotId, HttpMethod.GET, entity, SlotDTO.class);
        return exchange.getBody();
    }
}
