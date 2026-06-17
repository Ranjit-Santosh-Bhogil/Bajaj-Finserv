package com.ranjit.bajajfinservhealth.service;



import com.ranjit.bajajfinservhealth.dto.BfhlRequest;
import com.ranjit.bajajfinservhealth.dto.BfhlResponse;

public interface BfhlService {
    BfhlResponse processData(BfhlRequest request, String requestId);
}