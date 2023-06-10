package com.home.iot.domains;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Data
@NoArgsConstructor
public class DeviceDTO {

	@Min(1)
	@Max(1000)
	private long deviceId;

	@NotEmpty(message = "Device Name is required")
	private String deviceName;

	@NotEmpty(message = "Device Type is required")
	private String deviceType; // OnOff type / Rotary switch

	@NotEmpty(message = "Device Status is required")
	private String deviceStatus;

	@NotEmpty(message = "Device Info is required")
	private String deviceInfo;

	@NotEmpty(message = "Slot id is required")
	private long slotId;

	public DeviceDTO(long deviceId, String deviceName, String deviceType, String deviceStatus, String deviceInfo) {
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
