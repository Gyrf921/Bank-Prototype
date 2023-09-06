package com.bankprototype.deal.mapper;

import com.bankprototype.deal.repository.dao.Client;
import com.bankprototype.deal.repository.dao.jsonb.Passport;
import com.bankprototype.deal.web.dto.FinishRegistrationRequestDTO;
import com.bankprototype.deal.web.dto.LoanApplicationRequestDTO;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = EmploymentMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ClientMapper {


    @Mapping(target = "passport", source = "requestDTO")
    Client loanApplicationRequestDTOToClient(LoanApplicationRequestDTO requestDTO);

    static Passport mapPassport(LoanApplicationRequestDTO requestDTO) {
        return Passport.builder()
                .series(requestDTO.getPassportSeries())
                .number(requestDTO.getPassportNumber()).build();
    }


    @Mapping(target = "passport.issueDate", source = "requestFDTO.passportIssueDate")
    @Mapping(target = "passport.issueBranch", source = "requestFDTO.passportIssueBrach")
    Client updateClientToFinishRegistrationRequestDTO(FinishRegistrationRequestDTO requestFDTO, @MappingTarget Client client);


}
