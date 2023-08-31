package com.bankprototype.deal.service.impl;

import com.bankprototype.deal.exception.ResourceNotFoundException;
import com.bankprototype.deal.mapper.ClientMapper;
import com.bankprototype.deal.repository.ClientRepository;
import com.bankprototype.deal.repository.dao.Client;
import com.bankprototype.deal.repository.dao.jsonb.Passport;
import com.bankprototype.deal.web.dto.FinishRegistrationRequestDTO;
import com.bankprototype.deal.web.dto.LoanApplicationRequestDTO;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class ClientServiceImplTest {

    @MockBean
    private ClientRepository clientRepository;

    @Autowired
    private ClientMapper clientMapper;


    @Autowired
    private ClientServiceImpl clientService;


    @Test
    void getClientById() {
        Long clientId = 1L;

        Client clientTest = Client.builder()
                .clientId(clientId)
                .firstName("testFirstName")
                .build();

        when(clientRepository.findById(any()))
                .thenReturn(Optional.of(clientTest));

        Client clientReturned = clientService.getClientById(clientId);

        System.out.println(clientReturned);

        assertThat(clientReturned).isNotNull();
        assertEquals(clientReturned.getClientId(), clientTest.getClientId());
        assertEquals(clientReturned, clientTest);

        verify(clientRepository, times(1)).findById(any());
    }

    @Test
    void getClientByIdExceptionResourceNotFound() {

        Long clientId = 2L;

        when(clientRepository.findById(any()))
                .thenThrow(new ResourceNotFoundException("Client not found by this id :: " + clientId));

        assertThrows(ResourceNotFoundException.class, () -> {
            Client clientReturned = clientService.getClientById(clientId);
            System.out.println(clientReturned);
        });

        verify(clientRepository, times(1)).findById(any());

    }

    @Test
    void createClient() {

        LoanApplicationRequestDTO requestDTO = LoanApplicationRequestDTO.builder()
                .amount(BigDecimal.valueOf(1000000))
                .firstName("testFirstName")
                .birthDate(LocalDate.of(1991, 1, 1))
                .email("test@neoflex.com")
                .passportNumber("111111")
                .passportSeries("2222")
                .build();

        Client clientTest = clientMapper.loanApplicationRequestDTOToClient(requestDTO);

        when(clientRepository.save(any()))
                .thenReturn(clientTest);

        Client clientReturned = clientService.createClient(requestDTO);

        System.out.println(clientReturned);


        assertThat(clientReturned).isNotNull();
        System.out.println("clientReturned.passport :  " + clientReturned.getPassport());
        System.out.println("clientTest.passport :  " + clientTest.getPassport());
        assertEquals(clientReturned.getPassport(), clientTest.getPassport());

        assertEquals(clientReturned, clientTest);


        verify(clientRepository, times(1)).save(any());
    }

    @Test
    void updateClient() {

        Passport passport = Passport.builder()
                .series("2343")
                .number("234444")
                .build();

        Client clientTest = Client.builder()
                .passport(passport)
                .clientId(1L)
                .build();

        EnhancedRandom enhancedRandom = EnhancedRandomBuilder.aNewEnhancedRandomBuilder().build();
        FinishRegistrationRequestDTO requestDTO = enhancedRandom.nextObject(FinishRegistrationRequestDTO.class);

        Client client = clientMapper.updateClientToFinishRegistrationRequestDTO(requestDTO, clientTest);

        when(clientRepository.findById(any()))
                .thenReturn(Optional.of(clientTest));

        when(clientRepository.save(any()))
                .thenReturn(client);

        Client clientReturned = clientService.updateClient(clientTest.getClientId(), requestDTO);

        System.out.println(clientReturned);

        assertThat(clientReturned).isNotNull();
        System.out.println("clientReturned.passport :  " + clientReturned.getPassport());
        System.out.println("clientTest.passport :  " + clientTest.getPassport());
        assertEquals(clientReturned.getPassport(), clientTest.getPassport());

        assertEquals(clientReturned, clientTest);


        verify(clientRepository, times(1)).save(any());
    }
}