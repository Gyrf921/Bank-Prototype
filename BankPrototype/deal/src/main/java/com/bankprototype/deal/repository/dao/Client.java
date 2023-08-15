package com.bankprototype.deal.repository.dao;


import com.bankprototype.deal.repository.dao.enumfordao.Gender;
import com.bankprototype.deal.repository.dao.enumfordao.MaritalStatus;
import com.bankprototype.deal.repository.dao.jsonb.Employment;
import com.bankprototype.deal.repository.dao.jsonb.Passport;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDate;
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "client")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    private Long clientId;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "email")
    private String email;

    @Column(name = "gender")
    private Gender gender;

    @Column(name = "marital_status")
    private MaritalStatus maritalStatus;

    @Column(name = "dependent_amount")
    private BigDecimal dependentAmount;

    @Column(name = "passport_id")
    @JdbcTypeCode(SqlTypes.JSON)
    private Passport passport;

    @Column(name = "employment_id")
    @JdbcTypeCode(SqlTypes.JSON)
    private Employment employment;

    @Column(name = "account")
    private String account;

}
