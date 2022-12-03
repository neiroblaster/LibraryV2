package com.shchayuk.mvcProject2.repositories;

import com.shchayuk.mvcProject2.models.Book;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> findByOwnerId(int id);

    List<Book> findByNameIsStartingWith(String partName);
}
