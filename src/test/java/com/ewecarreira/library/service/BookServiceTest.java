package com.ewecarreira.library.service;

import com.ewecarreira.library.model.entity.Book;
import com.ewecarreira.library.model.repository.BookRepository;
import com.ewecarreira.library.service.impl.BookServiceImpl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class BookServiceTest {

    BookService bookService;

    @MockBean
    BookRepository bookRepository;

    @BeforeEach
    public void setup() {
        this.bookService = new BookServiceImpl(bookRepository);
    }

    @Test
    @DisplayName("Deve salvar um livro")
    public void saveBookTest() {
        Book book = Book.builder()
                .title("Titulo")
                .autor("Autor")
                .isbn("123456789")
                .build();
        Mockito.when(bookRepository.save(book)).thenReturn(Book.builder()
                .id(1L)
                .title("Titulo")
                .autor("Autor")
                .isbn("123456789")
                .build());

        Book savedBook = bookService.save(book);

        Assertions.assertThat(savedBook.getId()).isNotNull();
        Assertions.assertThat(savedBook.getTitle()).isEqualTo(book.getTitle());
        Assertions.assertThat(savedBook.getAutor()).isEqualTo(book.getAutor());
        Assertions.assertThat(savedBook.getIsbn()).isEqualTo(book.getIsbn());
    }
}
