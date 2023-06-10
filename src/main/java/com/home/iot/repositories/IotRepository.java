package com.home.iot.repositories;

import com.home.iot.domains.DeviceDTO;
import com.home.iot.util.ExecutionStateRecorder;
import org.springframework.stereotype.Repository;

import static com.home.iot.util.ApplicationConstants.maxCacheSize;

@Repository
public class IotRepository {

    public DeviceDTO operateDevice(String slotId, DeviceDTO deviceDTO) {
        // Save the data in cache
        insertData(new ExecutionStateRecorder(deviceDTO));
        return deviceDTO;
    }

    public String undoLastFewOperations(long undoOperationCount) {
        // Retrieve the data in cache
        return "success";
    }

    private void insertData(ExecutionStateRecorder recordedState) {
        captureUserActions(recordedState);
    }

    private void captureUserActions(ExecutionStateRecorder recordedState) {
        // Ability to add 10 items
        if (ExecutionStateRecorder.operationsRegister.size() < maxCacheSize) {
            //Simply add the data
            ExecutionStateRecorder.operationsRegister.offer(recordedState);
        } else {
            ExecutionStateRecorder.operationsRegister.poll();
            ExecutionStateRecorder.operationsRegister.offer(recordedState);
        }
    }
}
