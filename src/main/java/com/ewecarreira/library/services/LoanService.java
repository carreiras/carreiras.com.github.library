package com.ewecarreira.library.services;

import java.util.Optional;

import com.ewecarreira.library.entities.Loan;

public interface LoanService {

    Loan save(Loan loan);

    Optional<Loan> getById(Long id);

    Loan update(Loan loan);
}
