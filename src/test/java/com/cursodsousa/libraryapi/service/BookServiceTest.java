package com.cursodsousa.libraryapi.service;

import com.cursodsousa.libraryapi.exception.BusinessException;
import com.cursodsousa.libraryapi.model.entity.Book;
import com.cursodsousa.libraryapi.model.repository.BookRepository;
import com.cursodsousa.libraryapi.service.impl.BookServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

//subir o contexto do Spring apenas com o que precisa
@ExtendWith(SpringExtension.class)
//usar essas implementações apenas no ambiente/perfil de teste
@ActiveProfiles("test")
public class BookServiceTest {

    BookService service;

    @MockBean
    BookRepository repository;

    @BeforeEach
    public void setUp(){
        this.service = new BookServiceImpl(repository);
    }

    @Test
    @DisplayName("Deve salvar um livro")
    public void saveBookTest(){
        //cenário
        Book book = createValidBook();
        Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(false);
        Mockito.when( repository.save(book)).thenReturn(
                Book.builder()
                        .id(1L)
                        .isbn("123")
                        .title("As aventuras")
                        .author("Fulano")
                        .build()
        );

        //execução
        Book savedBook = service.save(book);

        //verificação
        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getIsbn()).isEqualTo("123");
        assertThat(savedBook.getTitle()).isEqualTo("As aventuras");
        assertThat(savedBook.getAuthor()).isEqualTo("Fulano");
    }

    private Book createValidBook() {
        return Book.builder().isbn("123").author("Fulano").title("As aventuras").build();
    }

    @Test
    @DisplayName("Deve lançar erro de negócio ao tentar salvar um livro com isbn duplicado")
    public void shouldNotSaveABookWithDuplicatedISBN(){
        //cenario
        Book book = createValidBook();
        Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(true);

        //execução
        Throwable exception = Assertions.catchThrowable( () -> service.save(book));

        //verificação
        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("Isbn já cadastrado.");

        Mockito.verify(repository, Mockito.never()).save(book);
    }

    @Test
    @DisplayName("Deve obter um livro por Id")
    public void getByIdTest(){
        Long id = 1L;
        Book book = createValidBook();
        book.setId(id);
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(book));

        //execução
        Optional<Book> foundBook = service.getById(id);

        //verificação
        assertThat( foundBook.isPresent()).isTrue();
        assertThat( foundBook.get().getId()).isEqualTo(id);
        assertThat( foundBook.get().getAuthor()).isEqualTo(book.getAuthor());
        assertThat( foundBook.get().getTitle()).isEqualTo(book.getTitle());
        assertThat( foundBook.get().getIsbn()).isEqualTo(book.getIsbn());
    }

    @Test
    @DisplayName("Deve retornar vazio ao obter um livro por Id quando ele não existir na base")
    public void bookNotFoundByIdTest(){
        Long id = 1L;
        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

        //execução
        Optional<Book> foundBook = service.getById(id);

        //verificação
        assertThat( foundBook.isPresent()).isFalse();
    }

    @Test
    @DisplayName("Deve deletar um livro por Id")
    public void deleteBookTest(){
        //cenário
        Book book = Book.builder().id(1L).build();

        //execução
        org.junit.jupiter.api.Assertions.assertDoesNotThrow( () -> service.delete(book));

        //verificação
        Mockito.verify(repository, Mockito.times(1)).delete(book);
    }

    @Test
    @DisplayName("Deve ocorrer erro ao tentar deletar um livro inexistente")
    public void deleteInvalidBookTest(){
        //cenário
        Book book = new Book();

        //execução
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> service.delete(book));

        //verificação
        Mockito.verify(repository, Mockito.never()).delete(book);
    }

    @Test
    @DisplayName("Deve atualizar um livro por Id")
    public void updateBookTest(){
        //cenário
        Long id = 1L;

        //livro a atualizar
        Book updatingBook = Book.builder().id(id).build();

        //simulação
        Book updatedBook = createValidBook();
        updatedBook.setId(id);

        Mockito.when(repository.save(updatingBook)).thenReturn(updatedBook);

        //execução
        Book book = service.update(updatingBook);

        //verificação
        assertThat(book.getId()).isEqualTo(updatedBook.getId());
        assertThat(book.getTitle()).isEqualTo(updatedBook.getTitle());
        assertThat(book.getAuthor()).isEqualTo(updatedBook.getAuthor());
        assertThat(book.getIsbn()).isEqualTo(updatedBook.getIsbn());

    }
    @Test
    @DisplayName("Deve ocorrer erro ao tentar atualizar um livro inexistente")
    public void updateInvalidBookTest(){
        //cenário
        Book book = new Book();

        //execução
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> service.update(book));

        //verificação
        Mockito.verify(repository, Mockito.never()).save(book);
    }

    @Test
    @DisplayName("Deve filtrar livros")
    public void findBookTest(){
        //cenário
        Book book = createValidBook();
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Book> list = Arrays.asList(book);
        Page<Book> page = new PageImpl<Book>(list, pageRequest, 1);
        Mockito.when(repository.findAll(Mockito.any(Example.class), Mockito.any(PageRequest.class)))
                .thenReturn(page);
        //execução
        Page<Book> result = service.find(book, pageRequest);

        //verificação
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).isEqualTo(list);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);
    }

}
