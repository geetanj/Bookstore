package com.example.admin.bookstore;

/**
 * Created by Admin on 2017-10-13.
 */

public class books { private String bookname,publisher,quantity;

    public books(String publisher, String quantity,String bookname) {
        this.publisher = publisher;
        this.quantity = quantity;
        this.bookname=bookname;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public books(String bookname) {

        this.bookname = bookname;
    }
    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }


}
