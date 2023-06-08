package com.home.iot.services;

import com.home.iot.domains.Device;
import com.home.iot.domains.Slot;
import com.home.iot.exceptions.IncorrectInputException;
import com.home.iot.repositories.DeviceRepository;
import lombok.SneakyThrows;
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
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private DeviceRepository deviceRepository;

    @Override
    public Device save(Device device) {
        return deviceRepository.save(device);
    }

    @Override
    public Device updateDevice(long slotId, Device device) {
        Slot origSlot = getCorrespondingSlots(slotId);
        origSlot.setDevice(device);
        updateSlot(slotId, origSlot);
        return deviceRepository.updateDevice(device);
    }

    @Override
    public void deleteById(long id) {

    }

    @Override
    public List<Device> findAll(){
        return deviceRepository.findAll();
    }

    @Override
    public Device findDeviceById(long slotId, long deviceId) throws Exception {
        return deviceRepository.findDeviceById(slotId, deviceId);
    }

    @Override
    public Device addDeviceToSlot(long slotId, Device device) {
        // Use RestTemplate to get check the slot
        List<Long> emptySlotIds = getEmptySlots().stream().map(Slot::getSlotId).collect(Collectors.toList());

        // If slot is found, assign the device
        if( emptySlotIds.contains(slotId) ) {
            Slot origSlot = getEmptySlots().stream()
                    .filter(s -> s.getSlotId() == slotId)
                    .findAny().get();
            origSlot.setDevice(device);
            updateSlot(slotId, origSlot);
            return deviceRepository.addDeviceToSlot(slotId, device);
        } else {
            throw new IncorrectInputException("Slot Id provided :: " + slotId + " is either occupied or not present (invalid)");
        }
    }

    @SneakyThrows
    private void updateSlot(long slotId, Slot updSlot) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        URI uri = new URI("http://localhost:8500/iot/api/v1/slots/" + slotId);

        RequestEntity<Slot> requestEntity = new RequestEntity<>(updSlot, headers, HttpMethod.PUT, uri);
        ParameterizedTypeReference<Slot> typeRef = new ParameterizedTypeReference<>() {};
        ResponseEntity<Slot> responseEntity = restTemplate.exchange(requestEntity, typeRef);
    }

    public List<Slot> getEmptySlots() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ParameterizedTypeReference<List<Slot>>  parameterizedTypeReference = new ParameterizedTypeReference<>(){};

        ResponseEntity<List<Slot>> exchange = restTemplate.exchange("http://localhost:8500/iot/api/v1/slots/vacantSlots", HttpMethod.GET, entity, parameterizedTypeReference);
        List<Slot> vacantSlots = exchange.getBody();
        return vacantSlots;
    }

    public Slot getCorrespondingSlots(long slotId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Slot> exchange = restTemplate.exchange("http://localhost:8500/iot/api/v1/slots/" + slotId, HttpMethod.GET, entity, Slot.class);
        return exchange.getBody();
    }
}
