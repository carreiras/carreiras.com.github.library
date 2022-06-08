package com.ewecarreira.library.repositories;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.ewecarreira.library.entities.Book;

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
    public void returnTrueWhenIsbnExistsTest() {
        // Cenário
        Book book = createNewBook();
        testEntityManager.persist(book);

        // Execução
        boolean exists = bookRepository.existsByIsbn("123456789");

        // Validação
        Assertions.assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Deve retornar false quando não existir um livro na base com o isbn informado")
    public void returnFalseWhenIsbnDoesnotExistsTest() {
        // Execução
        boolean exists = bookRepository.existsByIsbn("123456789");

        // Validação
        Assertions.assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Deve obter um livro por id")
    public void findByIdTest() {
        // Cenário
        Book book = createNewBook();
        testEntityManager.persist(book);

        // Execução
        Optional<Book> foundBook = bookRepository.findById(book.getId());

        // Validação
        Assertions.assertThat(foundBook.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Deve salvar um livro")
    public void saveBookTest() {
        // Cenário
        Book book = createNewBook();

        // Execução
        Book savedBook = bookRepository.save(book);

        // Validação
        Assertions.assertThat(savedBook.getId()).isNotNull();
    }

    @Test
    @DisplayName("Deve deletar um livro")
    public void deleteBookTest() {
        // Cenário
        Book book = createNewBook();
        testEntityManager.persist(book);

        Book foundBook = testEntityManager.find(Book.class, book.getId());

        // Execução
        bookRepository.delete(foundBook);
        Book deletedBook = testEntityManager.find(Book.class, book.getId());

        // Validação
        Assertions.assertThat(deletedBook).isNull();
    }

    public static Book createNewBook() {
        return Book.builder()
                .title("Titulo")
                .autor("Autor")
                .isbn("123456789")
                .build();
    }
}
