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
            var book1 = new Book("1234567891", "Chubby Hippo Journey", "Chubby Hippo", 9.90);
            var book2 = new Book("1234567892", "Hippo Hippo", "Hippo Son", 12.90);
            bookRepository.save(book1);
            bookRepository.save(book2);
        };
    }
}
