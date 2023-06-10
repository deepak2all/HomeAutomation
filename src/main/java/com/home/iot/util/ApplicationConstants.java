package com.home.iot.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class ApplicationConstants {
    @Value("${executionStateRecorder.max-cache-size: 10}")
    public static int maxCacheSize = 10;

}
