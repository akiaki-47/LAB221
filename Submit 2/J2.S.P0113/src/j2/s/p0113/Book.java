/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package j2.s.p0113;

/**
 *
 * @author Admin
 */
public class Book {
    private String bookCode, bookName, author, publisher;
    private int publishedYear;
    private boolean forRent;

    public Book(String bookCode, String bookName, String author, String publisher, int publishedYear, boolean forRent) {
        this.bookCode = bookCode;
        this.bookName = bookName;
        this.author = author;
        this.publisher = publisher;
        this.publishedYear = publishedYear;
        this.forRent = forRent;
    }

    public String getBookCode() {
        return bookCode;
    }

    public void setBookCode(String bookCode) {
        this.bookCode = bookCode;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getPublishedYear() {
        return publishedYear;
    }

    public void setPublishedYear(int publishedYear) {
        this.publishedYear = publishedYear;
    }

    public boolean isForRent() {
        return forRent;
    }

    public void setForRent(boolean forRent) {
        this.forRent = forRent;
    }
    
    
}
