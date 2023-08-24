package com.bankprototype.application.service;

import com.bankprototype.application.web.dto.LoanApplicationRequestDTO;

public interface PrescoringValidation {

    /**
     * Проверяет правильность параметра Birthdate LoanApplicationRequestDTO
     *
     * @param requestDTO - Вводные данные для проверки (LoanApplicationRequestDTO)
     */
    void checkBirthdateValid(LoanApplicationRequestDTO requestDTO);


}

