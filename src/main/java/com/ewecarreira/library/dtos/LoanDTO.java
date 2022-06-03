package com.ewecarreira.library.dtos;

import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanDTO {

    private String isbn;
    private String customer;
}
