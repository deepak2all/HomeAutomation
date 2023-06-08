package com.home.iot.domains;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Slot {

	private long slotId;
	private String slotName;
	private String slotType; // Long or short or medium-sized slots

	private Device device;

	public Slot(long slotId, String slotName, String slotType) {
		this.slotId = slotId;
		this.slotName = slotName;
		this.slotType = slotType;
	}

	public void setDevice(Device device) {
		this.device = device;
	}
}
