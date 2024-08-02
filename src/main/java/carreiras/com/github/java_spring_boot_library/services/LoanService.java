package carreiras.com.github.java_spring_boot_library.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import carreiras.com.github.java_spring_boot_library.dtos.LoanFilterDTO;
import carreiras.com.github.java_spring_boot_library.entities.Book;
import carreiras.com.github.java_spring_boot_library.entities.Loan;

public interface LoanService {

    Loan save(Loan loan);

    Optional<Loan> getById(Long id);

    Loan update(Loan loan);

    Page<Loan> find(LoanFilterDTO loanFilterDTO, Pageable pageable);

    Page<Loan> getLoansByBook(Book book, Pageable pageable);
    
    List<Loan> getAllLateLoans();
}
