package com.ewecarreira.library.model.repository;

import java.util.Optional;

import com.ewecarreira.library.model.entity.Book;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DataJpaTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class BookRepositoryTest {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    BookRepository bookRepository;

    @Test
    @DisplayName("Deve retornar verdadeiro quando existir um livro na base com o isbn informado")
    public void returnTrueWhenIsbnExists() {
        String isbn = "123456789";
        Book book = createNewBook();
        testEntityManager.persist(book);

        boolean exists = bookRepository.existsByIsbn(isbn);

        Assertions.assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Deve retornar false quando não existir um livro na base com o isbn informado")
    public void returnFalseWhenIsbnDoesnotExists() {
        String isbn = "123456789";

        boolean exists = bookRepository.existsByIsbn(isbn);

        Assertions.assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Deve obter um livro por id")
    public void findById() {
        Book book = createNewBook();
        testEntityManager.persist(book);

        Optional<Book> foundBook = bookRepository.findById(book.getId());

        Assertions.assertThat(foundBook.isPresent()).isTrue();
    }

    private Book createNewBook() {
        return Book.builder()
                .title("Titulo")
                .autor("Autor")
                .isbn("123456789")
                .build();
    }
}
