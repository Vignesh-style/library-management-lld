package com.library.management.controller;

import com.library.management.dto.BookTransactionRequest;
import com.library.management.models.Book;
import com.library.management.models.Transaction;
import com.library.management.models.User;
import com.library.management.service.LibraryService;

import java.util.List;

public class LibraryController {

    private final LibraryService libraryService;

    // Constructor injection simulation
    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    // --- Book Endpoints ---

    // POST /api/books (Simulates @PostMapping)
    public Book addBook(Book book) {
        System.out.println("\n[POST /api/books] Adding new book: " + book.getTitle());
        return libraryService.addBook(book);
    }

    // GET /api/books (Simulates @GetMapping)
    public List<Book> getAllBooks() {
        System.out.println("\n[GET /api/books] Retrieving all books.");
        return libraryService.getAllBooks();
    }
    
    // GET /api/books/search?query=... (Simulates @GetMapping with @RequestParam)
    public List<Book> searchBooks(String query) {
        System.out.println("\n[GET /api/books/search] Searching books for: " + query);
        return libraryService.searchBooks(query);
    }

    // --- User Endpoints ---
    
    // POST /api/users (Simulates @PostMapping)
    public User registerUser(User user) {
        System.out.println("\n[POST /api/users] Registering new user: " + user.getName());
        return libraryService.registerUser(user);
    }

    // --- Transaction Endpoints ---

    // POST /api/library/borrow (Simulates @PostMapping)
    public Transaction borrowBook(BookTransactionRequest request) {
        System.out.println("\n[POST /api/library/borrow] Attempting to borrow Book ID: " + request.getBookId() + " by User ID: " + request.getUserId());
        return libraryService.borrowBook(request);
    }
    
    // POST /api/library/return (Simulates @PostMapping)
    public Transaction returnBook(BookTransactionRequest request) {
        System.out.println("\n[POST /api/library/return] Attempting to return Book ID: " + request.getBookId() + " by User ID: " + request.getUserId());
        return libraryService.returnBook(request);
    }
    
    // GET /api/library/history/{userId} (Simulates @GetMapping with @PathVariable)
    public List<Transaction> getUserHistory(Long userId) {
        System.out.println("\n[GET /api/library/history/" + userId + "] Retrieving user transaction history.");
        return libraryService.getUserHistory(userId);
    }
}
