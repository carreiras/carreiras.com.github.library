package com.ewecarreira.library.service;

import com.ewecarreira.library.model.entity.Book;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class BookServiceTest {

    BookService bookService;

    @Test
    @DisplayName("Deve salvar um livro")
    public void saveBookTest() {
        Book book = Book.builder()
                .title("Titulo")
                .autor("Autor")
                .isbn("123456789")
                .build();

        Book savedBook = bookService.save(book);

        Assertions.assertThat(savedBook.getId()).isNotNull();
        Assertions.assertThat(savedBook.getTitle()).isEqualTo(book.getTitle());
        Assertions.assertThat(savedBook.getAutor()).isEqualTo(book.getAutor());
        Assertions.assertThat(savedBook.getId()).isEqualTo(book.getIsbn());
        
    }
}
