package com.library.management.service;

import com.library.management.dto.BookTransactionRequest;
import com.library.management.exception.InsufficientStockException;
import com.library.management.exception.ResourceNotFoundException;
import com.library.management.exception.TransactionException;
import com.library.management.models.Book;
import com.library.management.models.Transaction;
import com.library.management.models.User;
import com.library.management.repo.BookRepository;
import com.library.management.repo.TransactionRepository;
import com.library.management.repo.UserRepository;

import javax.swing.*;
import java.util.List;

public class LibraryService {
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    // Constructor injection simulation
    public LibraryService(BookRepository bookRepository, UserRepository userRepository, TransactionRepository transactionRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    // --- Book and User Management (Simple CRUD) ---

    public Book addBook(Book book) {
        // Simple logic: saves the book, relying on repository for ID generation
        return bookRepository.save(book);
    }

    public User registerUser(User user) {
        // Simple logic: registers the user
        return userRepository.save(user);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public List<Book> searchBooks(String query) {
        if (query == null || query.trim().isEmpty()) {
            return bookRepository.findAll();
        }
        return bookRepository.findByTitleOrAuthor(query);
    }

    // --- Core Business Logic: Borrow ---
    // Inside the LibraryService class...

    public Transaction borrowBook(BookTransactionRequest request) {
        Long bookId = request.getBookId();
        Long userId = request.getUserId();

        // 1. Validate resources exist
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + bookId));

        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        // CRITICAL SECTION: Synchronize access to the specific Book object.
        synchronized (book) {

            // 2. Enforce business rule: Check availability
            if (!book.isAvailable()) {
                // If synchronization was needed, the other thread took the last copy.
                throw new InsufficientStockException("No available copies of book ID: " + bookId);
            }

            // 3. Enforce business rule: Check for active loan
            // This is safe to check inside the synchronized block.
            if (transactionRepository.findActiveTransaction(bookId, userId).isPresent()) {
                throw new TransactionException("User " + userId + " already has a borrowed copy of book " + bookId);
            }

            // 4. Update state (Simulated Transactional Logic)
            // Decrement available count and save updated book state
            book.setAvailableQuantity(book.getAvailableQuantity() - 1);
            bookRepository.save(book);

            // Create and save new transaction (ID and current time set in repository)
            Transaction newTransaction = new Transaction(null, bookId, userId, System.currentTimeMillis());

            return transactionRepository.save(newTransaction);
        } // Lock released here
    }

  /** /2. Spring Boot / Database Solution (Real-World)

    In a professional Spring Boot application using `@SpringBootApplication` and `spring-boot-starter-data-jpa` (which you mentioned in your dependencies), the problem is solved using **Database Transactions and Locking**:

            1.  **Use `@Transactional`:** Annotate the entire `borrowBook` service method with `@Transactional`. This ensures that all database operations within the method succeed or fail as a single unit.
            2.  **Use Pessimistic Locking:** Within your JPA repository, you would use a query or an annotation like `@Lock(LockModeType.PESSIMISTIC_WRITE)` on the `findById` method for the `Book` entity. This tells the database: **"Lock this book row right now. Do not let any other connection read or write to it until my transaction is complete."**

    This database-level locking is the standard, reliable solution for concurrency in distributed applications, as it ensures data integrity even across multiple application servers.

---

    The corrected code in your Canvas uses the `synchronized(book)` block, which is the appropriate way to demonstrate thread-safety within your current, single-process, in-memory architecture.

    Do you have any questions about the difference between Java's `synchronized` approach and a database-level locking approach?

    // --- Core Business Logic: Return ---

     * Handles the return process, updating stock and closing the transaction record.
     */

    public Transaction returnBook(BookTransactionRequest request) {
        Long bookId = request.getBookId();
        Long userId = request.getUserId();

        // 1. Find the active transaction
        Transaction activeTransaction = transactionRepository.findActiveTransaction(bookId, userId)
                .orElseThrow(() -> new TransactionException("No active loan found for user " + userId + " and book " + bookId));


        // 2. Update book stock
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found (Internal inconsistency)"));

        synchronized (book) {

            book.setAvailableQuantity(book.getAvailableQuantity() + 1);
            bookRepository.save(book);

            // 3. Update transaction state
            // Use a copy constructor for updating to ensure immutability is handled cleanly if this were a production entity
            Transaction updatedTransaction = new Transaction(activeTransaction);
            updatedTransaction.setStatus(Transaction.Status.RETURNED);
            updatedTransaction.setReturnDate(System.currentTimeMillis());

            return transactionRepository.save(updatedTransaction);

        }

    }

    // --- History Retrieval ---

    public List<Transaction> getUserHistory(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        return transactionRepository.findByUserId(userId);
    }
}