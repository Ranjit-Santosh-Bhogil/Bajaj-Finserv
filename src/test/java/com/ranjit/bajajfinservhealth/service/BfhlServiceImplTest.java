package com.ranjit.bajajfinservhealth.service;


import com.ranjit.bajajfinservhealth.dto.BfhlRequest;
import com.ranjit.bajajfinservhealth.dto.BfhlResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BfhlServiceImplTest {

    private BfhlServiceImpl bfhlService;

    @BeforeEach
    void setUp() {
        bfhlService = new BfhlServiceImpl();
    }

    @Test
    @DisplayName("Example 1: Basic mixed input")
    void testExample1() {
        BfhlRequest request = new BfhlRequest();
        request.setData(Arrays.asList("A", "1", "22", "$", "B", "7"));

        BfhlResponse response = bfhlService.processData(request, "REQ-1001");

        assertTrue(response.isSuccess());
        assertEquals("REQ-1001", response.getRequestId());
        assertEquals(Arrays.asList("1", "7"), response.getOddNumbers());
        assertEquals(Arrays.asList("22"), response.getEvenNumbers());
        assertEquals(Arrays.asList("A", "B"), response.getAlphabets());
        assertEquals(Collections.singletonList("$"), response.getSpecialCharacters());
        assertEquals("30", response.getSum());
        assertEquals("22", response.getLargestNumber());
        assertEquals("1", response.getSmallestNumber());
        assertEquals(2, response.getAlphabetCount());
        assertEquals(3, response.getNumberCount());
        assertEquals(1, response.getSpecialCharacterCount());
        assertFalse(response.getContainsDuplicates());
        assertNotNull(response.getProcessingTimeMs());
    }

    @Test
    @DisplayName("Example 2: Alphanumeric strings")
    void testExample2() {
        BfhlRequest request = new BfhlRequest();
        request.setData(Arrays.asList("A1B2", "100", "#", "Test123", "Z", "55"));

        BfhlResponse response = bfhlService.processData(request, "REQ-1002");

        assertTrue(response.isSuccess());
        assertEquals("REQ-1002", response.getRequestId());
        assertEquals(Collections.singletonList("55"), response.getOddNumbers());
        assertEquals(Collections.singletonList("100"), response.getEvenNumbers());
        assertEquals(Arrays.asList("A", "B", "T", "E", "S", "T", "Z"), response.getAlphabets());
        assertEquals(Collections.singletonList("#"), response.getSpecialCharacters());
        assertEquals("155", response.getSum());
        assertEquals("100", response.getLargestNumber());
        assertEquals("55", response.getSmallestNumber());
        assertEquals(7, response.getAlphabetCount());
        assertEquals(2, response.getNumberCount());
        assertEquals(1, response.getSpecialCharacterCount());
        assertFalse(response.getContainsDuplicates());
    }

    @Test
    @DisplayName("Example 3: Duplicates and ignored values")
    void testExample3() {
        BfhlRequest request = new BfhlRequest();
        request.setData(Arrays.asList("10", "10", "A", "A", "", null, "&", "5"));

        BfhlResponse response = bfhlService.processData(request, "REQ-1003");

        assertTrue(response.isSuccess());
        assertEquals("REQ-1003", response.getRequestId());
        assertEquals(Collections.singletonList("5"), response.getOddNumbers());
        assertEquals(Collections.singletonList("10"), response.getEvenNumbers());
        assertEquals(Collections.singletonList("A"), response.getAlphabets());
        assertEquals(Collections.singletonList("&"), response.getSpecialCharacters());
        assertEquals("15", response.getSum());
        assertEquals("10", response.getLargestNumber());
        assertEquals("5", response.getSmallestNumber());
        assertEquals(1, response.getAlphabetCount());
        assertEquals(2, response.getNumberCount());
        assertEquals(1, response.getSpecialCharacterCount());
        assertTrue(response.getContainsDuplicates());
        assertEquals(4, response.getUniqueElementCount());
    }

    @Test
    @DisplayName("Example 4: Negative and decimal numbers")
    void testExample4() {
        BfhlRequest request = new BfhlRequest();
        request.setData(Arrays.asList("-10", "25.5", "-100.75", "B", "@", "5", "A9"));

        BfhlResponse response = bfhlService.processData(request, "REQ-1004");

        assertTrue(response.isSuccess());
        assertEquals("REQ-1004", response.getRequestId());
        assertEquals(Collections.singletonList("5"), response.getOddNumbers());
        assertEquals(Collections.singletonList("-10"), response.getEvenNumbers());
        assertEquals(Arrays.asList("B", "A"), response.getAlphabets());
        assertEquals(Collections.singletonList("@"), response.getSpecialCharacters());
        assertEquals("-80.25", response.getSum());
        assertEquals("25.5", response.getLargestNumber());
        assertEquals("-100.75", response.getSmallestNumber());
        assertEquals(2, response.getAlphabetCount());
        assertEquals(4, response.getNumberCount());
        assertEquals(1, response.getSpecialCharacterCount());
        assertFalse(response.getContainsDuplicates());
    }

    @Test
    @DisplayName("Example 5: Complex mixed input")
    void testExample5() {
        BfhlRequest request = new BfhlRequest();
        request.setData(Arrays.asList("ABC", "123", "A1B2", "$", "%", "-50", "0", "xyz", "", null, "999", "Test99", "&"));

        BfhlResponse response = bfhlService.processData(request, "REQ-1005");

        assertTrue(response.isSuccess());
        assertEquals("REQ-1005", response.getRequestId());
        assertEquals(Arrays.asList("123", "999"), response.getOddNumbers());
        assertEquals(Arrays.asList("-50", "0"), response.getEvenNumbers());
        assertEquals(Arrays.asList("ABC", "A", "B", "XYZ", "TEST"), response.getAlphabets());
        assertEquals(Arrays.asList("$", "%", "&"), response.getSpecialCharacters());
        assertEquals("1072", response.getSum());
        assertEquals("999", response.getLargestNumber());
        assertEquals("-50", response.getSmallestNumber());
        assertEquals(8, response.getAlphabetCount());
        assertEquals(4, response.getNumberCount());
        assertEquals(3, response.getSpecialCharacterCount());
        assertFalse(response.getContainsDuplicates());
        assertEquals(3, response.getVowelCount());
        assertEquals(8, response.getConsonantCount());
    }

    @Test
    @DisplayName("Empty data array")
    void testEmptyData() {
        BfhlRequest request = new BfhlRequest();
        request.setData(Collections.emptyList());

        BfhlResponse response = bfhlService.processData(request, "REQ-EMPTY");

        assertTrue(response.isSuccess());
        assertEquals("0", response.getSum());
        assertEquals(0, response.getAlphabetCount());
        assertEquals(0, response.getNumberCount());
        assertEquals(0, response.getSpecialCharacterCount());
        assertFalse(response.getContainsDuplicates());
    }

    @Test
    @DisplayName("Summary object validation")
    void testSummary() {
        BfhlRequest request = new BfhlRequest();
        request.setData(Arrays.asList("10", "10", "A", "", null, "5"));

        BfhlResponse response = bfhlService.processData(request, "REQ-SUM");

        assertNotNull(response.getSummary());
        assertEquals(6, response.getSummary().getTotalElementsReceived());
        assertEquals(3, response.getSummary().getValidElementsProcessed());
        assertEquals(3, response.getSummary().getInvalidElementsIgnored());
    }

    @Test
    @DisplayName("Alphabet frequency calculation")
    void testAlphabetFrequency() {
        BfhlRequest request = new BfhlRequest();
        request.setData(Arrays.asList("AABBC", "A", "B"));

        BfhlResponse response = bfhlService.processData(request, "REQ-FREQ");

        assertNotNull(response.getAlphabetFrequency());
        assertEquals(3, response.getAlphabetFrequency().get("A"));
        assertEquals(3, response.getAlphabetFrequency().get("B"));
        assertEquals(1, response.getAlphabetFrequency().get("C"));
    }

    @Test
    @DisplayName("Sorted numbers validation")
    void testSortedNumbers() {
        BfhlRequest request = new BfhlRequest();
        request.setData(Arrays.asList("5", "-10", "3.5", "0"));

        BfhlResponse response = bfhlService.processData(request, "REQ-SORT");

        assertEquals(Arrays.asList("-10", "0", "3.5", "5"), response.getSortedNumbers());
    }

    @Test
    @DisplayName("Longest and shortest alphabetic values")
    void testLongestShortestAlphabetic() {
        BfhlRequest request = new BfhlRequest();
        request.setData(Arrays.asList("HELLO", "A", "WORLD", "XY"));

        BfhlResponse response = bfhlService.processData(request, "REQ-LEN");

        assertEquals("HELLO", response.getLongestAlphabeticValue());
        assertEquals("A", response.getShortestAlphabeticValue());
    }

    @Test
    @DisplayName("Vowel count with mixed case")
    void testVowelCount() {
        BfhlRequest request = new BfhlRequest();
        request.setData(Arrays.asList("Hello", "World", "AEIOU"));

        BfhlResponse response = bfhlService.processData(request, "REQ-VOWEL");

        assertEquals(5, response.getVowelCount()); // E,O,A,E,I,O,U = 7... wait
        // H,e,l,l,o = 2 vowels (e,o)
        // W,o,r,l,d = 1 vowel (o)
        // A,E,I,O,U = 5 vowels
        // Total = 8
        assertEquals(8, response.getVowelCount());
        assertEquals(7, response.getConsonantCount());
    }
}
