package com.ewecarreira.library.services;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.ewecarreira.library.dtos.LoanFilterDTO;
import com.ewecarreira.library.entities.Book;
import com.ewecarreira.library.entities.Loan;
import com.ewecarreira.library.exceptions.BusinessException;
import com.ewecarreira.library.repositories.LoanRepository;
import com.ewecarreira.library.services.impl.LoanServiceImpl;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class LoanServiceTest {

    private LoanService loanService;

    @MockBean
    private LoanRepository loanRepository;

    @BeforeEach
    public void setUp() {
        this.loanService = new LoanServiceImpl(loanRepository);
    }

    @Test
    @DisplayName("Deve salvar um empréstimo")
    public void saveLoanTest() {
        // Cenário
        Book book = Book.builder()
                .id(1L)
                .build();

        String customer = "Fulano";

        Loan salvingLoan = Loan.builder()
                .book(book)
                .customer(customer)
                .loanDate(LocalDate.now())
                .build();

        Loan savedLoan = Loan.builder()
                .id(1L)
                .book(book)
                .customer(customer)
                .loanDate(LocalDate.now())
                .build();

        Mockito.when(loanRepository.existsByBookAndNotReturned(book)).thenReturn(false);
        Mockito.when(loanRepository.save(salvingLoan)).thenReturn(savedLoan);

        // Execução
        Loan loan = loanService.save(salvingLoan);

        // Validações
        Assertions.assertThat(loan.getId()).isEqualTo(savedLoan.getId());
        Assertions.assertThat(loan.getBook().getId()).isEqualTo(savedLoan.getBook().getId());
        Assertions.assertThat(loan.getCustomer()).isEqualTo(savedLoan.getCustomer());
        Assertions.assertThat(loan.getLoanDate()).isEqualTo(savedLoan.getLoanDate());
    }

    @Test
    @DisplayName("Deve lançar erro de negócio ao salvar um empréstimo com livro já emprestado")
    public void loanedBookSaveTest() {
        // Cenário
        Book book = Book.builder()
                .id(1l)
                .build();

        Loan savingLoan = Loan.builder()
                .book(book)
                .customer("Fulano")
                .loanDate(LocalDate.now())
                .build();

        Mockito.when(loanRepository.existsByBookAndNotReturned(book)).thenReturn(true);

        // Execução
        Throwable exception = Assertions.catchThrowable(() -> loanService.save(savingLoan));

        // Validações
        Assertions.assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("Book already loaned");

        Mockito.verify(loanRepository, Mockito.never()).save(savingLoan);
    }

    @Test
    @DisplayName("Deve obter as informações de um empréstimo pelo Id")
    public void getLoanDetailsTest() {
        //  Cenário
        Long id = 1L;

        Loan loan = createLoan();
        loan.setId(id);
        
        Mockito.when(loanRepository.findById(id)).thenReturn(Optional.of(loan));

        // Execução
        Optional<Loan> result = loanService.getById(id);

        //  Validações
        Assertions.assertThat(result.isPresent()).isTrue();
        Assertions.assertThat(result.get().getId()).isEqualTo(id);
        Assertions.assertThat(result.get().getCustomer()).isEqualTo(loan.getCustomer());
        Assertions.assertThat(result.get().getBook()).isEqualTo(loan.getBook());
        Assertions.assertThat(result.get().getLoanDate()).isEqualTo(loan.getLoanDate());

        Mockito.verify(loanRepository).findById(id);
    }

    @Test
    @DisplayName("Deve atualizar um empréstimo")
    public void updateLoanTest() {
        // Cenário
        Loan loan = createLoan();
        loan.setId(1L);
        loan.setReturned(true);

        Mockito.when(loanRepository.save(loan)).thenReturn(loan);

        // Execução
        Loan updatedLoan = loanService.update(loan);

        // Validações
        Assertions.assertThat(updatedLoan.getReturned()).isTrue();
        Mockito.verify(loanRepository).save(loan);
    }

    @Test
    @DisplayName("Deve filtrar empréstimos pelas propriedades")
    public void findLoanTest() {
        // Cenário
        LoanFilterDTO loanFilterDTO = LoanFilterDTO.builder()
                .customer("Fulano")
                .isbn("123456789")
                .build();

        Loan loan = createLoan();
        loan.setId(1L);

        List<Loan> lista = Arrays.asList(loan);

        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Loan> page = new PageImpl<Loan>(lista, pageRequest, lista.size());

        Mockito.when(loanRepository.findByBookIsbnOrCustomer(Mockito.anyString(), Mockito.anyString(),
                Mockito.any(PageRequest.class))).thenReturn(page);

        // Execução
        Page<Loan> result = loanService.find(loanFilterDTO, pageRequest);

        // Validações
        Assertions.assertThat(result.getTotalElements()).isEqualTo(1);
        Assertions.assertThat(result.getContent()).isEqualTo(lista);
        Assertions.assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        Assertions.assertThat(result.getPageable().getPageSize()).isEqualTo(10);
    }

    public static Loan createLoan() {
        Book book = Book.builder().id(1L).build();
        String customer = "Fulano";

        return Loan.builder()
                .book(book)
                .customer(customer)
                .loanDate(LocalDate.now())
                .build();
    }
}
