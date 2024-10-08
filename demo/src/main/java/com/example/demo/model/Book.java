package com.example.demo.model;

import jakarta.persistence.*;


//Data Model Class that corresponds to entity and table books
@Entity //class is a persistent java class
@Table(name = "books") //provides the table that maps books entity
public class Book {

    @Id //primary key
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "isbn")
    private String  isbn;

    @Column(name = "title")
    private String title;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "description")
    private String description;

    @Column(name = "published")
    private boolean published;

    public Book(){

    }

    public Book(String isbn, String title, String firstName, String lastName, String description, boolean published){
        this.isbn = isbn;
        this.title = title;
        this.firstName = firstName;
        this.lastName = lastName;
        this.description = description;
        this.published = published;
    }

    public long getId() {
        return id;
    }

    /*public void setId(long id) {
        this.id = id;
    }*/

    public String getIsbn(){
        return isbn;
    }

    public void setIsbn(String isbn){
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstName(){
        return firstName;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", isbn=" + isbn +
                ", title='" + title + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", description='" + description + '\'' +
                ", published=" + published +
                '}';
    }
}
