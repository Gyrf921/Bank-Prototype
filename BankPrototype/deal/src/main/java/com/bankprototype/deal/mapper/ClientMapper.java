package com.bankprototype.deal.mapper;

import com.bankprototype.deal.repository.dao.Client;
import com.bankprototype.deal.repository.dao.Credit;
import com.bankprototype.deal.repository.dao.jsonb.Passport;
import com.bankprototype.deal.web.dto.CreditDTO;
import com.bankprototype.deal.web.dto.FinishRegistrationRequestDTO;
import com.bankprototype.deal.web.dto.LoanApplicationRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = EmploymentMapper.class)
public interface ClientMapper {

    @Mapping(target = "passport", source = "requestDTO")
    Client LoanApplicationRequestDTOToClient(LoanApplicationRequestDTO requestDTO);

    static Passport mapPassport(LoanApplicationRequestDTO requestDTO) {
        return Passport.builder()
                .series(requestDTO.passportSeries)
                .number(requestDTO.passportNumber).build();
    }


    @Mapping(target = "passport.issueDate", source = "requestFDTO.passportIssueDate")
    @Mapping(target = "passport.issueBranch", source = "requestFDTO.passportIssueBrach")
    Client updateClientToFinishRegistrationRequestDTO(FinishRegistrationRequestDTO requestFDTO, @MappingTarget Client client);


}
