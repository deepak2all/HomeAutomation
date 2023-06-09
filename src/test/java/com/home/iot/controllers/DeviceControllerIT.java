package com.home.iot.controllers;

import com.home.iot.HomeAutomationApplication;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = HomeAutomationApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DeviceControllerIT {

    @LocalServerPort
    private int port;

    TestRestTemplate restTemplate = new TestRestTemplate();

    HttpHeaders headers = new HttpHeaders();

    @Test
    public void testRetrieveDeviceInfo() throws JSONException {

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/iot/api/v1/slots/0/devices/1"),
                HttpMethod.GET, entity, String.class);

        String expected = "{\"deviceId\":1,\"deviceName\":\"Living Room Light 1\",\"deviceType\":\"On/Off\",\"deviceStatus\":\"ON\",\"deviceInfo\":\"Living Room Light is Switched ON\",\"slotId\":0}";

        JSONAssert.assertEquals(expected, response.getBody(), false);
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
