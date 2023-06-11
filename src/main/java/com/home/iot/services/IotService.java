package com.home.iot.services;

import com.home.iot.domains.DeviceDTO;

public interface IotService {
    DeviceDTO operateDevice(DeviceDTO deviceDTO, String slotId, String deviceId, String userAction);

    String undoLastNthOperation(long undoOperationCount);

    String undoLastXOperations(long undoOperationCount);
}
