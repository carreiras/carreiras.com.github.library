package carreiras.com.github.library.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import carreiras.com.github.library.entities.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

    boolean existsByIsbn(String isbn);

    Optional<Book> findByIsbn(String isbn);
}
