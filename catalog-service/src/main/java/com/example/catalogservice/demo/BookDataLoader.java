package com.example.catalogservice.demo;

import com.example.catalogservice.domain.Book;
import com.example.catalogservice.domain.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("testdata")
public class BookDataLoader {
    private final BookRepository bookRepository;

    public BookDataLoader(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Bean
    public CommandLineRunner loadBookData() {
        return args -> {
            bookRepository.deleteAll();
            var book1 = Book.of("1234567891", "Hippo Journey", "Lil Hip", 9.90, "ChubbyHippo");
            var book2 = Book.of("1234567892", "Hippology", "Big Hip", 12.90, "ChubbyHippo");
            bookRepository.save(book1);
            bookRepository.save(book2);
        };
    }
}
