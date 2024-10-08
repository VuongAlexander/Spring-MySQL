package com.example.demo;

import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.repository.BookRepository;
import com.example.demo.model.Book;

//Used for configuring real databases.
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)

//If you want to disable transaction management
//@Transactional(propagation = Propagation.NOT_SUPPORTED)
@RunWith(SpringRunner.class)
@DataJpaTest
public class JPAUnitTest {

    @Autowired
    //Used to interact with persistence context
    private TestEntityManager entityManager;

    @Autowired
    BookRepository bookRepository;

    @Test
    public void should_find_no_books_if_repository_is_empty(){
        Iterable books = bookRepository.findAll();
        assertThat(books).isEmpty();
    }

    @Test
    public void should_store_a_book(){
        Book book = bookRepository.save(new Book("9798351145013", "The Great Gatsby", "Francis", "Fitzgerald", "testing description", true));

        assertThat(book).hasFieldOrPropertyWithValue("isbn", "9798351145013");
        assertThat(book).hasFieldOrPropertyWithValue("title", "The Great Gatsby");
        assertThat(book).hasFieldOrPropertyWithValue("firstName", "Francis");
        assertThat(book).hasFieldOrPropertyWithValue("lastName", "Fitzgerald");

        assertThat(book).hasFieldOrPropertyWithValue("description", "testing description");
        assertThat(book).hasFieldOrPropertyWithValue("published", true);

    }

    @Test
    public void should_find_all_books(){
        Book book1 = new Book("isbn#1", "Book1", "Alex", "Vuong", "description1", true);
        entityManager.persist(book1);

        Book book2 = new Book("isbn#2", "Book2", "Asuka", "Langley", "description2", false);
        entityManager.persist(book2);

        Book book3 = new Book("isbn#3", "Book3", "Shinji", "Ikari", "description3", true);
        entityManager.persist(book3);

        Iterable books = bookRepository.findAll();

        assertThat(books).hasSize(3).contains(book1, book2, book3);
    }

    @Test
    public void should_find_book_by_id(){
        Book book1 = new Book("isbn#1", "Book1", "Alex", "Vuong", "description1", true);
        entityManager.persist(book1);

        Book foundBook = bookRepository.findById(book1.getId()).get();

        assertThat(foundBook).isEqualTo(book1);

    }

    @Test
    public void should_find_book_by_isbn(){
        Book book1 = new Book("isbn#1", "Book1", "Alex", "Vuong", "description1", true);
        entityManager.persist(book1);

        Book book2 = new Book("isbn#2", "Book2", "Steve", "Rogers", "description2", false);
        entityManager.persist(book2);

        Book book3 = new Book("isbn#3", "Book3", "Alex", "Houston", "description3", true);
        entityManager.persist(book3);

        Iterable books = bookRepository.findByIsbn("isbn#1");

        assertThat(books).hasSize(1).contains(book1);
    }

    @Test
    public void should_find_book_by_first_name(){
        Book book1 = new Book("isbn#1", "Book1", "Alex", "Vuong", "description1", true);
        entityManager.persist(book1);

        Book book2 = new Book("isbn#2", "Book2", "Steve", "Rogers", "description2", false);
        entityManager.persist(book2);

        Book book3 = new Book("isbn#3", "Book3", "Travis", "Houston", "description3", true);
        entityManager.persist(book3);

        Iterable books = bookRepository.findByFirstName("Alex");

        assertThat(books).hasSize(1).contains(book1);
    }

    @Test
    public void should_find_book_by_last_name(){
        Book book1 = new Book("isbn#1", "Book1", "Alex", "Vuong", "description1", true);
        entityManager.persist(book1);

        Book book2 = new Book("isbn#2", "Book2", "Alan", "Vuong", "description2", false);
        entityManager.persist(book2);

        Book book3 = new Book("isbn#3", "Book3", "Kendrick", "Lamar", "description3", true);
        entityManager.persist(book3);

        Iterable books = bookRepository.findByLastName("Vuong");

        assertThat(books).hasSize(2).contains(book1, book2);
    }

    @Test
    public void should_find_published_books(){
        Book book1 = new Book("isbn#1", "Book1", "Alex", "Vuong", "description1", true);
        entityManager.persist(book1);

        Book book2 = new Book("isbn#2", "Book2", "Alan", "Vuong", "description2", false);
        entityManager.persist(book2);

        Book book3 = new Book("isbn#3", "Book3", "Alvin", "Vuong", "description3", true);
        entityManager.persist(book3);

        Iterable books = bookRepository.findByPublished(true);

        assertThat(books).hasSize(2).contains(book1, book3);
    }

    @Test
    public void should_find_books_by_title_containing_string(){
        Book book1 = new Book("isbn#1", "Dividing", "Alex", "Vuong", "description1", true);
        entityManager.persist(book1);

        Book book2 = new Book("isbn#2", "Subtracting", "Alan", "Vuong", "description2", false);
        entityManager.persist(book2);

        Book book3 = new Book("isbn#3", "Adding", "Alvin", "Vuong", "description3", true);
        entityManager.persist(book3);

        Iterable books = bookRepository.findByTitleContaining("dd");

        assertThat(books).hasSize(1).contains(book3);
    }

    @Test
    public void should_update_books_by_id(){
        Book book1 = new Book("isbn#1", "Dividing", "Alex", "Vuong", "description1", true);
        entityManager.persist(book1);

        Book book2 = new Book("isbn#2", "Subtracting", "Alan", "Vuong", "description2", false);
        entityManager.persist(book2);

        Book updateBook = new Book("isbn#4", "Multiplying", "Alex", "Vuong", "updated description1", true);

        Book book = bookRepository.findById(book1.getId()).get();
        book.setIsbn(updateBook.getIsbn());
        book.setTitle(updateBook.getTitle());
        book.setFirstName(updateBook.getFirstName());
        book.setLastName(updateBook.getLastName());
        book.setDescription(updateBook.getDescription());
        book.setPublished(updateBook.isPublished());
        bookRepository.save(book);

        Book checkBook = bookRepository.findById(book1.getId()).get();

        assertThat(checkBook.getId()).isEqualTo(book1.getId());
        assertThat(checkBook.getIsbn()).isEqualTo(book1.getIsbn());
        assertThat(checkBook.getTitle()).isEqualTo(book1.getTitle());
        assertThat(checkBook.getFirstName()).isEqualTo(book1.getFirstName());
        assertThat(checkBook.getLastName()).isEqualTo(book1.getLastName());
        assertThat(checkBook.getDescription()).isEqualTo(book1.getDescription());
        assertThat(checkBook.isPublished()).isEqualTo(book1.isPublished());
    }

    @Test
    public void should_delete_books_by_id(){
        Book book1 = new Book("isbn#1", "Dividing", "Alex", "Vuong", "description1", true);
        entityManager.persist(book1);

        Book book2 = new Book("isbn#2", "Subtracting", "Alan", "Vuong", "description2", false);
        entityManager.persist(book2);

        Book book3 = new Book("isbn#3", "Adding", "Alvin", "Vuong", "description3", true);
        entityManager.persist(book3);

        bookRepository.deleteById(book2.getId());

        Iterable books = bookRepository.findAll();

        assertThat(books).hasSize(2).contains(book1, book3);
    }

    @Test
    public void should_delete_all_books(){
        Book book1 = new Book("isbn#1", "Dividing", "Alex", "Vuong", "description1", true);
        entityManager.persist(book1);

        Book book2 = new Book("isbn#2", "Subtracting", "Alan", "Vuong", "description2", false);
        entityManager.persist(book2);

        bookRepository.deleteAll();

        assertThat(bookRepository.findAll()).isEmpty();
    }
}
