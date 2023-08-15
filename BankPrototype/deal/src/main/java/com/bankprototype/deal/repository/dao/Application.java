package com.bankprototype.deal.repository.dao;

import com.bankprototype.deal.repository.dao.jsonb.StatusHistory;
import com.bankprototype.deal.web.dto.LoanOfferDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.sql.Timestamp;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "application")
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_id")
    private Long applicationId;

    @Column(name = "client_id")
    private Long clientId;

    @Column(name = "credit_id")
    private Long creditId;

    @Column(name = "status")
    private String status;

    @Column(name = "creation_date")
    private Timestamp creationDate;

    @Column(name = "applied_offer")
    @JdbcTypeCode(SqlTypes.JSON)
    private LoanOfferDTO appliedOffer;

    @Column(name = "sign_date")
    private Timestamp signDate;

    @Column(name = "ses_code")
    private Long sesCode;

    @Column(name = "status_history")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<StatusHistory> statusHistory;
}
