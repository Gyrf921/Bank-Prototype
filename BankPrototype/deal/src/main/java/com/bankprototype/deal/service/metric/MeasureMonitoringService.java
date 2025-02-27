package com.bankprototype.deal.service.metric;

import com.bankprototype.deal.dao.enumfordao.ApplicationStatus;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MeasureMonitoringService {

    private static final String COUNTER_STATUS_NAME = "application.status";

    private final MeterRegistry meterRegistry;

    @PostConstruct
    private void initStatusCounters() {
        Arrays.stream(ApplicationStatus.values())
                .forEach(status -> {
                            Counter.builder(COUNTER_STATUS_NAME)
                                    .description("Number of applications in each status")
                                    .tag("status", status.name())
                                    .register(meterRegistry);
                        }
                );
    }

    public void incrementStatusCounter(ApplicationStatus status) {
        Counter counter = meterRegistry.counter(COUNTER_STATUS_NAME, List.of(Tag.of("status", status.name())));
        log.info("Get counter {} for application status {} and increment it", counter, status);
        counter.increment();
    }
}
