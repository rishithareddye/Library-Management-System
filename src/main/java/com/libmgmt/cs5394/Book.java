
package com.libmgmt.sampleproject;

import java.util.*;

public class Book {
    protected int id;
    protected String title;
    protected String author;
    protected String type;
    protected boolean bestSeller;
    protected Date published_on;
    protected boolean borrowed;
    protected boolean requested;
    protected String borrowerName;

    public Book() {
        
    }
    
    public Book(int id, String title, String author, String type, boolean bestSeller, Date published_on) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.type = type;
        this.bestSeller = bestSeller;
        this.published_on = published_on;
        this.borrowed = false;
        this.requested = false;
    }

    public boolean getBorrowed() {
        return this.borrowed;
    }

    public void setBorrowed(boolean requested) {
        this.borrowed = borrowed;
    }
    public boolean getRequested() {
        return this.requested;
    }

    public void setRequested(boolean requested) {
        this.requested = requested;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isBestSeller() {
        return bestSeller;
    }

    public void setIsBestSeller(boolean bestSeller) {
        this.bestSeller = bestSeller;
    }

    public Date getPublishedOn() {
        return published_on;
    }

    public void setPublishedOn(Date published_on) {
        this.published_on = published_on;
    }
    
    public boolean isNotMag(){
        if("magazine".equals(this.type) || "reference book".equals(this.type) )
            return false;
        else
            return true;
    }

    // public String getBorrowerName() {
    //     return borrowerName;
    // }

    // public void setBorrowerName(String borrowerName) {
    //     this.borrowerName = borrowerName;
    // }
    
}
