package com.home.iot.services;

import com.home.iot.domains.DeviceDTO;

import java.util.List;

public interface DeviceService {

	List<DeviceDTO> findAll();

	DeviceDTO findDeviceById(long slotId, long deviceId) throws Exception;

	// Add a device
	DeviceDTO save(DeviceDTO deviceDTO);

	// Update device
	DeviceDTO updateDevice(long slotId, DeviceDTO deviceDTO);

	// Delete a device
	String deleteDevice(long slotId, long deviceId);

	// Register a device
	DeviceDTO addDeviceToSlot(long slotId, DeviceDTO deviceDTO);

	// Unregister a device --> Removing the device to the slot

	// Remove device from slot

}
