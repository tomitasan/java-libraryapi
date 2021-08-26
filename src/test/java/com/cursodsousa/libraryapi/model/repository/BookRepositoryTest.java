package com.cursodsousa.libraryapi.model.repository;

import com.cursodsousa.libraryapi.model.entity.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest //testes com JPA - cria instancia do banco de dados em memoria apenas para os testes da classe e depois apaga - h2Database
public class BookRepositoryTest {

    //objeto para simular um cenário, especificamente de teste
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    BookRepository repository;

    @Test
    @DisplayName("Deve retornar verdadeiro quando existir um livro na base com o isbn informado")
    public void returnTrueWhenIsbnExists(){
        //cenário
        String isbn = "123";
        Book book = createNewBook(isbn);
        entityManager.persist(book);

        //execução
        Boolean exists = repository.existsByIsbn(isbn);

        //verificação
        assertThat(exists).isTrue();
    }

    private Book createNewBook(String isbn) {
        return Book.builder().title("Aventuras").author("Fulano").isbn(isbn).build();
    }

    @Test
    @DisplayName("Deve retornar falso quando não existir um livro na base com o isbn informado")
    public void returnFalseWhenIsbnDoesntExists(){
        //cenário
        String isbn = "123";
//        Book book = Book.builder().title("Aventuras").author("Fulano").isbn(isbn).build();
//        entityManager.persist(book);

        //execução
        Boolean exists = repository.existsByIsbn(isbn);

        //verificação
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Deve obter um livro por id")
    public void findByIdTest(){
        //cenário
        Book book = createNewBook("123");
        entityManager.persist(book);

        //execução
        Optional<Book> foundBook = repository.findById(book.getId());

        //verificação
        assertThat(foundBook.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Deve salvar um livro")
    public void saveBookTest(){
        //cenário
        Book book = createNewBook("123");

        //execução
        Book savedBook = repository.save(book);

        //verificação
        assertThat(savedBook.getId()).isNotNull();
    }

    @Test
    @DisplayName("Deve deletar um livro")
    public void deleteBookTest(){
        //cenário
        Book book = createNewBook("123");
        entityManager.persist(book);

        //execução
        Book foundBook = entityManager.find(Book.class, book.getId());
        repository.delete(foundBook);

        //verificação
        Book deletedBook = entityManager.find(Book.class, book.getId());
        assertThat(deletedBook).isNull();
    }
}
