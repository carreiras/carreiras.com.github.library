package com.ewecarreira.library.service;

import java.util.Optional;

import com.ewecarreira.library.exception.BusinessException;
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
        Book book = createValidBook();
        Mockito.when(bookRepository.existsByIsbn(Mockito.anyString())).thenReturn(false);
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

    @Test
    @DisplayName("Deve lançar erro de negócio ao tentar salvar um livro com ISBN duplicado")
    public void shouldNotSaveABookWithDuplicatedIsbn() {
        Book book = createValidBook();
        Mockito.when(bookRepository.existsByIsbn(Mockito.anyString())).thenReturn(true);

        Throwable exception = Assertions.catchThrowable(() -> bookService.save(book));

        Assertions.assertThat(exception).isInstanceOf(BusinessException.class).hasMessage("Isbn já cadastrado.");
        Mockito.verify(bookRepository, Mockito.never()).save(book);
    }

    @Test
    @DisplayName("Deve obter um livro por id")
    public void getById() {
        Long id = 1L;
        Book book = createValidBook();
        book.setId(id);
        Mockito.when(bookRepository.findById(id)).thenReturn(Optional.of(book));

        Optional<Book> foundBook = bookService.getById(id);

        Assertions.assertThat(foundBook.isPresent()).isTrue();
        Assertions.assertThat(foundBook.get().getId()).isEqualTo(id);
        Assertions.assertThat(foundBook.get().getAutor()).isEqualTo(book.getAutor());
        Assertions.assertThat(foundBook.get().getTitle()).isEqualTo(book.getTitle());
        Assertions.assertThat(foundBook.get().getIsbn()).isEqualTo(book.getIsbn());
    }

    @Test
    @DisplayName("Deve retornar vazio ao obter um livro por id quando ele não existe na base")
    public void bookNotFoundById() {
        Long id = 1L;
        Mockito.when(bookRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Book> book = bookService.getById(id);

        Assertions.assertThat(book.isPresent()).isFalse();
    }

    private Book createValidBook() {
        return Book.builder()
                .title("Titulo")
                .autor("Autor")
                .isbn("123456789")
                .build();
    }
}
