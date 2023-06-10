package com.home.iot.repositories;

import com.home.iot.domains.DeviceDTO;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class DeviceRepository {

    HashMap<Long, DeviceDTO> devices = new HashMap<>();

    public DeviceDTO save(DeviceDTO deviceDTO) {
        return devices.put(deviceDTO.getDeviceId(), deviceDTO);
    }

    public List<DeviceDTO> findAll() {
        return devices.values().stream()
                .collect(Collectors.toList());
    }

    public DeviceDTO findDeviceById(long slotId, long deviceId) throws Exception {
        return devices.values().stream()
                .filter(x -> x.getSlotId() == slotId && x.getDeviceId() == deviceId)
                .findAny()
                .get();
    }

    public DeviceDTO addDeviceToSlot(long slotId, DeviceDTO deviceDTO) {
        devices.put(deviceDTO.getDeviceId(), deviceDTO);
        deviceDTO.setSlotId(slotId);
        return deviceDTO;
    }

    public DeviceDTO updateDevice(DeviceDTO deviceDTO) {
        devices.put(deviceDTO.getDeviceId(), deviceDTO);
        return deviceDTO;
    }

    public String deleteDevice(long deviceId) {
        devices.remove(deviceId);
        return String.format("Device %d is deleted",deviceId);
    }
}
