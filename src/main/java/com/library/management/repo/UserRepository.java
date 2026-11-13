package com.library.management.repo;

import com.library.management.models.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class UserRepository {
    private final Map<Long, User> storage = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(100); // Start user IDs higher

    public User save(User user) {
        if (user.getId() == null) {
            user = new User(idGenerator.getAndIncrement(), user.getName(), user.getEmail());
        }
        storage.put(user.getId(), user);
        return user;
    }

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }
}