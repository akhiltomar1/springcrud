package com.example.rest_services.service;

import com.example.rest_services.model.Book;
import com.example.rest_services.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book book1;
    private Book book2;

    @BeforeEach
    void setup() {

        book1 = new Book();
        book1.setBook_id(101);
        book1.setBook_name("Book 101");

        book2 = new Book();
        book2.setBook_id(201);
        book2.setBook_name("Book 201");

    }

    @Test
    void testGetAllBooks() {

        when(bookRepository.findAll()).thenReturn(Arrays.asList(book1, book2));

        List<Book> books = bookService.getAllBooks();

        assertNotNull(books);
        assertEquals(2, books.size());
        assertEquals("Book 101", books.get(0).getBook_name());
        assertEquals("Book 201", books.get(1).getBook_name());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void testDeleteBook() {
        Long bookId = 201L;
        bookService.deleteBook(bookId);
        verify(bookRepository, times(1)).deleteById(bookId);
    }

    @Test
    void testSaveBook() {

        when(bookRepository.save(book1)).thenReturn(book1);
        Book savedBook = bookService.saveBook(book1);

        assertNotNull(savedBook);
        assertEquals("Book 101", savedBook.getBook_name());
        verify(bookRepository, times(1)).save(book1);
    }

    @Test
    void testSaveBookWithNullTitle() {

        Book invalidBook = new Book();
        invalidBook.setBook_id(301);
        invalidBook.setBook_name(null);


        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            bookService.saveBook(invalidBook);
        });

        assertEquals("Book name cannot be null or empty", exception.getMessage());
        verify(bookRepository, times(0)).save(invalidBook);
    }
}
