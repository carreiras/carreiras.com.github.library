package com.ewecarreira.library.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ewecarreira.library.dtos.LoanFilterDTO;
import com.ewecarreira.library.entities.Book;
import com.ewecarreira.library.entities.Loan;

public interface LoanService {

    Loan save(Loan loan);

    Optional<Loan> getById(Long id);

    Loan update(Loan loan);

    Page<Loan> find(LoanFilterDTO loanFilterDTO, Pageable pageable);

    Page<Loan> getLoansByBook(Book book, Pageable pageable);
}
