package com.bankprototype.deal.service.impl;

import com.bankprototype.deal.exception.ResourceNotFoundException;
import com.bankprototype.deal.mapper.StatusHistoryMapperImpl;
import com.bankprototype.deal.repository.ApplicationRepository;
import com.bankprototype.deal.dao.Application;
import com.bankprototype.deal.dao.Client;
import com.bankprototype.deal.dao.enumfordao.ApplicationStatus;
import com.bankprototype.deal.dao.enumfordao.ChangeType;
import com.bankprototype.deal.dao.jsonb.StatusHistory;
import com.bankprototype.deal.web.dto.ApplicationStatusHistoryDTO;
import com.bankprototype.deal.web.dto.LoanOfferDTO;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static com.bankprototype.deal.dao.enumfordao.ApplicationStatus.PREAPPROVAL;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class ApplicationServiceImplTest extends BaseServiceTest {

    @Mock
    private ApplicationRepository applicationRepository;

    private final StatusHistoryMapperImpl statusHistoryMapper = new StatusHistoryMapperImpl();

    private ApplicationServiceImpl applicationService;

    @BeforeEach
    void setUp() {
        applicationService = new ApplicationServiceImpl(applicationRepository, statusHistoryMapper);
    }

    @Test
    void getApplicationById() {
        Long applicationId = 1L;

        Client client = Client.builder()
                .clientId(1L).build();

        Application applicationTest = Application.builder()
                .applicationId(applicationId)
                .clientId(client)
                .status(PREAPPROVAL)
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
                .clientId(3L).build();

        ApplicationStatusHistoryDTO applicationStatusHistoryDTO = ApplicationStatusHistoryDTO.builder()
                .status(PREAPPROVAL)
                .time(LocalDateTime.now())
                .changeType(ChangeType.AUTOMATIC)
                .build();

        List<StatusHistory> listStatus = List.of(statusHistoryMapper.applicationStatusHistoryDtoToStatusHistory(applicationStatusHistoryDTO));

        Application applicationTest = Application.builder()
                .applicationId(1L)
                .clientId(client)
                .creditId(null)
                .status(PREAPPROVAL)
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

        assertEquals(client, applicationSaved.getClientId());
        assertEquals(applicationTest.getApplicationId(), applicationSaved.getApplicationId());

        assertEquals(1, applicationSaved.getStatusHistory().size());

        verify(applicationRepository, times(1)).save(any());

    }

    @Test
    void updateStatusHistoryForApplication() {
        Client client = Client.builder()
                .clientId(1L).build();

        ApplicationStatusHistoryDTO applicationStatusHistoryDTO = ApplicationStatusHistoryDTO.builder()
                .status(PREAPPROVAL)
                .time(LocalDateTime.now())
                .changeType(ChangeType.AUTOMATIC)
                .build();
        List<StatusHistory> listStatus = new LinkedList<>();
        listStatus.add(statusHistoryMapper.applicationStatusHistoryDtoToStatusHistory(applicationStatusHistoryDTO));
        Application applicationTestBefore = Application.builder()
                .applicationId(1L)
                .clientId(client)
                .status(PREAPPROVAL)
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
                .applicationId(1L)
                .clientId(client)
                .status(status)
                .statusHistory(listStatus)
                .appliedOffer(loanOfferDTO)
                .build();


        when(applicationRepository.findById(any()))
                .thenReturn(Optional.of(applicationTestBefore));

        when(applicationRepository.save(any()))
                .thenReturn(applicationTestAfter);


        Application applicationSaved = applicationService
                .updateApplicationSetLoanOffer(loanOfferDTO);


        System.out.println(applicationSaved);
        assertEquals(applicationSaved.getApplicationId(), applicationTestAfter.getApplicationId());
        assertEquals(applicationSaved.getApplicationId(), applicationTestBefore.getApplicationId());
        assertEquals(ApplicationStatus.APPROVED, applicationSaved.getStatus());

        assertThat(applicationSaved.getAppliedOffer()).isNotNull();
        assertThat(applicationSaved.getAppliedOffer().getIsInsuranceEnabled()).isTrue();
        assertThat(applicationSaved.getAppliedOffer().getIsSalaryClient()).isTrue();

        verify(applicationRepository, times(1)).save(any());

    }

    @Test
    void updateSesCodeForApplication() {
        EnhancedRandom enhancedRandom = EnhancedRandomBuilder.aNewEnhancedRandomBuilder().build();

        Application application = enhancedRandom.nextObject(Application.class);
        application.setSesCode(null);

        when(applicationRepository.findById(any()))
                .thenReturn(Optional.of(application));

        when(applicationRepository.save(any()))
                .thenReturn(application);


        Application applicationSaved = applicationService
                .updateSesCodeForApplication(application.getApplicationId());


        System.out.println(applicationSaved);
        assertThat(applicationSaved).isNotNull();

        System.out.println(applicationSaved.getSesCode());
        assertThat(applicationSaved.getSesCode()).isNotNull();
        assertThat(applicationSaved.getSesCode()).isLessThanOrEqualTo(999999L);
        assertThat(applicationSaved.getSesCode()).isGreaterThanOrEqualTo(100000L);

        verify(applicationRepository, times(1)).save(any());

    }

    @Test
    void checkingCorrectnessSesCode() {
        EnhancedRandom enhancedRandom = EnhancedRandomBuilder.aNewEnhancedRandomBuilder().build();
        Application application = enhancedRandom.nextObject(Application.class);

        long sesCodeFromUser = application.getSesCode();

        when(applicationRepository.findById(any()))
                .thenReturn(Optional.of(application));

        Boolean isSesCodeCorrect = applicationService
                .checkingCorrectnessSesCode(application.getApplicationId(), sesCodeFromUser);

        System.out.println(application);
        System.out.println("Ses code is correct: " + isSesCodeCorrect);
        assertThat(isSesCodeCorrect).isTrue();
        assertThat(application.getSesCode()).isEqualTo(sesCodeFromUser);
    }
}