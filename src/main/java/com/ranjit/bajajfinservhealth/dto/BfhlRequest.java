package com.ranjit.bajajfinservhealth.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class BfhlRequest {

    @NotNull(message = "Data array cannot be null")
    @Valid
    private List<String> data;
}