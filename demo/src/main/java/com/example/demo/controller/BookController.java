package com.example.demo.controller;


import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//RestController that has request mapping methods for RESTful requests

@CrossOrigin(origins = "http://localhost:8081")
@RestController //indicates return value of methods should be bound to web response body
public class BookController {

    @Autowired //used to inject BookRepository bean to local variable
    BookRepository bookRepository;

    @GetMapping("/books")
    public ResponseEntity<List<Book>> getAllBooks(@RequestParam(required = false) String title){
        List<Book> books = new ArrayList<Book>();

        if (title == null){
            bookRepository.findAll().forEach(books::add);
        }else{
            bookRepository.findByTitleContaining(title).forEach(books::add);
        }

        if (books.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("books/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable("id") long id){
        Optional<Book> bookData = bookRepository.findById(id);

        if (bookData.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(bookData.get(), HttpStatus.OK);
    }

    @PostMapping("/books")
    public ResponseEntity<Book> createBook(@RequestBody Book book){
        Book newBook = new Book(book.getIsbn(), book.getTitle(), book.getFirstName(), book.getLastName(), book.getDescription(), false);
        bookRepository.save(newBook);
        return new ResponseEntity<>(newBook, HttpStatus.CREATED);
    }

    @PutMapping("/books/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable("id") long id, @RequestBody Book book){
        Optional<Book> bookData = bookRepository.findById(id);

        if (bookData.isPresent()){
            Book newBook = bookData.get();
            newBook.setIsbn(book.getIsbn());
            newBook.setTitle(book.getTitle());
            newBook.setFirstName(book.getFirstName());
            newBook.setLastName(book.getLastName());
            newBook.setDescription(book.getDescription());
            newBook.setPublished(book.isPublished());
            bookRepository.save(newBook);
            return new ResponseEntity<>(newBook, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/books/{id}")
    public ResponseEntity<HttpStatus> deleteBook(@PathVariable("id") long id){
        if (bookRepository.findById(id).isPresent()){
            bookRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/books")
    public ResponseEntity<HttpStatus> deleteAllBooks(){
        bookRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("books/isbn={isbn}")
    public ResponseEntity<List<Book>> findByIsbn(@PathVariable("isbn") String isbn){
        List<Book> books = bookRepository.findByIsbn(isbn);
        if (books.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("books/firstname={firstname}")
    public ResponseEntity<List<Book>> findByFirstName(@PathVariable("firstname") String firstName){
        List<Book> books = bookRepository.findByFirstName(firstName);
        if (books.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("books/lastname={lastname}")
    public ResponseEntity<List<Book>> findByLastName(@PathVariable("lastname") String lastName){
        List<Book> books = bookRepository.findByLastName(lastName);
        if (books.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(books, HttpStatus.OK);
    }


    @GetMapping("books/published")
    public ResponseEntity<List<Book>> findByPublished(){
        List<Book> books = bookRepository.findByPublished(true);
        if (books.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("books?title={keyword}")
    public ResponseEntity<List<Book>> findByTitleContaining(@RequestParam(required = true) String keyword){
        List<Book> books = bookRepository.findByTitleContaining(keyword);
        if (books.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(books, HttpStatus.OK);
    }
}
