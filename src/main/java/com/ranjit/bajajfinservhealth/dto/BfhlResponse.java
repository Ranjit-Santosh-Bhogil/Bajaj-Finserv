package com.ranjit.bajajfinservhealth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BfhlResponse {

    @JsonProperty("is_success")
    private boolean isSuccess;

    @JsonProperty("request_id")
    private String requestId;

    @JsonProperty("odd_numbers")
    private List<String> oddNumbers;

    @JsonProperty("even_numbers")
    private List<String> evenNumbers;

    private List<String> alphabets;

    @JsonProperty("special_characters")
    private List<String> specialCharacters;

    private String sum;

    @JsonProperty("largest_number")
    private String largestNumber;

    @JsonProperty("smallest_number")
    private String smallestNumber;

    @JsonProperty("alphabet_count")
    private Integer alphabetCount;

    @JsonProperty("number_count")
    private Integer numberCount;

    @JsonProperty("special_character_count")
    private Integer specialCharacterCount;

    @JsonProperty("contains_duplicates")
    private Boolean containsDuplicates;

    @JsonProperty("unique_element_count")
    private Integer uniqueElementCount;

    @JsonProperty("sorted_numbers")
    private List<String> sortedNumbers;

    @JsonProperty("vowel_count")
    private Integer vowelCount;

    @JsonProperty("consonant_count")
    private Integer consonantCount;

    @JsonProperty("longest_alphabetic_value")
    private String longestAlphabeticValue;

    @JsonProperty("shortest_alphabetic_value")
    private String shortestAlphabeticValue;

    @JsonProperty("alphabet_frequency")
    private Map<String, Integer> alphabetFrequency;

    @JsonProperty("processing_time_ms")
    private Long processingTimeMs;

    private Summary summary;

    @Data
    @Builder
    public static class Summary {
        @JsonProperty("total_elements_received")
        private Integer totalElementsReceived;

        @JsonProperty("valid_elements_processed")
        private Integer validElementsProcessed;

        @JsonProperty("invalid_elements_ignored")
        private Integer invalidElementsIgnored;
    }
}
