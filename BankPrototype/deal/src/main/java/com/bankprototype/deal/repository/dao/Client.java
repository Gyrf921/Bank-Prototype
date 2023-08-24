package com.bankprototype.deal.repository.dao;


import com.bankprototype.deal.repository.dao.enumfordao.Gender;
import com.bankprototype.deal.repository.dao.enumfordao.MaritalStatus;
import com.bankprototype.deal.repository.dao.jsonb.Employment;
import com.bankprototype.deal.repository.dao.jsonb.Passport;
import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "client")
@TypeDefs({
        @TypeDef(name = "json", typeClass = JsonType.class)
})
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

    @Type(type = "json")
    @Column(name = "passport_id", columnDefinition = "json")
    private Passport passport;

    @Type(type = "json")
    @Column(name = "employment_id", columnDefinition = "json")
    private Employment employment;

    @Column(name = "account")
    private String account;

}
