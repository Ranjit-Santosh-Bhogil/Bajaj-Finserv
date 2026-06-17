package com.ranjit.bajajfinservhealth.controller;

import com.ranjit.bajajfinservhealth.dto.BfhlRequest;
import com.ranjit.bajajfinservhealth.dto.BfhlResponse;
import com.ranjit.bajajfinservhealth.service.BfhlService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BfhlController {

    private static final String REQUEST_ID_HEADER = "X-Request-Id";

    private final BfhlService bfhlService;

    @PostMapping("/bfhl")
    public ResponseEntity<BfhlResponse> processBfhl(
            @Valid @RequestBody BfhlRequest request,
            @RequestHeader(value = REQUEST_ID_HEADER, required = false) String requestId) {

        String finalRequestId = requestId != null ? requestId : UUID.randomUUID().toString();
        log.info("Processing BFHL request with ID: {}", finalRequestId);

        BfhlResponse response = bfhlService.processData(request, finalRequestId);
        return ResponseEntity.ok(response);
    }
}
