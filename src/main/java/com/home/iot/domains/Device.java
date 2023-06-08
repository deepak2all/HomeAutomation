package com.home.iot.domains;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Device {

	private long deviceId;
	private String deviceName;
	private String deviceType; // OnOff type / Rotary switch
	private String deviceStatus;
	private String deviceInfo;

	private long slotId;

	public Device(long deviceId, String deviceName, String deviceType, String deviceStatus, String deviceInfo) {
		this.deviceId = deviceId;
		this.deviceName = deviceName;
		this.deviceType = deviceType;
		this.deviceStatus = deviceStatus;
		this.deviceInfo = deviceInfo;
	}

	public void setSlotId(long slotId) {
		this.slotId = slotId;
	}

	public void setDeviceStatus(String deviceStatus) {
		this.deviceStatus = deviceStatus;
	}

	public void setDeviceInfo(String deviceInfo) {
		this.deviceInfo = deviceInfo;
	}

	@Override
	public String toString() {
		return "Device{" +
				"deviceId=" + deviceId +
				", deviceName='" + deviceName + '\'' +
				", deviceType='" + deviceType + '\'' +
				", deviceStatus='" + deviceStatus + '\'' +
				", deviceInfo='" + deviceInfo + '\'' +
				", slotId=" + slotId +
				'}';
	}
}
