package com.home.iot.controllers;

import com.home.iot.domains.DeviceDTO;
import com.home.iot.services.DeviceService;
import com.home.iot.services.IotService;
import com.home.iot.services.SlotService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = IotController.class)
@WithMockUser
public class IotControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IotService iotService;

    @MockBean
    private DeviceService deviceService;

    @MockBean
    private SlotService slotService;

    DeviceDTO mockDeviceDTO = new DeviceDTO(1, "Living Room Light 1", "On/Off", "ON", "Living Room Light is Switched ON");

    String exampleJson = "{\"deviceId\":1,\"deviceName\":\"Living Room Light 1\",\"deviceType\":\"On/Off\",\"deviceStatus\":\"ON\",\"deviceInfo\":\"Living Room Light is Switched ON\",\"slotId\":1}";

    @Test
    public void operateToTurnOnADevice() throws Exception {

        Mockito.when(iotService.operateDevice(Mockito.any(DeviceDTO.class), Mockito.anyString(), Mockito.anyString())).thenReturn(mockDeviceDTO);

        MvcResult result = mockMvc.perform( MockMvcRequestBuilders
                        .put("/iot/api/v1/operateDevice/slots/{slotId}/devices/{deviceId}/{userAction}", 0, 1, "ON")
                        .content(exampleJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        String expected = "{\"deviceId\":1,\"deviceName\":\"Living Room Light 1\",\"deviceType\":\"On/Off\",\"deviceStatus\":\"ON\",\"deviceInfo\":\"Living Room Light is Switched ON\",\"slotId\":0}";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }

    @Test
    public void undoATurnOnOperation() throws Exception {

        Mockito.when(iotService.undoLastNthOperation(Mockito.anyLong())).thenReturn("success");

        mockMvc.perform( MockMvcRequestBuilders
                        .put("/iot/api/v1/operateDevice/undoOperation/{undoCount}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString("success")));

    }

}
