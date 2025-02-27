package com.bankprototype.deal.service.impl;

import com.bankprototype.deal.dao.mapper.CreditMapper;
import com.bankprototype.deal.repository.ApplicationRepository;
import com.bankprototype.deal.dao.Application;
import com.bankprototype.deal.dao.Client;
import com.bankprototype.deal.dao.Credit;
import com.bankprototype.deal.dao.enumfordao.CreditStatus;
import com.bankprototype.deal.service.CreditService;
import com.bankprototype.deal.web.dto.CreditDTO;
import com.bankprototype.deal.web.dto.FinishRegistrationRequestDTO;
import com.bankprototype.deal.web.dto.LoanOfferDTO;
import com.bankprototype.deal.web.dto.ScoringDataDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreditServiceImpl implements CreditService {

    private final ApplicationRepository applicationRepository;

    private final CreditMapper creditMapper;

    @Override
    public ScoringDataDTO createScoringDataDTO(FinishRegistrationRequestDTO requestDTO, Client client, LoanOfferDTO loanOfferDTO) {
        log.info("[createScoringDataDTO] >> requestDTO:{}, client: {}, loanOfferDTO: {}", requestDTO, client, loanOfferDTO);

        ScoringDataDTO scoringDataDTO = creditMapper.infoToScoringDataDTO(requestDTO, client, loanOfferDTO);

        log.info("[createScoringDataDTO] << result: {}", scoringDataDTO);
        return scoringDataDTO;
    }

    @Override
    public Credit createCredit(CreditDTO creditDTO, Application application) {
        log.info("[createCredit] >> creditDTO: {}", creditDTO);

        Credit credit = creditMapper.creditDtoToCredit(creditDTO);
        credit.setCreditStatus(CreditStatus.CALCULATED);

        application.setCreditId(credit);
        applicationRepository.save(application);

        log.info("[createCredit] << result: {}", credit);

        return credit;
    }
}
