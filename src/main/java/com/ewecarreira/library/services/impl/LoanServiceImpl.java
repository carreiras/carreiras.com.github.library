package com.ewecarreira.library.services.impl;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ewecarreira.library.dtos.LoanFilterDTO;
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
        if (loanRepository.existsByBookAndNotReturned(loan.getBook())) {
            throw new BusinessException("Book already loaned");
        }
        return loanRepository.save(loan);
    }

    @Override
    public Optional<Loan> getById(Long id) {
        return loanRepository.findById(id);
    }

    @Override
    public Loan update(Loan loan) {
        return loanRepository.save(loan);
    }

    @Override
    public Page<Loan> find(LoanFilterDTO loanFilterDTO, Pageable pageable) {
        return loanRepository.findByBookIsbnOrCustomer(loanFilterDTO.getIsbn(), loanFilterDTO.getCustomer(), pageable);
    }
}
