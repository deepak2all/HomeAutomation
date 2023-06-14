package com.home.iot.controllers;

import com.home.iot.domains.DeviceDTO;
import com.home.iot.services.DeviceService;
import com.home.iot.services.SlotService;
import com.home.iot.util.DeviceStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = DeviceController.class)
@WithMockUser
public class DeviceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeviceService deviceService;

    @MockBean
    private SlotService slotService;

    DeviceDTO mockDeviceDTO = new DeviceDTO(1, "Living Room Light 1", "On/Off", DeviceStatus.ON, "Living Room Light is Switched ON");

    String exampleJson = "{\"deviceId\":1,\"deviceName\":\"Living Room Light 1\",\"deviceType\":\"On/Off\",\"deviceStatus\":\"ON\",\"deviceInfo\":\"Living Room Light is Switched ON\",\"slotId\":0}";

    @Test
    public void retrieveDeviceDetails() throws Exception {

        Mockito.when(deviceService.findDeviceById(Mockito.anyLong(), Mockito.anyLong())).thenReturn(mockDeviceDTO);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/iot/api/v1/slots/0/devices/1")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        System.out.println(result.getResponse());
        String expected = "{\"deviceId\":1,\"deviceName\":\"Living Room Light 1\",\"deviceType\":\"On/Off\",\"deviceStatus\":\"ON\",\"deviceInfo\":\"Living Room Light is Switched ON\",\"slotId\":0}";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }

    @Test
    public void createDevice() throws Exception {

        // deviceService.addDeviceToSlot to respond back with mockDevice
        Mockito.when(deviceService.addDeviceToSlot(Mockito.anyLong(), Mockito.any(DeviceDTO.class))).thenReturn(mockDeviceDTO);

        // Send device as body to /iot/api/v1/slots/0/devices/
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/iot/api/v1/slots/0/devices/")
                .accept(MediaType.APPLICATION_JSON).content(exampleJson)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

    }

}
