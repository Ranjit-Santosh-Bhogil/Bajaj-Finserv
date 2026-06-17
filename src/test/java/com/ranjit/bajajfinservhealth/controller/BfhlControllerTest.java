package com.ranjit.bajajfinservhealth.controller;

import com.ranjit.bajajfinservhealth.dto.BfhlRequest;
import com.ranjit.bajajfinservhealth.dto.BfhlResponse;
import com.ranjit.bajajfinservhealth.service.BfhlService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BfhlController.class)
class BfhlControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BfhlService bfhlService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testProcessBfhlSuccess() throws Exception {
        BfhlRequest request = new BfhlRequest();
        request.setData(Arrays.asList("A", "1", "22"));

        BfhlResponse response = BfhlResponse.builder()
                .isSuccess(true)
                .requestId("REQ-1001")
                .sum("23")
                .build();

        when(bfhlService.processData(any(), anyString())).thenReturn(response);

        mockMvc.perform(post("/bfhl")
                        .header("X-Request-Id", "REQ-1001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.is_success").value(true))
                .andExpect(jsonPath("$.request_id").value("REQ-1001"))
                .andExpect(jsonPath("$.sum").value("23"));
    }

    @Test
    void testProcessBfhlWithoutRequestId() throws Exception {
        BfhlRequest request = new BfhlRequest();
        request.setData(Arrays.asList("A", "1"));

        BfhlResponse response = BfhlResponse.builder()
                .isSuccess(true)
                .requestId("generated-id")
                .sum("1")
                .build();

        when(bfhlService.processData(any(), anyString())).thenReturn(response);

        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void testValidationError() throws Exception {
        BfhlRequest request = new BfhlRequest();
        // data is null

        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}