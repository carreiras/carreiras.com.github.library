package com.ewecarreira.library.service.impl;

import java.util.Optional;

import com.ewecarreira.library.exception.BusinessException;
import com.ewecarreira.library.model.entity.Book;
import com.ewecarreira.library.model.repository.BookRepository;
import com.ewecarreira.library.service.BookService;

import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {

    private BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Optional<Book> getById(Long id) {
        // TODO Auto-generated method stub
        return bookRepository.findById(id);
    }

    @Override
    public Book save(Book book) {
        if (bookRepository.existsByIsbn(book.getIsbn())) {
            throw new BusinessException("Isbn já cadastrado.");
        }
        return bookRepository.save(book);
    }

    @Override
    public Book update(Book book) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void delete(Book book) {
        // TODO Auto-generated method stub

    }
}
