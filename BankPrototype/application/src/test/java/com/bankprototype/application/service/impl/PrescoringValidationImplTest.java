package com.bankprototype.application.service.impl;

import com.bankprototype.application.exception.BirthdateException;
import com.bankprototype.application.web.dto.LoanApplicationRequestDTO;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class PrescoringValidationImplTest {

    @InjectMocks
    private PrescoringValidationImpl prescoringService;

    @Test
    void checkBirthdateValid() {
        EnhancedRandom enhancedRandom = EnhancedRandomBuilder.aNewEnhancedRandomBuilder().build();

        LoanApplicationRequestDTO requestDTO = enhancedRandom.nextObject(LoanApplicationRequestDTO.class);

        requestDTO.setBirthDate(LocalDate.of(2000, 7, 7));

        prescoringService.checkBirthdateValid(requestDTO);

    }

    @Test
    void checkBirthdateValid_BirthdateException() {
        EnhancedRandom enhancedRandom = EnhancedRandomBuilder.aNewEnhancedRandomBuilder().build();

        LoanApplicationRequestDTO requestDTO = enhancedRandom.nextObject(LoanApplicationRequestDTO.class);

        requestDTO.setBirthDate(LocalDate.of(2020, 7, 7));

        assertThrows(BirthdateException.class, () -> {
            prescoringService.checkBirthdateValid(requestDTO);
        });

    }
}