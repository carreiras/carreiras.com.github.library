package carreiras.com.github.java_spring_boot_library.dtos;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanDTO {

    private Long id;

    @NotEmpty
    private String isbn;

    @NotEmpty
    private String customer;

    @NotEmpty
    private String email;
    private BookDTO book;
}
