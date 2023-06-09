package com.home.iot.repositories;

import com.home.iot.domains.Device;
import com.home.iot.util.ApplicationConstants;
import com.home.iot.util.ExecutionStateRecorder;
import org.springframework.stereotype.Repository;

@Repository
public class IotRepository {

    public Device operateDevice(String slotId, Device device) {
        // Save the data in cache
        insertData(new ExecutionStateRecorder(device));
        return device;
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
        if (ExecutionStateRecorder.operationsRegister.size() < ApplicationConstants.maxCacheSize) {
            //Simply add the data
            ExecutionStateRecorder.operationsRegister.offer(recordedState);
        } else {
            ExecutionStateRecorder.operationsRegister.poll();
            ExecutionStateRecorder.operationsRegister.offer(recordedState);
        }
    }
}
