package com.cursodsousa.libraryapi.model.repository;

import com.cursodsousa.libraryapi.model.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

//JPA<Entidade que vai ser trabalhada,Tipo da chave primaria da entidade>
public interface BookRepository extends JpaRepository<Book, Long> {
    boolean existsByIsbn(String isbn);
}
