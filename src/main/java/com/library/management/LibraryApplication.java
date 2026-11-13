package com.library.management;

import com.library.management.controller.LibraryController;
import com.library.management.dto.BookTransactionRequest;
import com.library.management.models.Book;
import com.library.management.models.Transaction;
import com.library.management.models.User;
import com.library.management.repo.BookRepository;
import com.library.management.repo.TransactionRepository;
import com.library.management.repo.UserRepository;
import com.library.management.service.LibraryService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LibraryApplication {

	public static void main(String[] args) {

        System.out.println("--- Library Management System Startup ---");

        // 1. Dependency Injection Simulation (Wiring the components)
        BookRepository bookRepository = new BookRepository();
        UserRepository userRepository = new UserRepository();
        TransactionRepository transactionRepository = new TransactionRepository();

        LibraryService libraryService = new LibraryService(bookRepository, userRepository, transactionRepository);
        LibraryController controller = new LibraryController(libraryService);

        // 2. Data Initialization (Populating the in-memory database)
        Book book1 = controller.addBook(new Book(null, "The Lord of the Rings", "J.R.R. Tolkien", "9780618260233", 3));
        Book book2 = controller.addBook(new Book(null, "The Secret History", "Donna Tartt", "9780679733371", 1));
        Book book3 = controller.addBook(new Book(null, "Where the Crawdads Sing", "Delia Owens", "9780735219113", 2));

        User user1 = controller.registerUser(new User(null, "Alice Johnson", "alice@example.com"));
        User user2 = controller.registerUser(new User(null, "Bob Williams", "bob@example.com"));

        System.out.println("\n--- Initial State ---");
        System.out.println("Books: " + controller.getAllBooks());

        // 3. Simulated API Call Flow (Testing the Business Logic)

        // A. Alice borrows 'The Lord of the Rings' (Success)
        try {
            Transaction t1 = controller.borrowBook(new BookTransactionRequest(book1.getId(), user1.getId()));
            System.out.println("SUCCESS: " + t1);
        } catch (RuntimeException e) {
            System.err.println("FAILURE: " + e.getMessage());
        }

        // B. Bob borrows 'The Secret History' (Success)
        try {
            Transaction t2 = controller.borrowBook(new BookTransactionRequest(book2.getId(), user2.getId()));
            System.out.println("SUCCESS: " + t2);
        } catch (RuntimeException e) {
            System.err.println("FAILURE: " + e.getMessage());
        }

        // C. Alice attempts to borrow 'The Secret History' (Failure - Insufficient Stock)
        try {
            controller.borrowBook(new BookTransactionRequest(book2.getId(), user1.getId()));
        } catch (RuntimeException e) {
            System.err.println("FAILURE (Expected): " + e.getMessage());
        }

        // D. Alice attempts to borrow 'The Lord of the Rings' AGAIN (Failure - Already Borrowed)
        try {
            controller.borrowBook(new BookTransactionRequest(book1.getId(), user1.getId()));
        } catch (RuntimeException e) {
            System.err.println("FAILURE (Expected): " + e.getMessage());
        }

        // E. Alice returns 'The Lord of the Rings' (Success)
        try {
            Transaction rt1 = controller.returnBook(new BookTransactionRequest(book1.getId(), user1.getId()));
            System.out.println("SUCCESS: " + rt1);
        } catch (RuntimeException e) {
            System.err.println("FAILURE: " + e.getMessage());
        }

        // F. Final Check
        System.out.println("\n--- Final State ---");
        System.out.println("Books: " + controller.getAllBooks());
        System.out.println("Alice's History: " + controller.getUserHistory(user1.getId()));
        System.out.println("Bob's History: " + controller.getUserHistory(user2.getId()));

	}

}
