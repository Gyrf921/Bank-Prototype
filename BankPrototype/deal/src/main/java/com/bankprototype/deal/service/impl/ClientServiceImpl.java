package com.bankprototype.deal.service.impl;

import com.bankprototype.deal.exception.ResourceNotFoundException;
import com.bankprototype.deal.mapper.ClientMapper;
import com.bankprototype.deal.repository.ClientRepository;
import com.bankprototype.deal.repository.dao.Client;
import com.bankprototype.deal.service.ClientService;
import com.bankprototype.deal.web.dto.FinishRegistrationRequestDTO;
import com.bankprototype.deal.web.dto.LoanApplicationRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;


    private final ClientMapper clientMapper;


    @Override
    public Client getClientById(Long id) {

        log.info("[getClientById] >> clientId: {}", id);

        Client client = clientRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Client not found by this id :{} ", id);
                    return new ResourceNotFoundException("Client not found by this id :: " + id);
                });

        log.info("[getClientById] << result: {}", client);

        return client;
    }

    @Override
    public Client createClient(LoanApplicationRequestDTO requestDTO) {
        log.info("[createClient] >> requestDTO: {}", requestDTO);

        Client client = clientMapper.loanApplicationRequestDTOToClient(requestDTO);

        Client savedClient = clientRepository.save(client);

        log.info("[createClient] << result: {}", savedClient);

        return savedClient;
    }

    @Override
    public Client updateClient(Long clientId, FinishRegistrationRequestDTO requestDTO) {
        log.info("[updateClient] >> clientId: {}, requestDTO: {}", clientId, requestDTO);

        Client client = getClientById(clientId);

        Client clientForSaving = clientMapper.updateClientToFinishRegistrationRequestDTO(requestDTO, client);

        Client updatedClient = clientRepository.save(clientForSaving);

        log.info("[updateClient] << result: {}", updatedClient);

        return updatedClient;
    }

}
