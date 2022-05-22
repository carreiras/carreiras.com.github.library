package com.ewecarreira.library.api.resource.Controller;

import com.ewecarreira.library.api.resource.dto.BookDTO;
import com.ewecarreira.library.model.entity.Book;
import com.ewecarreira.library.service.BookService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    BookService bookService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO create(@RequestBody BookDTO bookDTO) {
        Book toBook = Book.builder()
                .title(bookDTO.getTitle())
                .autor(bookDTO.getAutor())
                .isbn(bookDTO.getIsbn())
                .build();

        Book book = bookService.save(toBook);

        BookDTO toBookDto = BookDTO.builder()
                .id(book.getId())
                .title(book.getTitle())
                .autor(book.getAutor())
                .isbn(book.getIsbn())
                .build();

        return toBookDto;
    }
}
