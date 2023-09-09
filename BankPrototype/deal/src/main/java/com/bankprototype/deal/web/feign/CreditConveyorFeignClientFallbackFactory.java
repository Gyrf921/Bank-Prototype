package com.bankprototype.deal.web.feign;

import com.bankprototype.deal.exception.BadScoringInfoException;
import com.bankprototype.deal.kafka.EmailMessageDTO;
import com.bankprototype.deal.kafka.enumfordto.Theme;
import com.bankprototype.deal.service.DealProducer;
import com.bankprototype.deal.web.dto.CreditDTO;
import com.bankprototype.deal.web.dto.LoanApplicationRequestDTO;
import com.bankprototype.deal.web.dto.LoanOfferDTO;
import com.bankprototype.deal.web.dto.ScoringDataDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class CreditConveyorFeignClientFallbackFactory implements FallbackFactory<CreditConveyorFeignClient> {

    @Value("${topic-name.application-denied}")
    private String applicationDeniedTopicName;

    private final DealProducer dealProducer;

    @Override
    public CreditConveyorFeignClient create(Throwable cause) {

        log.error("An exception occurred when calling the CreditConveyorFeignClient", cause);

        return new CreditConveyorFeignClient() {

            @Override
            public ResponseEntity<List<LoanOfferDTO>> calculatePossibleLoanOffers(LoanApplicationRequestDTO loanApplicationRequestDTO){

                log.info("[Fallback.calculatePossibleLoanOffers] >> loanApplicationRequestDTO: {}", loanApplicationRequestDTO);

                log.info("[Fallback.calculatePossibleLoanOffers] << result: null");

                return ResponseEntity.internalServerError().body(null);
            }

            @Override
            public ResponseEntity<CreditDTO> calculateFullLoanParameters(ScoringDataDTO scoringDataDTO) {
                log.info("[Fallback.calculateFullLoanParameters] >> scoringDataDTO: {}", scoringDataDTO);

                if (cause instanceof BadScoringInfoException) {
                    EmailMessageDTO emailMessageDTO = dealProducer.createMessage(0L, Theme.APPLICATION_DENIED);

                    dealProducer.sendMessage(emailMessageDTO, applicationDeniedTopicName);
                }

                log.info("[Fallback.calculateFullLoanParameters] << result: null");

                return ResponseEntity.badRequest().body(null);
            }


        };
    }
}
