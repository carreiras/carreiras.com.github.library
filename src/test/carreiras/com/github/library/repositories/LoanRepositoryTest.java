package com.carreiras.library.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import carreiras.com.github.library.entities.Book;
import carreiras.com.github.library.entities.Loan;
import carreiras.com.github.library.repositories.LoanRepository;

@DataJpaTest
@ActiveProfiles("test")
// @ExtendWith(SpringExtension.class)
public class LoanRepositoryTest {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    @DisplayName("Deve verificar se existe um emprestimo não retornado para o livro passado.")
    public void existsByBookAndNotReturnedTest() {
        // Cenário
        Loan loan = createAndPersistLoan(LocalDate.now());
        Book book = loan.getBook();

        // Execução
        boolean exists = loanRepository.existsByBookAndNotReturned(book);

        // Validações
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Deve buscar empréstimo pelo isbn do livro ou custumer")
    public void findByBookIsbnOrCustomerTest() {
        // Cenário
        Loan loan = createAndPersistLoan(LocalDate.now());

        // Execução
        Page<Loan> result = loanRepository.findByBookIsbnOrCustomer(
                "123456789",
                "Fulano",
                PageRequest.of(0, 10));

        // Validações
        Assertions.assertThat(result.getContent()).hasSize(1);
        Assertions.assertThat(result.getContent()).contains(loan);
        Assertions.assertThat(result.getPageable().getPageSize()).isEqualTo(10);
        Assertions.assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        Assertions.assertThat(result.getTotalElements()).isEqualTo(1);
    }

    @Test
    @DisplayName("Deve obter empréstimos cuja data de empréstimo for menor ou igual a três dias atrás e não retornado")
    public void findByLoanDateLessThanAndNotReturnedTest() {
        // Cenário
        Loan loan = createAndPersistLoan(LocalDate.now().minusDays(5));

        // Execução
        List<Loan> result = loanRepository.findByLoanDateLessThanAndNotReturned(
                LocalDate.now().minusDays(4));

        // Validações
        Assertions.assertThat(result).hasSize(1).contains(loan);
    }

    @Test
    @DisplayName("Deve retornar vazio quando não houver empréstimos atrasados")
    public void notFindByLoanDateLessThanAndNotReturnedTest() {
        // Cenário
        Loan loan = createAndPersistLoan(LocalDate.now());

        // Execução
        List<Loan> result = loanRepository.findByLoanDateLessThanAndNotReturned(
                LocalDate.now().minusDays(4));

        // Validações
        Assertions.assertThat(result).isEmpty();;
    }

    public Loan createAndPersistLoan(LocalDate loanDate) {
        Book book = BookRepositoryTest.createNewBook();
        Loan loan = Loan.builder()
                .book(book)
                .customer("Fulano")
                .loanDate(loanDate)
                .build();

        testEntityManager.persist(book);
        testEntityManager.persist(loan);

        return loan;
    }
}
