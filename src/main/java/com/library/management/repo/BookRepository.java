package com.library.management.repo;

import com.library.management.models.Book;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BookRepository {
    // In-memory storage simulation
    private final Map<Long, Book> storage = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    /**
     * Saves a book (creates if ID is null, updates otherwise).
     */
    public Book save(Book book) {
        if (book.getId() == null) {
            Long newId = idGenerator.getAndIncrement();
            book = new Book(newId, book.getTitle(), book.getAuthor(), book.getIsbn(), book.getTotalQuantity());
        }
        storage.put(book.getId(), book);
        return book;
    }

    public Optional<Book> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<Book> findAll() {
        return new ArrayList<>(storage.values());
    }
    
    // Custom query: find books by title or author (case-insensitive contains)
    public List<Book> findByTitleOrAuthor(String query) {
        final String lowerCaseQuery = query.toLowerCase();

        List<Book> collect1 = storage.values().stream()
                .filter(b -> b.getTitle().toLowerCase().contains(lowerCaseQuery) ||
                        b.getAuthor().toLowerCase().contains(lowerCaseQuery))
                .collect(Collectors.toList());

        Map<Boolean, List<Book>> aThis = storage.values().stream().collect(Collectors.partitioningBy(k -> k.getAuthor().equals("this")));
        Map<Integer, List<Book>> collect = storage.values().stream().collect(Collectors.groupingBy(Book::getTotalQuantity));
        List<Book> list = storage.values().stream().filter(Book::isAvailable).sorted(Comparator.comparing(Book::getTotalQuantity).reversed()).toList();

        return collect1;

    }

}