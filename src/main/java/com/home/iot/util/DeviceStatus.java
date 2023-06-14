package com.home.iot.util;

public enum DeviceStatus {
    ON("ON"),
    OFF("OFF");

    private String status;

    DeviceStatus(String status) {
        this.status = status;
    }
}
