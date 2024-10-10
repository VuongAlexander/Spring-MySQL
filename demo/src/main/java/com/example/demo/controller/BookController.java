package com.example.demo.controller;


import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

//RestController that has request mapping methods for RESTful requests

@CrossOrigin(origins = "http://localhost:8081")
@RestController //indicates return value of methods should be bound to web response body
public class BookController {

    @Autowired //used to inject BookRepository bean to local variable
    BookRepository bookRepository;

    private Sort.Direction getSortDirection(String direction){
        if (direction.equals("asc")){
            return Sort.Direction.ASC;
        }
        else if (direction.equals("desc")){
            return Sort.Direction.DESC;
        }
        return Sort.Direction.ASC;
    }

    @GetMapping("/books")
    public ResponseEntity<Map<String, Object>> getAllBooks(@RequestParam(required = false) String title,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "3") int size,
                                                           @RequestParam(defaultValue = "id, desc") String[] sort){

        List<Sort.Order> orders = new ArrayList<Order>();

        if(sort[0].contains(",")){
            //will sort more than 2 fields
            //sortOrder="field, direction")
            for (String sortOrder: sort){
                String[] _sort = sortOrder.split(",");
                orders.add(new Sort.Order(getSortDirection(_sort[1]), _sort[0]));
            }
        }else{
            orders.add(new Order(getSortDirection(sort[1]), sort[0]));
        }

        List<Book> books = new ArrayList<Book>();
        Pageable pagingSort = PageRequest.of(page, size, Sort.by(orders));

        Page<Book> pageBooks;
        if (title == null){
            pageBooks = bookRepository.findAll(pagingSort);//.forEach(books::add);
        }else{
            pageBooks = bookRepository.findByTitleContaining(title, pagingSort);//.forEach(books::add);
        }

        books = pageBooks.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("books", books);
        response.put("currentPage", pageBooks.getNumber());
        response.put("totalItems", pageBooks.getTotalElements());
        response.put("totalPages", pageBooks.getTotalPages());


        if (books.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
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
    public ResponseEntity<Map<String, Object>> findByIsbn(@PathVariable("isbn") String isbn,
                                                 @RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "3") int size){
        List<Book> books = new ArrayList<Book>();
        Pageable paging = PageRequest.of(page, size);

        Page<Book> pageBooks = bookRepository.findByIsbn(isbn, paging);
        books = pageBooks.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("books", books);
        response.put("currentPage", pageBooks.getNumber());
        response.put("totalItems", pageBooks.getTotalElements());
        response.put("totalPages", pageBooks.getTotalPages());

        if (books.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("books/firstname={firstname}")
    public ResponseEntity<Map<String, Object>> findByFirstName(@PathVariable("firstname") String firstName,
                                                      @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "3") int size){

        List<Book> books = new ArrayList<Book>();
        Pageable paging = PageRequest.of(page, size);

        Page<Book> pageBooks = bookRepository.findByFirstName(firstName, paging);
        books = pageBooks.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("books", books);
        response.put("currentPage", pageBooks.getNumber());
        response.put("totalItems", pageBooks.getTotalElements());
        response.put("totalPages", pageBooks.getTotalPages());

        if (books.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("books/lastname={lastname}")
    public ResponseEntity<Map<String, Object>> findByLastName(@PathVariable("lastname") String lastName,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "3") int size){
        List<Book> books = new ArrayList<Book>();
        Pageable paging = PageRequest.of(page, size);

        Page<Book> pageBooks = bookRepository.findByLastName(lastName, paging);
        books = pageBooks.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("books", books);
        response.put("currentPage", pageBooks.getNumber());
        response.put("totalItems", pageBooks.getTotalElements());
        response.put("totalPages", pageBooks.getTotalPages());

        if (books.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("books/published")
    public ResponseEntity<Map<String, Object>> findByPublished(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "3") int size){

        List<Book> books = new ArrayList<Book>();
        Pageable paging = PageRequest.of(page, size);

        Page<Book> pageBooks = bookRepository.findByPublished(true, paging);
        books = pageBooks.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("books", books);
        response.put("currentPage", pageBooks.getNumber());
        response.put("totalItems", pageBooks.getTotalElements());
        response.put("totalPages", pageBooks.getTotalPages());

        if (books.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("books?title={keyword}")
    public ResponseEntity<Map<String, Object>> findByTitleContaining(@RequestParam(required = true) String keyword,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "3") int size){
        List<Book> books = new ArrayList<Book>();
        Pageable paging = PageRequest.of(page, size);

        Page<Book> pageBooks = bookRepository.findByTitleContaining(keyword, paging);
        books = pageBooks.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("books", books);
        response.put("currentPage", pageBooks.getNumber());
        response.put("totalItems", pageBooks.getTotalElements());
        response.put("totalPages", pageBooks.getTotalPages());


        if (books.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
