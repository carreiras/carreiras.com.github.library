package carreiras.com.github.java_spring_boot_library.services.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import carreiras.com.github.java_spring_boot_library.dtos.LoanFilterDTO;
import carreiras.com.github.java_spring_boot_library.entities.Book;
import carreiras.com.github.java_spring_boot_library.entities.Loan;
import carreiras.com.github.java_spring_boot_library.exceptions.BusinessException;
import carreiras.com.github.java_spring_boot_library.repositories.LoanRepository;
import carreiras.com.github.java_spring_boot_library.services.LoanService;

@Service
public class LoanServiceImpl implements LoanService {

    private LoanRepository loanRepository;

    public LoanServiceImpl(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    @Override
    public Loan save(Loan loan) {
        if (loanRepository.existsByBookAndNotReturned(loan.getBook()))
            throw new BusinessException("Book already loaned");

        return loanRepository.save(loan);
    }

    @Override
    public Optional<Loan> getById(Long id) {
        return loanRepository.findById(id);
    }

    @Override
    public Loan update(Loan loan) {
        return loanRepository.save(loan);
    }

    @Override
    public Page<Loan> find(LoanFilterDTO loanFilterDTO, Pageable pageable) {
        return loanRepository.findByBookIsbnOrCustomer(loanFilterDTO.getIsbn(), loanFilterDTO.getCustomer(), pageable);
    }

    @Override
    public Page<Loan> getLoansByBook(Book book, Pageable pageable) {
        return loanRepository.findByBook(book, pageable);
    }

    @Override
    public List<Loan> getAllLateLoans() {
        final Integer loanDays = 4;
        LocalDate threeDaysAgo = LocalDate.now().minusDays(loanDays);
        return loanRepository.findByLoanDateLessThanAndNotReturned(threeDaysAgo);
    }
}
