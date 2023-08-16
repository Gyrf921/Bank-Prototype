package com.bankprototype.deal.service.impl;

import com.bankprototype.deal.repository.ApplicationRepository;
import com.bankprototype.deal.repository.dao.Application;
import com.bankprototype.deal.repository.dao.Client;
import com.bankprototype.deal.repository.dao.Credit;
import com.bankprototype.deal.repository.dao.enumfordao.CreditStatus;
import com.bankprototype.deal.mapper.CreditMapper;
import com.bankprototype.deal.repository.CreditRepository;
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

    private final CreditRepository creditRepository;

    private final ApplicationRepository applicationRepository;

    private final CreditMapper creditMapper;

    @Override
    public ScoringDataDTO createScoringDataDTO(FinishRegistrationRequestDTO requestDTO, Client client, LoanOfferDTO loanOfferDTO) {
        log.info("[createScoringDataDTO] >> requestDTO:{}, client: {}, loanOfferDTO: {}", requestDTO, client, loanOfferDTO);

        ScoringDataDTO scoringDataDTO = ScoringDataDTO.builder()
                .amount(loanOfferDTO.getRequestedAmount())
                .term(loanOfferDTO.getTerm())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .middleName(client.getMiddleName())
                .gender(requestDTO.getGender())
                .birthdate(client.getBirthDate())
                .passportSeries(client.getPassport().getSeries())
                .passportNumber(client.getPassport().getNumber())
                .passportIssueDate(requestDTO.getPassportIssueDate())
                .passportIssueBranch(requestDTO.getPassportIssueBrach())
                .maritalStatus(requestDTO.getMaritalStatus())
                .dependentAmount(requestDTO.getDependentAmount())
                .employment(requestDTO.getEmployment())
                .account(requestDTO.getAccount())
                .isInsuranceEnabled(loanOfferDTO.getIsInsuranceEnabled())
                .isSalaryClient(loanOfferDTO.getIsSalaryClient())
                .build();

        log.info("[createScoringDataDTO] << result: {}", scoringDataDTO);
        return scoringDataDTO;
    }

    @Override
    public Credit createCredit(CreditDTO creditDTO, Application application) {
        log.info("[createCredit] >> creditDTO: {}", creditDTO);

        Credit credit = creditMapper.creditDtoToCredit(creditDTO);
        credit.setCreditStatus(CreditStatus.CALCULATED.name());

        //Todo Хрень какая-то
        application.setCreditId(credit);
        applicationRepository.save(application);

        Credit savedCredit = creditRepository.save(credit);

        log.info("[createCredit] << result: {}", savedCredit);

        return savedCredit;
    }
}
