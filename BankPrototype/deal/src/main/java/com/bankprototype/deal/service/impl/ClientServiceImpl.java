package com.bankprototype.deal.service.impl;

import com.bankprototype.deal.mapper.EmploymentMapper;
import com.bankprototype.deal.repository.dao.Client;
import com.bankprototype.deal.repository.dao.jsonb.Employment;
import com.bankprototype.deal.repository.dao.jsonb.Passport;
import com.bankprototype.deal.exception.ResourceNotFoundException;
import com.bankprototype.deal.repository.ClientRepository;
import com.bankprototype.deal.service.ClientService;
import com.bankprototype.deal.web.dto.FinishRegistrationRequestDTO;
import com.bankprototype.deal.web.dto.LoanApplicationRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@Slf4j
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Client getClientById(Long id) {

        log.info("[getClientById] >> clientId: {}", id);

        Client client = clientRepository.findById(id)
                .orElseThrow(() ->{
                    log.error("Client not found by this id :{} ", id);
                    return new ResourceNotFoundException("Client not found by this id :: " + id);
                });

        log.info("[getClientById] << result: {}", client);

        return client;
    }

    @Override
    public Client createClient(LoanApplicationRequestDTO requestDTO) {
        log.info("[createClient] >> requestDTO: {}", requestDTO);

        Passport passport = Passport.builder()
                .series(requestDTO.passportSeries)
                .number(requestDTO.passportNumber)
                .build();

        Client client = Client.builder()
                .lastName(requestDTO.lastName)
                .firstName(requestDTO.firstName)
                .middleName(requestDTO.middleName)
                .birthDate(requestDTO.birthdate)
                .email(requestDTO.email)
                .gender(null)
                .maritalStatus(null)
                .dependentAmount(requestDTO.amount)
                .passport(passport)
                .employment(new Employment())
                .build();

        Client savedClient = clientRepository.save(client);

        log.info("[createClient] << result: {}", savedClient);

        return savedClient;
    }

    @Override
    public Client updateClient(Long clientId, FinishRegistrationRequestDTO requestDTO) {
        log.info("[updateClient] >> clientId: {}, requestDTO: {}", clientId, requestDTO);

        Client client = getClientById(clientId);

        Passport passport = Passport.builder()
                .number(client.getPassport().getNumber())
                .series(client.getPassport().getSeries())
                .issueDate(Timestamp.valueOf(requestDTO.getPassportIssueDate().atStartOfDay()))
                .issueBranch(requestDTO.getPassportIssueBrach())
                .build();

        client.setGender(requestDTO.getGender());
        client.setMaritalStatus(requestDTO.getMaritalStatus());
        client.setAccount(requestDTO.getAccount());
        client.setPassport(passport);
        client.setEmployment(EmploymentMapper.INSTANCE.employmentDtoToEmployment(requestDTO.getEmployment()));

        Client updatedClient = clientRepository.save(client);

        log.info("[updateClient] << result: {}", updatedClient);

        return updatedClient;
    }

}
