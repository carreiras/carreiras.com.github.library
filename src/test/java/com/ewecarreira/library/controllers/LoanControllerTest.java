package com.ewecarreira.library.controllers;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.ewecarreira.library.dtos.LoanDTO;
import com.ewecarreira.library.dtos.LoanFilterDTO;
import com.ewecarreira.library.dtos.ReturnedLoanDTO;
import com.ewecarreira.library.entities.Book;
import com.ewecarreira.library.entities.Loan;
import com.ewecarreira.library.exceptions.BusinessException;
import com.ewecarreira.library.services.BookService;
import com.ewecarreira.library.services.LoanService;
import com.ewecarreira.library.services.LoanServiceTest;
import com.fasterxml.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = LoanController.class)
public class LoanControllerTest {

        static String LOAN_API = "/api/loans";

        @Autowired
        MockMvc mockMvc;

        @MockBean
        BookService bookService;

        @MockBean
        LoanService loanService;

        @Test
        @DisplayName("Deve realizar um empréstimo")
        public void createLoanTest() throws Exception {
                LoanDTO loanDTO = LoanDTO.builder().isbn("123456789").customer("Fulano").build();
                Book book = Book.builder().id(1L).isbn("123456789").build();
                Loan loan = Loan.builder().id(1L).customer("Fulano").book(book).loanDate(LocalDate.now()).build();

                String json = new ObjectMapper().writeValueAsString(loanDTO);

                BDDMockito.given(bookService.getBookByIsbn("123456789")).willReturn(Optional.of(book));
                BDDMockito.given(loanService.save(Mockito.any(Loan.class))).willReturn(loan);

                MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(LOAN_API)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(json);

                mockMvc.perform(request)
                                .andExpect(MockMvcResultMatchers.status().isCreated())
                                .andExpect(MockMvcResultMatchers.content().string("1"));
        }

        @Test
        @DisplayName("Deve retornar erro ao tentar fazer o empréstimo de um livro inexistente")
        public void invalidIsbnCreateLoanTest() throws Exception {
                LoanDTO loanDTO = LoanDTO.builder().isbn("123456789").customer("Fulano").build();

                String json = new ObjectMapper().writeValueAsString(loanDTO);

                BDDMockito.given(bookService.getBookByIsbn("123456789")).willReturn(Optional.empty());

                MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(LOAN_API)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(json);

                mockMvc.perform(request)
                                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                                .andExpect(MockMvcResultMatchers.jsonPath("errors", Matchers.hasSize(1)))
                                .andExpect(MockMvcResultMatchers.jsonPath("errors[0]")
                                                .value("Book not found for passed isbn"));
        }

        @Test
        @DisplayName("Deve retornar erro ao tentar fazer o empréstimo de um livro já emprestado")
        public void loanedBookErrorOnCreateLoanTest() throws Exception {
                LoanDTO loanDTO = LoanDTO.builder().isbn("123456789").customer("Fulano").build();
                Book book = Book.builder().id(1L).isbn("123456789").build();

                String json = new ObjectMapper().writeValueAsString(loanDTO);

                BDDMockito.given(bookService.getBookByIsbn("123456789")).willReturn(Optional.of(book));
                BDDMockito.given(loanService.save(Mockito.any(Loan.class)))
                                .willThrow(new BusinessException("Book already loaned"));

                MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(LOAN_API)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(json);

                mockMvc.perform(request)
                                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                                .andExpect(MockMvcResultMatchers.jsonPath("errors", Matchers.hasSize(1)))
                                .andExpect(MockMvcResultMatchers.jsonPath("errors[0]").value("Book already loaned"));
        }

        @Test
        @DisplayName("Deve retornar um livro")
        public void returnBookTest() throws Exception {
                Loan loan = Loan.builder().id(1L).build();
                ReturnedLoanDTO returnedLoanDTO = ReturnedLoanDTO.builder().returned(true).build();
                String json = new ObjectMapper().writeValueAsString(returnedLoanDTO);

                BDDMockito.given(loanService.getById(Mockito.anyLong())).willReturn(Optional.of(loan));

                mockMvc.perform(MockMvcRequestBuilders.patch(LOAN_API.concat("/1"))
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                                .andExpect(MockMvcResultMatchers.status().isOk());
        }

        @Test
        @DisplayName("Deve retornar 404 quando tentar devolver um livro inexistente")
        public void returnInexistentBookTest() throws Exception {
                ReturnedLoanDTO returnedLoanDTO = ReturnedLoanDTO.builder().returned(true).build();
                String json = new ObjectMapper().writeValueAsString(returnedLoanDTO);

                BDDMockito.given(loanService.getById(Mockito.anyLong())).willReturn(Optional.empty());

                mockMvc.perform(MockMvcRequestBuilders.patch(LOAN_API.concat("/1"))
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                                .andExpect(MockMvcResultMatchers.status().isNotFound());
        }

        @Test
        @DisplayName("Deve filtrar empréstimos")
        public void findBooksTest() throws Exception {
                Long id = 1L;
                Loan loan = LoanServiceTest.createLoan();
                loan.setId(id);
                Book book = Book.builder().id(1L).isbn("123456789").build();
                loan.setBook(book);

                BDDMockito.given(loanService.find(Mockito.any(LoanFilterDTO.class), Mockito.any(Pageable.class)))
                                .willReturn(new PageImpl<Loan>(Arrays.asList(loan), PageRequest.of(0, 10), 1));

                String queryString = String.format(
                                "?isbn=%s&customer=%s&page=0&size=10",
                                book.getIsbn(),
                                loan.getCustomer());

                MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(LOAN_API.concat(queryString))
                                .accept(MediaType.APPLICATION_JSON);

                mockMvc.perform(request)
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("content", Matchers.hasSize(1)))
                                .andExpect(MockMvcResultMatchers.jsonPath("totalElements").value(1))
                                .andExpect(MockMvcResultMatchers.jsonPath("pageable.pageSize").value(10))
                                .andExpect(MockMvcResultMatchers.jsonPath("pageable.pageNumber").value(0));
        }
}
