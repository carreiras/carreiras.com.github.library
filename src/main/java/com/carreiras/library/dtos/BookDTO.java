package com.carreiras.library.dtos;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {

    private Long id;

    @NotEmpty(message = "Título deve ser informado!")
    private String title;

    @NotEmpty(message = "Autor deve ser informado!")
    private String autor;
    
    @NotEmpty(message = "ISBN deve ser informado!")
    private String isbn;
}
