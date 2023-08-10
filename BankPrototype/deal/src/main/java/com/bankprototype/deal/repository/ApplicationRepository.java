package com.bankprototype.deal.repository;

import com.bankprototype.deal.repository.dao.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository  extends JpaRepository<Application, Long> {
}
