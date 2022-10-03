package com.edu.ulab.app.repository;

import com.edu.ulab.app.config.SystemJpaTest;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.exception.NotFoundException;
import com.vladmihalcea.sql.SQLStatementCountValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static com.vladmihalcea.sql.SQLStatementCountValidator.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Тесты репозитория {@link BookRepository}.
 */
@SystemJpaTest
public class BookRepositoryTest {
    @Autowired
    BookRepository bookRepository;
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        SQLStatementCountValidator.reset();
    }

    @DisplayName("Сохранить книгу и автора. Число select должно равняться 1")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void findAllBadges_thenAssertDmlCount() {
        //Given

        Person person = new Person();
        person.setAge(111);
        person.setTitle("reader");
        person.setFullName("Test Test");

        Person savedPerson = userRepository.save(person);

        Book book = new Book();
        book.setAuthor("Test Author");
        book.setTitle("test");
        book.setPageCount(1000);
        book.setPerson(savedPerson);

        //When
        Book result = bookRepository.save(book);

        //Then
        assertThat(result.getPageCount()).isEqualTo(1000);
        assertThat(result.getTitle()).isEqualTo("test");
        assertSelectCount(2);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    @DisplayName("Обновление книги. Число select должно равняться 1")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void updateBook_thenAssertDmlCount() {
        Optional<Book> bookFindById = bookRepository.findById(3003);
        Book book = bookFindById.get();

        assertEquals(1001, book.getPerson().getId());
        assertEquals("more default book", book.getTitle());
        assertEquals("on more author", book.getAuthor());
        assertEquals(6655, book.getPageCount());

        book.setTitle("new title");
        book.setAuthor("new author");
        book.setPageCount(30);

        bookRepository.save(book);

        Optional<Book> updateBookId = bookRepository.findById(3003);
        Book updateBook = updateBookId.get();

        assertEquals("new title", updateBook.getTitle());
        assertEquals("new author", updateBook.getAuthor());
        assertEquals(30, updateBook.getPageCount());

        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    @DisplayName("Получение книги из репозитория по id. Число select должно равняться 1")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void getBookById_thenAssertDmlCount() {
        Optional<Book> bookFindById = bookRepository.findById(3003);
        Book book = bookFindById.get();

        assertEquals(1001, book.getPerson().getId());
        assertEquals("more default book", book.getTitle());
        assertEquals("on more author", book.getAuthor());
        assertEquals(6655, book.getPageCount());

        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    @DisplayName("Получение всех книг из репозитория. Число select должно равняться 1")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void getBooksByUserId_thenAssertDmlCount() {
        Integer userId = 1001;
        List<Book> bookList = bookRepository.findAllByPersonId(userId);

        assertEquals(2, bookList.size());
        assertEquals(1001, bookList.get(0).getPerson().getId());
        assertEquals("default book", bookList.get(0).getTitle());
        assertEquals("author", bookList.get(0).getAuthor());
        assertEquals(5500, bookList.get(0).getPageCount());

        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    @DisplayName("Удаление книги из репозитория по id. Число select должно равняться 2")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void deleteBookById_thenAssertDmlCount() {
        bookRepository.deleteById(2002);

        assertEquals(1, bookRepository.findAllByPersonId(1001).size());

        assertSelectCount(2);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(1);
    }

    @DisplayName("Поиск несуществующей книги по id. Число select должно равняться 1")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void bookByIdIsNotExist_thenAssertDmlCount() {
        Integer id = 10;
        assertThatThrownBy(() -> {
            bookRepository.findById(id).orElseThrow(() -> new NotFoundException("Can not fount book with ID: " + id));
        }).isInstanceOf(NotFoundException.class);

        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }
    // example failed test
}
