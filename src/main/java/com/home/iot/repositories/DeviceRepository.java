package com.home.iot.repositories;

import com.home.iot.domains.Device;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class DeviceRepository {

    HashMap<Long, Device> devices = new HashMap<>();

    public Device save(Device device) {
        return devices.put(device.getDeviceId(), device);
    }

    public List<Device> findAll() {
        return devices.values().stream()
                .collect(Collectors.toList());
    }

    public Device findDeviceById(long slotId, long deviceId) throws Exception {
        return devices.values().stream()
                .filter(x -> x.getSlotId() == slotId && x.getDeviceId() == deviceId)
                .findAny()
                .get();
    }

    public Device addDeviceToSlot(long slotId, Device device) {
        devices.put(device.getDeviceId(), device);
        device.setSlotId(slotId);
        return device;
    }

    public Device updateDevice(Device device) {
        devices.put(device.getDeviceId(), device);
        return device;
    }

    public String deleteDevice(long deviceId) {
        devices.remove(deviceId);
        return String.format("Device %d is deleted",deviceId);
    }
}
