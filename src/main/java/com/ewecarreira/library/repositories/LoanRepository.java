package com.ewecarreira.library.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ewecarreira.library.entities.Loan;

public interface LoanRepository extends JpaRepository<Loan, Long> {

}
