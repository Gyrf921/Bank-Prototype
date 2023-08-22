package com.bankprototype.deal.service.impl;

import com.bankprototype.deal.mapper.StatusHistoryMapper;
import com.bankprototype.deal.mapper.StatusHistoryMapperImpl;
import com.bankprototype.deal.repository.dao.Application;
import com.bankprototype.deal.exception.ResourceNotFoundException;
import com.bankprototype.deal.repository.ApplicationRepository;
import com.bankprototype.deal.repository.dao.Client;
import com.bankprototype.deal.repository.dao.enumfordao.ApplicationStatus;
import com.bankprototype.deal.repository.dao.enumfordao.ChangeType;
import com.bankprototype.deal.repository.dao.jsonb.StatusHistory;
import com.bankprototype.deal.web.dto.ApplicationStatusHistoryDTO;
import com.bankprototype.deal.web.dto.LoanOfferDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class ApplicationServiceImplTest {

    @MockBean
    private ApplicationRepository applicationRepository;

    @Autowired
    private StatusHistoryMapper statusHistoryMapper;

    @Autowired
    private ApplicationServiceImpl applicationService = new ApplicationServiceImpl(applicationRepository, new StatusHistoryMapperImpl());



    @Test
    void getApplicationById() {
        Long applicationId = 1L;

        Client client = Client.builder()
                .clientId(1L).build();

        Application applicationTest = Application.builder()
                .applicationId(applicationId)
                .clientId(client)
                .status("PREAPPROVAL")
                .build();

        when(applicationRepository.findById(any()))
                .thenReturn(Optional.of(applicationTest));

        Application applicationReturned = applicationService.getApplicationById(applicationId);

        System.out.println(applicationReturned);

        assertThat(applicationReturned).isNotNull();
        assertEquals(applicationReturned.getApplicationId(), applicationTest.getApplicationId());
        assertEquals(applicationReturned, applicationTest);

        verify(applicationRepository, times(1)).findById(any());
    }

    @Test
    void getApplicationByIdExceptionResourceNotFound() {

        Long applicationId = 1L;

        when(applicationRepository.findById(any()))
                .thenThrow(new ResourceNotFoundException("Application not found by this id :: " + applicationId));

        assertThrows(ResourceNotFoundException.class, () -> {
            Application applicationReturned = applicationService.getApplicationById(applicationId);
            System.out.println(applicationReturned);
        });

        verify(applicationRepository, times(1)).findById(any());

    }

    @Test
    void createApplication() {

        Client client = Client.builder()
                .clientId(3l).build();

        ApplicationStatusHistoryDTO applicationStatusHistoryDTO = ApplicationStatusHistoryDTO.builder()
                .status(ApplicationStatus.PREAPPROVAL)
                .time(LocalDateTime.now())
                .changeType(ChangeType.AUTOMATIC)
                .build();

        List<StatusHistory> listStatus = List.of(statusHistoryMapper.applicationStatusHistoryDtoToStatusHistory(applicationStatusHistoryDTO));

        Application applicationTest = Application.builder()
                .applicationId(1L)
                .clientId(client)
                .creditId(null)
                .status(ApplicationStatus.PREAPPROVAL.name())
                .creationDate(LocalDateTime.now())
                .appliedOffer(null)
                .signDate(null)
                .sesCode(null)
                .statusHistory(listStatus)
                .build();

        when(applicationRepository.save(any()))
                .thenReturn(applicationTest);

        Application applicationSaved = applicationService.createApplication(client);

        System.out.println(applicationSaved);

        assertEquals(applicationSaved.getClientId(), client);
        assertEquals(applicationSaved.getApplicationId(), applicationTest.getApplicationId());

        assertEquals(applicationSaved.getStatusHistory().size(), 1);

        verify(applicationRepository, times(1)).save(any());

    }

    @Test
    void updateStatusHistoryForApplication() {
        Client client = Client.builder()
                .clientId(1L).build();

        ApplicationStatusHistoryDTO applicationStatusHistoryDTO = ApplicationStatusHistoryDTO.builder()
                .status(ApplicationStatus.PREAPPROVAL)
                .time(LocalDateTime.now())
                .changeType(ChangeType.AUTOMATIC)
                .build();
        List<StatusHistory> listStatus = new LinkedList<>();
        listStatus.add(statusHistoryMapper.applicationStatusHistoryDtoToStatusHistory(applicationStatusHistoryDTO));
        Application applicationTestBefore = Application.builder()
                .applicationId(1L)
                .clientId(client)
                .status(ApplicationStatus.PREAPPROVAL.name())
                .statusHistory(listStatus)
                .build();

        ApplicationStatus status = ApplicationStatus.APPROVED;

        LoanOfferDTO loanOfferDTO = LoanOfferDTO.builder()
                .applicationId(1L)
                .term(6)
                .isInsuranceEnabled(true)
                .isSalaryClient(true)
                .build();

        Application applicationTestAfter = Application.builder()
                .applicationId(1l)
                .clientId(client)
                .status(status.name())
                .statusHistory(listStatus)
                .appliedOffer(loanOfferDTO)
                .build();


        when(applicationRepository.findById(any()))
                .thenReturn(Optional.of(applicationTestBefore));

        when(applicationRepository.save(any()))
                .thenReturn(applicationTestAfter);


        Application applicationSaved = applicationService
                .updateStatusHistoryForApplication(loanOfferDTO, status);


        System.out.println(applicationSaved);
        assertEquals(applicationSaved.getApplicationId(), applicationTestAfter.getApplicationId());
        assertEquals(applicationSaved.getApplicationId(), applicationTestBefore.getApplicationId());
        assertEquals(applicationSaved.getStatus(), ApplicationStatus.APPROVED.name());

        assertThat(applicationSaved.getAppliedOffer()).isNotNull();
        assertThat(applicationSaved.getAppliedOffer().getIsInsuranceEnabled()).isEqualTo(true);
        assertThat(applicationSaved.getAppliedOffer().getIsSalaryClient()).isEqualTo(true);

        verify(applicationRepository, times(1)).save(any());

    }
}