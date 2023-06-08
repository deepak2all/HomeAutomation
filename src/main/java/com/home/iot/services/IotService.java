package com.home.iot.services;

import com.home.iot.domains.Device;
import com.home.iot.domains.Slot;

import java.util.List;

public interface IotService {
    Device operateDevice(Device device, String slotId, String userAction);

    String undoLastNthOperation(long undoOperationCount);

    String undoLastXOperations(long undoOperationCount);
}
