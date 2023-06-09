package com.home.iot.util;

import com.home.iot.domains.Device;

import java.util.ArrayDeque;
import java.util.Deque;

public class ExecutionStateRecorder {

    public static Deque<ExecutionStateRecorder> operationsRegister = new ArrayDeque<>(ApplicationConstants.maxCacheSize);

    private Device device; //Remote device that needs to be controlled

    public ExecutionStateRecorder(Device device) {
        super();
        this.device = device;
    }

    public Device getDevice() {
        return device;
    }

    @Override
    public String toString() {
        return "DeviceStateRecorder{" +
                "device='{'" +
                    "  deviceId=" + device.getDeviceId() + '\'' +
                    ", deviceName='" + device.getDeviceName() + '\'' +
                    ", deviceType='" + device.getDeviceType() + '\'' +
                    ", deviceStatus='" + device.getDeviceStatus() + '\'' +
                    ", deviceInfo='" + device.getDeviceInfo() + '\'' +
                    ", slotId=" + device.getSlotId() + '\'' +
                '}';
    }
}
