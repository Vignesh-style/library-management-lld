package com.library.management.models;

import java.util.concurrent.atomic.AtomicLong;
import java.util.*;
import java.util.stream.Collectors; // Will be used in Repository/Service
import java.io.Serializable;        // Used for simulated DTOs

// --- Core Entity: Book ---
/**
 * Represents a Book in the Library Catalog.
 */
public class Book {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private int totalQuantity;      // Total copies owned by the library
    private int availableQuantity;  // Copies currently available for borrowing

    public Book(Long id, String title, String author, String isbn, int totalQuantity) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.totalQuantity = totalQuantity;
        this.availableQuantity = totalQuantity; // Initially, all copies are available
    }

    // Getters and Setters
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }
    public int getTotalQuantity() { return totalQuantity; }
    public int getAvailableQuantity() { return availableQuantity; }
    public void setAvailableQuantity(int availableQuantity) { this.availableQuantity = availableQuantity; }

    // Helper for transaction logic
    public boolean isAvailable() { return availableQuantity > 0; }
    
    @Override
    public String toString() {
        return "Book{id=" + id + ", title='" + title + "', author='" + author +
               ", available=" + availableQuantity + "/" + totalQuantity + '}';
    }
}