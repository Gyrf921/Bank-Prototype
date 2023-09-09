package com.bankprototype.deal.mapper;

import com.bankprototype.deal.repository.dao.Client;
import com.bankprototype.deal.repository.dao.Credit;
import com.bankprototype.deal.web.dto.CreditDTO;
import com.bankprototype.deal.web.dto.FinishRegistrationRequestDTO;
import com.bankprototype.deal.web.dto.LoanOfferDTO;
import com.bankprototype.deal.web.dto.ScoringDataDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CreditMapper {

    @Mapping(target = "insuranceEnable", source = "creditDTO.isInsuranceEnabled")
    @Mapping(target = "salaryClient", source = "creditDTO.isSalaryClient")
    Credit creditDtoToCredit(CreditDTO creditDTO);

    @Mapping(target = "amount", source = "loanOfferDTO.requestedAmount")
    @Mapping(target = "term", source = "loanOfferDTO.term")
    @Mapping(target = "firstName", source = "client.firstName")
    @Mapping(target = "lastName", source = "client.lastName")
    @Mapping(target = "middleName", source = "client.middleName")
    @Mapping(target = "gender", source = "requestDTO.gender")
    @Mapping(target = "birthDate", source = "client.birthDate")
    @Mapping(target = "passportSeries", source = "client.passport.series")
    @Mapping(target = "passportNumber", source = "client.passport.number")
    @Mapping(target = "passportIssueDate", source = "requestDTO.passportIssueDate")
    @Mapping(target = "passportIssueBranch", source = "requestDTO.passportIssueBrach")
    @Mapping(target = "maritalStatus", source = "requestDTO.maritalStatus")
    @Mapping(target = "dependentAmount", source = "requestDTO.dependentAmount")
    @Mapping(target = "employment", source = "requestDTO.employment")
    @Mapping(target = "account", source = "requestDTO.account")
    @Mapping(target = "isInsuranceEnabled", source = "loanOfferDTO.isInsuranceEnabled")
    @Mapping(target = "isSalaryClient", source = "loanOfferDTO.isSalaryClient")
    ScoringDataDTO infoToScoringDataDTO(FinishRegistrationRequestDTO requestDTO, Client client, LoanOfferDTO loanOfferDTO);
}
