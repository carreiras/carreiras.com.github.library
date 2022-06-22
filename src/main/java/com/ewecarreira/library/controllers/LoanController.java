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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/loans")
@Tag(name = "Loans", description = "API responsible for maintaining book loans.")
public class LoanController {

    private final BookService bookService;
    private final LoanService loanService;
    private final ModelMapper modelMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "borrow a book.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful loan."),
            @ApiResponse(responseCode = "400", description = "Failure to take out the loan.")
    })
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
    @Operation(summary = "Get loan details by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Loan details successfully obtained."),
            @ApiResponse(responseCode = "400", description = "Failed to get loan details.")
    })
    public void returnBook(@PathVariable Long id, @RequestBody ReturnedLoanDTO returnedLoanDTO) {
        Loan loan = loanService.getById(id).orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
        loan.setReturned(returnedLoanDTO.getReturned());
        loanService.update(loan);
    }

    @GetMapping
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Find loans by parameters successfully obtained."),
        @ApiResponse(responseCode = "400", description = "Failed to find loans by parameters.")
})
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
