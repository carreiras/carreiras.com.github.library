package com.carreiras.library.controllers;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.carreiras.library.dtos.BookDTO;
import com.carreiras.library.dtos.LoanDTO;
import com.carreiras.library.entities.Book;
import com.carreiras.library.entities.Loan;
import com.carreiras.library.services.BookService;
import com.carreiras.library.services.LoanService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
@Tag(name = "Books", description = "API responsible for book maintenance.")
public class BookController {

    private final BookService bookService;
    private final LoanService loanService;
    private final ModelMapper modelMapper;

    @GetMapping("/{id}")
    @Operation(summary = "Get details of a book by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book details successfully obtained."),
            @ApiResponse(responseCode = "400", description = "Failed to get book details.")
    })
    public BookDTO get(@PathVariable Long id) {
        log.info("obtaining details for book id: {}", id);
        return bookService.getById(id)
                .map(book -> modelMapper.map(book, BookDTO.class))
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a book.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book created successfully."),
            @ApiResponse(responseCode = "400", description = "Failed to create book.")
    })
    public BookDTO create(@RequestBody @Valid BookDTO request) {
        log.info("creating a book for isbn: {}", request.getIsbn());
        Book book = modelMapper.map(request, Book.class);
        Book bookSaved = bookService.save(book);
        BookDTO bookDTO = modelMapper.map(bookSaved, BookDTO.class);

        return bookDTO;
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a book.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book successfully updated."),
            @ApiResponse(responseCode = "400", description = "Failed to update book.")
    })
    public BookDTO update(@PathVariable Long id, @RequestBody @Valid BookDTO request) {
        log.info("updating book of id: {}", id);
        return bookService.getById(id)
                .map(book -> {
                    book.setAutor(request.getAutor());
                    book.setTitle(request.getTitle());
                    book = bookService.update(book);
                    return modelMapper.map(book, BookDTO.class);
                })
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a book.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted book."),
            @ApiResponse(responseCode = "400", description = "failed to delete book.")
    })
    public void delete(@PathVariable Long id) {
        log.info("deleting book of id: {}", id);
        Book book = bookService.getById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
        bookService.delete(book);
    }

    @GetMapping
    @Operation(summary = "Find books by params.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Find books by parameters successfully obtained."),
            @ApiResponse(responseCode = "400", description = "Failed to find books by parameters.")
    })
    public Page<BookDTO> find(BookDTO bookDTO, Pageable pageRequest) {
        Book filter = modelMapper.map(bookDTO, Book.class);
        Page<Book> result = bookService.find(filter, pageRequest);
        List<BookDTO> list = result.getContent()
                .stream()
                .map(entity -> modelMapper.map(entity, BookDTO.class))
                .collect(Collectors.toList());

        return new PageImpl<BookDTO>(list, pageRequest, result.getTotalElements());
    }

    @GetMapping("/{id}/loans")
    @Operation(summary = "Search loans by book id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book loan search successfully obtained."),
            @ApiResponse(responseCode = "400", description = "Failure to get book loans.")
    })
    public Page<LoanDTO> loansByBook(@PathVariable Long id, Pageable pageable) {
        Book book = bookService.getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Page<Loan> result = loanService.getLoansByBook(book, pageable);
        List<LoanDTO> list = result.getContent()
                .stream()
                .map(loan -> {
                    Book loanBook = loan.getBook();
                    BookDTO bookDTO = modelMapper.map(loanBook, BookDTO.class);
                    LoanDTO loanDTO = modelMapper.map(loan, LoanDTO.class);
                    loanDTO.setBook(bookDTO);
                    return loanDTO;
                }).collect(Collectors.toList());
        return new PageImpl<LoanDTO>(list, pageable, result.getTotalElements());
    }
}
