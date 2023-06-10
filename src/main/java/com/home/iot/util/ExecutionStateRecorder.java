package com.home.iot.util;

import com.home.iot.domains.DeviceDTO;

import java.util.ArrayDeque;
import java.util.Deque;

public class ExecutionStateRecorder {

    public static Deque<ExecutionStateRecorder> operationsRegister = new ArrayDeque<>(ApplicationConstants.maxCacheSize);

    private DeviceDTO deviceDTO; //Remote device that needs to be controlled

    public ExecutionStateRecorder(DeviceDTO deviceDTO) {
        super();
        this.deviceDTO = deviceDTO;
    }

    public DeviceDTO getDevice() {
        return deviceDTO;
    }

    @Override
    public String toString() {
        return "DeviceStateRecorder{" +
                "device='{'" +
                    "  deviceId=" + deviceDTO.getDeviceId() + '\'' +
                    ", deviceName='" + deviceDTO.getDeviceName() + '\'' +
                    ", deviceType='" + deviceDTO.getDeviceType() + '\'' +
                    ", deviceStatus='" + deviceDTO.getDeviceStatus() + '\'' +
                    ", deviceInfo='" + deviceDTO.getDeviceInfo() + '\'' +
                    ", slotId=" + deviceDTO.getSlotId() + '\'' +
                '}';
    }
}
