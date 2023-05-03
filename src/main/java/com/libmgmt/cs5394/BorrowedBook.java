
package com.libmgmt.sampleproject;


import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


public class BorrowedBook extends Book {
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private int checkedOutBy;
    private Date checkedOutAt = null;
    private Date dueAt = null;
    private User borrowedUser = null;
    protected boolean returned;
//
//    public BorrowedBook(int id, String title, String author, String type, boolean isBestSeller, Date published_on) {
//        super(id, title, author, type, isBestSeller, published_on);
//    }
    public BorrowedBook() {}
    
    public BorrowedBook(int checkedOutBy, Date checkedOutAt, Date dueAt, boolean returned) {
        this.checkedOutBy = checkedOutBy;
        this.checkedOutAt = checkedOutAt;
        this.dueAt = dueAt;
        this.returned = returned;
    }

    public boolean isReturned() {
        return returned;
    }

    public void setReturned(boolean returned) {
        this.returned = returned;
    }

    public int getCheckedOutBy() {
        return checkedOutBy;
    }

    public void setCheckedOutBy(int checkedOutBy) {
        this.checkedOutBy = checkedOutBy;
    }

    public Date getCheckedOutAt() {
        return checkedOutAt;
    }

    public void setCheckedOutAt(Date checkedOutAt) {
        this.checkedOutAt = checkedOutAt;
    }

    public Date getDueAt() {
        return dueAt;
    }

    public void setDueAt(Date dueAt) {
        this.dueAt = dueAt;
    }

    public User getBorrowedUser() {
        return borrowedUser;
    }

    public void setBorrowedUser(User borrowedUser) {
        this.borrowedUser = borrowedUser;
    }

    public void setBook(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.type = book.getType();
        this.bestSeller = book.isBestSeller();
        this.published_on = book.getPublishedOn();
    }

    public boolean isOverDue() {
        Date currentDate = new Date();

        //To Test
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.YEAR, 4 );
        //c.add(Calendar.DATE, 100 );
        Date currentDatePlusOne = c.getTime();
        return currentDatePlusOne.compareTo(this.getDueAt()) > 0;
        
        //return currentDate.compareTo(this.getDueAt()) > 0;
    }

    public int getOverDueDays() {

        Date currentDate = new Date();
        
        //To Test
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.YEAR, 4 );
        //c.add(Calendar.DATE, 100 );
        Date currentDatePlusOne = c.getTime();
        int days = this.daysBetween(this.getDueAt(), currentDatePlusOne);
        
        //int days = this.daysBetween(this.getDueAt(), currentDate);

        return days;
    }

    public float getOverDueFine() {

        // Here I am assuming that the maximum price of the book is $100.

        int days = this.getOverDueDays();
        float fine_day = 0.1f;

        float fine = fine_day * days;

        if(fine > 100.0) {
            fine = 100f;
        }

        return fine;
    }

    public int daysBetween(Date d1, Date d2){
        return (int)( Math.abs(d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }
}
