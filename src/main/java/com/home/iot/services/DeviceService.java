package com.home.iot.services;

import com.home.iot.domains.Device;

import java.util.List;

public interface DeviceService {

	List<Device> findAll();

	Device findDeviceById(long slotId, long deviceId) throws Exception;

	// Add a device
	Device save(Device device);

	// Update device
	Device updateDevice(long slotId, Device device);

	// Delete a device
	String deleteDevice(long slotId, long deviceId);

	// Register a device
	Device addDeviceToSlot(long slotId, Device device);

	// Unregister a device --> Removing the device to the slot

	// Remove device from slot

}
