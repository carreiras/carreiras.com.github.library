package com.ewecarreira.library.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.ewecarreira.library.entities.Book;
import com.ewecarreira.library.entities.Loan;

@DataJpaTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class LoanRepositoryTest {

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    TestEntityManager testEntityManager;

    @Test
    @DisplayName("Deve verificar se existe um emprestimo não retornado para o livro passado.")
    public void existsByBookAndNotReturnedTest() {
        Book book = BookRepositoryTest.createNewBook();
        testEntityManager.persist(book);

        Loan loan = Loan.builder().book(book).customer("Fulano").loanDate(LocalDate.now()).build();
        testEntityManager.persist(loan);
        // Book book = Book.builder().isbn("123").autor("fulano").title("As
        // aventuras").build();

        boolean exists = loanRepository.existsByBookAndNotReturned(book);

        assertThat(exists).isTrue();
    }
}
