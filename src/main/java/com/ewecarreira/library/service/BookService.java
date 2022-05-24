package com.ewecarreira.library.service;

import java.util.Optional;

import com.ewecarreira.library.model.entity.Book;

public interface BookService {

    Book save(Book book);

    Optional<Book> getById(Long id);

    void delete(Book book);
    
}
