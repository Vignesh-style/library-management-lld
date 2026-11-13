package com.library.management.repo;

import com.library.management.models.Transaction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class TransactionRepository {
    private final Map<Long, Transaction> storage = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(10000); // Start transaction IDs very high

    public Transaction save(Transaction transaction) {
        if (transaction.getId() == null) {
            long l = idGenerator.addAndGet(1);
            transaction = new Transaction(l, transaction.getBookId(), transaction.getUserId(), transaction.getBorrowDate());
        }
        storage.put(transaction.getId(), transaction);
        return transaction;
    }
    
    public Optional<Transaction> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    /**
     * Custom query: Finds an active (BORROWED) transaction for a specific user and book.
     */
    public Optional<Transaction> findActiveTransaction(Long bookId, Long userId) {
        return storage.values().stream()
                .filter(t -> t.getBookId().equals(bookId) && 
                             t.getUserId().equals(userId) && 
                             t.getStatus() == Transaction.Status.BORROWED)
                .findFirst();
    }

    /**
     * Custom query: Finds all transactions (history) for a specific user.
     */
    public List<Transaction> findByUserId(Long userId) {
        return storage.values().stream()
                .filter(t -> t.getUserId().equals(userId))
                .collect(Collectors.toList());
    }
}