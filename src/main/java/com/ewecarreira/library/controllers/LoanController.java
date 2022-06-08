package com.ewecarreira.library.controllers;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.ewecarreira.library.dtos.BookDTO;
import com.ewecarreira.library.dtos.LoanDTO;
import com.ewecarreira.library.dtos.LoanFilterDTO;
import com.ewecarreira.library.dtos.ReturnedLoanDTO;
import com.ewecarreira.library.entities.Book;
import com.ewecarreira.library.entities.Loan;
import com.ewecarreira.library.services.BookService;
import com.ewecarreira.library.services.LoanService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/loans")
public class LoanController {

    private final BookService bookService;
    private final LoanService loanService;
    private final ModelMapper modelMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(@RequestBody LoanDTO loanDTO) {
        Book book = bookService.getBookByIsbn(loanDTO.getIsbn())
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "Book not found for passed isbn"));
        Loan entity = Loan.builder()
                .book(book)
                .customer(loanDTO.getCustomer())
                .loanDate(LocalDate.now())
                .build();
        entity = loanService.save(entity);

        return entity.getId();
    }

    @PatchMapping("/{id}")
    public void returnBook(@PathVariable Long id, @RequestBody ReturnedLoanDTO returnedLoanDTO) {
        Loan loan = loanService.getById(id).orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
        loan.setReturned(returnedLoanDTO.getReturned());
        loanService.update(loan);
    }

    @GetMapping
    public Page<LoanDTO> find(LoanFilterDTO filter, Pageable pageRequest) {
        Page<Loan> result = loanService.find(filter, pageRequest);
        List<LoanDTO> list = result.getContent()
                .stream()
                .map(entity -> {
                    Book book = entity.getBook();
                    BookDTO bookDTO = modelMapper.map(book, BookDTO.class);
                    LoanDTO loanDTO = modelMapper.map(entity, LoanDTO.class);
                    loanDTO.setBook(bookDTO);

                    return loanDTO;
                })
                .collect(Collectors.toList());

        return new PageImpl<LoanDTO>(list, pageRequest, result.getTotalElements());
    }
}
