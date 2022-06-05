package com.ewecarreira.library.services.impl;

import org.springframework.stereotype.Service;

import com.ewecarreira.library.entities.Loan;
import com.ewecarreira.library.exceptions.BusinessException;
import com.ewecarreira.library.repositories.LoanRepository;
import com.ewecarreira.library.services.LoanService;

public class LoanServiceImpl implements LoanService {

    private LoanRepository loanRepository;

    public LoanServiceImpl(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    @Override
    public Loan save(Loan loan) {
        if( loanRepository.existsByBookAndNotReturned(loan.getBook()) ){
            throw new BusinessException("Book already loaned");
        }
        return loanRepository.save(loan);
    }
}
