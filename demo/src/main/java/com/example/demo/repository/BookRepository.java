package com.example.demo.repository;

import com.example.demo.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


//Interface that extends JpaRepository for CRUD methods and custom methods. Autowired in BookController
public interface BookRepository extends JpaRepository<Book, Long> {
    //extending JpaRepository to call JpaRepository methods without creating our own


    //Custom methods that JpaRepository cant do
    Page<Book> findByPublished(boolean published, Pageable pageable); //returns all books with published having value as input published

    @Query("SELECT b FROM Book b WHERE b.title LIKE CONCAT('%',:keyword,'%')")
    Page<Book> findByTitleContaining(String keyword, Pageable pageable); //returns all books with title containing input keywords

    Page<Book> findByIsbn(String isbn, Pageable pageable);

    Page<Book> findByFirstName(String firstName, Pageable pageable);

    Page<Book> findByLastName(String lastName, Pageable pageable);

    //@Query("SELECT b FROM Book b WHERE b.title LIKE CONCAT('%',:keyword,'%') order by b.title ASC")
    List<Book> findByTitleContaining(String title, Sort sort);

    //----------------------------------------------------------------//
    List<Book> findByPublished(boolean published); //returns all books with published having value as input published

    @Query("SELECT b FROM Book b WHERE b.title LIKE CONCAT('%',:keyword,'%')")
    List<Book> findByTitleContaining(String keyword); //returns all books with title containing input keywords

    List<Book> findByIsbn(String isbn);

    List<Book> findByFirstName(String firstName);

    List<Book> findByLastName(String lastName);
}
