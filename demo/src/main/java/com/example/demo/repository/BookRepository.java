package com.example.demo.repository;

import com.example.demo.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


//Interface that extends JpaRepository for CRUD methods and custom methods. Autowired in BookController
public interface BookRepository extends JpaRepository<Book, Long> {
    //extending JpaRepository to call JpaRepository methods without creating our own


    //Custom methods that JpaRepository cant do
    List<Book> findByPublished(boolean published); //returns all books with published having value as input published

    @Query("SELECT b FROM Book b WHERE b.title LIKE CONCAT('%',:keyword,'%')")
    List<Book> findByTitleContaining(String keyword); //returns all books with title containing input keywords

    List<Book> findByIsbn(String isbn);

    List<Book> findByFirstName(String firstName);

    List<Book> findByLastName(String lastName);
}
