package com.ranjit.bajajfinservhealth.service;



import com.ranjit.bajajfinservhealth.dto.BfhlRequest;
import com.ranjit.bajajfinservhealth.dto.BfhlResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BfhlServiceImpl implements BfhlService {

    private static final Set<Character> VOWELS = Set.of('A', 'E', 'I', 'O', 'U');

    @Override
    public BfhlResponse processData(BfhlRequest request, String requestId) {
        long startTime = System.currentTimeMillis();

        List<String> inputData = request.getData();
        int totalElementsReceived = inputData != null ? inputData.size() : 0;

        // Step 1: Filter out null, empty, whitespace-only strings
        List<String> validElements = inputData.stream()
                .filter(this::isValidElement)
                .collect(Collectors.toList());

        int invalidElementsIgnored = totalElementsReceived - validElements.size();

        // Step 2: Check for duplicates and deduplicate
        Set<String> uniqueSet = new LinkedHashSet<>(validElements);
        boolean containsDuplicates = uniqueSet.size() < validElements.size();
        List<String> deduplicated = new ArrayList<>(uniqueSet);

        int validElementsProcessed = deduplicated.size();

        // Step 3: Categorize elements
        List<String> standaloneNumbers = new ArrayList<>();
        List<String> alphabetStrings = new ArrayList<>();
        List<String> specialCharacters = new ArrayList<>();

        // For extracted alphabets from alphanumeric
        List<String> extractedAlphabets = new ArrayList<>();

        for (String element : deduplicated) {
            String trimmed = element.trim();

            if (isStandaloneNumber(trimmed)) {
                standaloneNumbers.add(trimmed);
            } else if (isAlphabetOnly(trimmed)) {
                alphabetStrings.add(trimmed.toUpperCase());
            } else if (isSpecialCharacterOnly(trimmed)) {
                specialCharacters.add(trimmed);
            } else if (isAlphanumeric(trimmed)) {
                // Extract alphabets and numbers
                StringBuilder alphaPart = new StringBuilder();
                for (char c : trimmed.toCharArray()) {
                    if (Character.isLetter(c)) {
                        alphaPart.append(Character.toUpperCase(c));
                    }
                }
                if (alphaPart.length() > 0) {
                    extractedAlphabets.add(alphaPart.toString());
                }
            } else {
                // Mixed special + other - treat as special character if single char, else check
                if (trimmed.length() == 1) {
                    specialCharacters.add(trimmed);
                } else {
                    // For strings with special chars mixed with alphabets/numbers
                    // Extract alphabets
                    StringBuilder alphaPart = new StringBuilder();
                    for (char c : trimmed.toCharArray()) {
                        if (Character.isLetter(c)) {
                            alphaPart.append(Character.toUpperCase(c));
                        }
                    }
                    if (alphaPart.length() > 0) {
                        extractedAlphabets.add(alphaPart.toString());
                    }
                    // Check if any special chars
                    boolean hasSpecial = trimmed.chars().anyMatch(c -> !Character.isLetterOrDigit(c));
                    if (hasSpecial && trimmed.length() == 1) {
                        specialCharacters.add(trimmed);
                    }
                }
            }
        }

        // Combine original alphabet strings with extracted alphabets
        List<String> allAlphabets = new ArrayList<>();
        allAlphabets.addAll(alphabetStrings);
        allAlphabets.addAll(extractedAlphabets);

        // Process numbers
        List<BigDecimal> numericValues = standaloneNumbers.stream()
                .map(BigDecimal::new)
                .sorted()
                .collect(Collectors.toList());

        List<String> oddNumbers = numericValues.stream()
                .filter(n -> n.remainder(BigDecimal.valueOf(2)).abs().compareTo(BigDecimal.ZERO) != 0)
                .map(BigDecimal::toPlainString)
                .collect(Collectors.toList());

        List<String> evenNumbers = numericValues.stream()
                .filter(n -> n.remainder(BigDecimal.valueOf(2)).abs().compareTo(BigDecimal.ZERO) == 0)
                .map(BigDecimal::toPlainString)
                .collect(Collectors.toList());

        BigDecimal sum = numericValues.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        String largestNumber = numericValues.isEmpty() ? null :
                numericValues.get(numericValues.size() - 1).toPlainString();
        String smallestNumber = numericValues.isEmpty() ? null :
                numericValues.get(0).toPlainString();

        List<String> sortedNumbers = numericValues.stream()
                .map(BigDecimal::toPlainString)
                .collect(Collectors.toList());

        // Alphabet frequency
        Map<String, Integer> alphabetFrequency = new TreeMap<>();
        for (String alpha : allAlphabets) {
            for (char c : alpha.toCharArray()) {
                String key = String.valueOf(c);
                alphabetFrequency.merge(key, 1, Integer::sum);
            }
        }

        // Vowel and consonant count
        int vowelCount = 0;
        int consonantCount = 0;
        for (String alpha : allAlphabets) {
            for (char c : alpha.toCharArray()) {
                if (VOWELS.contains(c)) {
                    vowelCount++;
                } else {
                    consonantCount++;
                }
            }
        }

        // Longest and shortest alphabetic value
        String longestAlphabetic = allAlphabets.stream()
                .max(Comparator.comparingInt(String::length))
                .orElse(null);
        String shortestAlphabetic = allAlphabets.stream()
                .min(Comparator.comparingInt(String::length))
                .orElse(null);

        long processingTime = System.currentTimeMillis() - startTime;

        return BfhlResponse.builder()
                .isSuccess(true)
                .requestId(requestId)
                .oddNumbers(oddNumbers.isEmpty() ? null : oddNumbers)
                .evenNumbers(evenNumbers.isEmpty() ? null : evenNumbers)
                .alphabets(allAlphabets.isEmpty() ? null : allAlphabets)
                .specialCharacters(specialCharacters.isEmpty() ? null : specialCharacters)
                .sum(sum.toPlainString())
                .largestNumber(largestNumber)
                .smallestNumber(smallestNumber)
                .alphabetCount(allAlphabets.size())
                .numberCount(standaloneNumbers.size())
                .specialCharacterCount(specialCharacters.size())
                .containsDuplicates(containsDuplicates)
                .uniqueElementCount(validElementsProcessed)
                .sortedNumbers(sortedNumbers.isEmpty() ? null : sortedNumbers)
                .vowelCount(vowelCount > 0 ? vowelCount : null)
                .consonantCount(consonantCount > 0 ? consonantCount : null)
                .longestAlphabeticValue(longestAlphabetic)
                .shortestAlphabeticValue(shortestAlphabetic)
                .alphabetFrequency(alphabetFrequency.isEmpty() ? null : alphabetFrequency)
                .processingTimeMs(processingTime)
                .summary(BfhlResponse.Summary.builder()
                        .totalElementsReceived(totalElementsReceived)
                        .validElementsProcessed(validElementsProcessed)
                        .invalidElementsIgnored(invalidElementsIgnored)
                        .build())
                .build();
    }

    private boolean isValidElement(String element) {
        if (element == null) return false;
        return !element.trim().isEmpty();
    }

    private boolean isStandaloneNumber(String str) {
        if (str == null || str.isEmpty()) return false;
        String trimmed = str.trim();
        // Match integers, decimals, negative numbers
        return trimmed.matches("^-?\\d+(\\.\\d+)?$");
    }

    private boolean isAlphabetOnly(String str) {
        if (str == null || str.isEmpty()) return false;
        return str.chars().allMatch(Character::isLetter);
    }

    private boolean isSpecialCharacterOnly(String str) {
        if (str == null || str.isEmpty()) return false;
        return str.chars().allMatch(c -> !Character.isLetterOrDigit(c));
    }

    private boolean isAlphanumeric(String str) {
        if (str == null || str.isEmpty()) return false;
        boolean hasLetter = str.chars().anyMatch(Character::isLetter);
        boolean hasDigit = str.chars().anyMatch(Character::isDigit);
        boolean hasOnlyAlphaNum = str.chars().allMatch(Character::isLetterOrDigit);
        return hasLetter && hasDigit && hasOnlyAlphaNum;
    }
}
