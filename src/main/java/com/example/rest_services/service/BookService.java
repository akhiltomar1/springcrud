package com.example.rest_services.service;


import com.example.rest_services.model.Book;
import com.example.rest_services.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public List<Book> getAllBooks(){
        return bookRepository.findAll();
    }

//    public Book createBook(Book book) {
//        return bookRepository.save(book); // No password hashing!
//    }

//    public Book findBookByBookName(String bookName){
//        return bookRepository.findByBookName(bookName);
//    }

    public void deleteBook(Long id){
        bookRepository.deleteById(id);
    }

    public Book saveBook(Book book) {
//        if (book.getBook_name() == null || book.getBook_name().isBlank()) {
//            throw new IllegalArgumentException("Book name cannot be null or empty");
//        }
        return bookRepository.save(book);
    }
}
