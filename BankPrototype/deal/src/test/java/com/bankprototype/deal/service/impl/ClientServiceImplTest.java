package com.bankprototype.deal.service.impl;

import com.bankprototype.deal.repository.dao.Client;
import com.bankprototype.deal.repository.dao.jsonb.Passport;
import com.bankprototype.deal.exception.ResourceNotFoundException;
import com.bankprototype.deal.repository.ClientRepository;
import com.bankprototype.deal.web.dto.LoanApplicationRequestDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientServiceImpl clientService;

  /*  @Test
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
                .birthdate(LocalDate.of(1991, 1, 1))
                .email("test@neoflex.com")
                .passportNumber("111111")
                .passportSeries("2222")
                .build();

        Passport passportTest = Passport.builder()
                .series(requestDTO.passportSeries)
                .number(requestDTO.passportNumber)
                .build();

        Client clientTest = Client.builder()
                .firstName(requestDTO.firstName)
                .birthDate(requestDTO.birthdate)
                .email(requestDTO.email)
                .dependentAmount(requestDTO.amount)
                .passport(passportTest)
                .build();

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


    }*/
}