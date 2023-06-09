package com.home.iot.util;

import org.springframework.beans.factory.annotation.Value;

public class ApplicationConstants {
    @Value("${executionStateRecorder.max-cache-size}")
    public static int maxCacheSize;

}
