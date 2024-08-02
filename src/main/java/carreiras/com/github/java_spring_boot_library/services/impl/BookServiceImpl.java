package carreiras.com.github.java_spring_boot_library.services.impl;

import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import carreiras.com.github.java_spring_boot_library.entities.Book;
import carreiras.com.github.java_spring_boot_library.exceptions.BusinessException;
import carreiras.com.github.java_spring_boot_library.repositories.BookRepository;
import carreiras.com.github.java_spring_boot_library.services.BookService;

@Service
public class BookServiceImpl implements BookService {

    private BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Optional<Book> getById(Long id) {
        return bookRepository.findById(id);
    }

    @Override
    public Book save(Book book) {
        if (bookRepository.existsByIsbn(book.getIsbn()))
            throw new BusinessException("ISBN já cadastrado. " +
                    "Por favor, revise o ISBN do livro ou contate a sua gerência.");

        return bookRepository.save(book);
    }

    @Override
    public Book update(Book book) {
        if (book == null || book.getId() == null)
            throw new IllegalArgumentException("Book id cant be null.");

        return this.bookRepository.save(book);
    }

    @Override
    public void delete(Book book) {
        if (book == null || book.getId() == null)
            throw new IllegalArgumentException("Book id cant be null.");

        this.bookRepository.delete(book);
    }

    @Override
    public Page<Book> find(Book filter, Pageable pageRequest) {
        Example<Book> example = Example.of(
                filter,
                ExampleMatcher.matching()
                        .withIgnoreCase()
                        .withIgnoreNullValues()
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));
        return bookRepository.findAll(example, pageRequest);
    }

    @Override
    public Optional<Book> getBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }


    
}
