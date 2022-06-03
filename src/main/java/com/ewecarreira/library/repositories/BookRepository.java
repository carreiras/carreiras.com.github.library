package com.ewecarreira.library.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ewecarreira.library.entities.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

    boolean existsByIsbn(String isbn);
}
