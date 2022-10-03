package com.edu.ulab.app.service;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.repository.BookRepository;
import com.edu.ulab.app.service.impl.BookServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Тестирование функционала {@link com.edu.ulab.app.service.impl.BookServiceImpl}.
 */
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DisplayName("Testing book functionality.")
public class BookServiceImplTest {
    @InjectMocks
    BookServiceImpl bookService;

    @Mock
    BookRepository bookRepository;

    @Mock
    BookMapper bookMapper;

    @Test
    @DisplayName("Создание книги. Должно пройти успешно.")
    void saveBook_Test() {
        //given
        Person person  = new Person();
        person.setId(1);

        BookDto bookDto = new BookDto();
        bookDto.setUserId(1);
        bookDto.setAuthor("test author");
        bookDto.setTitle("test title");
        bookDto.setPageCount(1000);

        BookDto result = new BookDto();
        result.setId(1);
        result.setUserId(1);
        result.setAuthor("test author");
        result.setTitle("test title");
        result.setPageCount(1000);

        Book book = new Book();
        book.setPageCount(1000);
        book.setTitle("test title");
        book.setAuthor("test author");
        book.setPerson(person);

        Book savedBook = new Book();
        savedBook.setId(1);
        savedBook.setPageCount(1000);
        savedBook.setTitle("test title");
        savedBook.setAuthor("test author");
        savedBook.setPerson(person);

        //when

        when(bookMapper.bookDtoToBook(bookDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(savedBook);
        when(bookMapper.bookToBookDto(savedBook)).thenReturn(result);


        //then
        BookDto bookDtoResult = bookService.createBook(bookDto);
        assertEquals(1, bookDtoResult.getId());
    }

    @Test
    @DisplayName("Update книги. Должно пройти успешно.")
    void updateBook_Test() {
        Person person  = new Person();
        person.setId(1);

        BookDto updateBookDto = new BookDto();
        updateBookDto.setId(1);
        updateBookDto.setUserId(1);
        updateBookDto.setAuthor("update author");
        updateBookDto.setTitle("update title");
        updateBookDto.setPageCount(3244);

        Book updateBook = new Book();
        updateBook.setId(1);
        updateBook.setAuthor("update author");
        updateBook.setTitle("update title");
        updateBook.setPageCount(3244);
        updateBook.setPerson(person);

        BookDto updateResBookDto = new BookDto();
        updateResBookDto.setId(1);
        updateResBookDto.setUserId(1);
        updateResBookDto.setPageCount(3244);
        updateResBookDto.setTitle("update title");
        updateResBookDto.setAuthor("update author");

        when(bookMapper.bookDtoToBook(updateBookDto)).thenReturn(updateBook);
        when(bookRepository.findById(1)).thenReturn(Optional.of(updateBook));
        when(bookRepository.save(updateBook)).thenReturn(updateBook);
        when(bookMapper.bookToBookDto(updateBook)).thenReturn(updateResBookDto);

        BookDto bookDtoRes = bookService.updateBook(updateResBookDto);
        assertEquals("update author", bookDtoRes.getAuthor());
        assertEquals("update title", bookDtoRes.getTitle());
        assertEquals(3244, bookDtoRes.getPageCount());
    }

    @Test
    @DisplayName("Получение книги по ID. Должно пройти успешно.")
    void getBookById() {
        //given
        Person person  = new Person();
        person.setId(1);

        BookDto bookDto = new BookDto();
        bookDto.setUserId(1);
        bookDto.setAuthor("test author");
        bookDto.setTitle("test title");
        bookDto.setPageCount(1000);

        BookDto result = new BookDto();
        result.setId(1);
        result.setUserId(1);
        result.setAuthor("test author");
        result.setTitle("test title");
        result.setPageCount(1000);

        Book book = new Book();
        book.setPageCount(1000);
        book.setTitle("test title");
        book.setAuthor("test author");
        book.setPerson(person);

        Book savedBook = new Book();
        savedBook.setId(1);
        savedBook.setPageCount(1000);
        savedBook.setTitle("test title");
        savedBook.setAuthor("test author");
        savedBook.setPerson(person);

        //when

        when(bookMapper.bookDtoToBook(bookDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(savedBook);
        when(bookMapper.bookToBookDto(savedBook)).thenReturn(result);
        when(bookRepository.findById(1)).thenReturn(Optional.of(savedBook));


        //then
        BookDto bookDtoResult = bookService.getBookById(1);
        assertEquals(1, bookDtoResult.getId());
    }

    @Test
    @DisplayName("Удаление книги по IDю Должно пройти успешно")
    void deleteBookById_Test() {
        Book book = new Book();
        book.setId(1);
        book.setTitle("test title");
        book.setAuthor("test author");
        book.setPageCount(100);



        bookService.deleteBookById(book.getId());
        verify(bookRepository).deleteById(book.getId());
    }

    @DisplayName("Получение книг по ID юзера. Должно пройти успешно.")
    @Test
    void getBookByUserId_Test() {
        Person person  = new Person();
        person.setId(1);

        BookDto firstBookDto = new BookDto();
        firstBookDto.setUserId(1);
        firstBookDto.setAuthor("first author");
        firstBookDto.setTitle("first title");
        firstBookDto.setPageCount(1000);

        BookDto secondBookDto = new BookDto();
        secondBookDto.setUserId(1);
        secondBookDto.setAuthor("second author");
        secondBookDto.setTitle("second title");
        secondBookDto.setPageCount(2000);

        Book firstBook = new Book();
        firstBook.setId(1);
        firstBook.setAuthor("first author");
        firstBook.setTitle("first title");
        firstBook.setPageCount(1000);
        firstBook.setPerson(person);

        Book secondBook = new Book();
        secondBook.setId(2);
        secondBook.setAuthor("second author");
        secondBook.setTitle("second title");
        secondBook.setPageCount(2000);
        secondBook.setPerson(person);

        List<Book> books = Arrays.asList(firstBook, secondBook);
        List<BookDto> result = Arrays.asList(firstBookDto, secondBookDto);

        when(bookMapper.bookToBookDto(firstBook)).thenReturn(firstBookDto);
        when(bookMapper.bookToBookDto(secondBook)).thenReturn(secondBookDto);
        when(bookRepository.findAllByPersonId(person.getId())).thenReturn(books);

        List<BookDto> resultBooksDto = bookService.getBookByUserId(person.getId());

        assertEquals(result, resultBooksDto);
    }


    @DisplayName("Удаление книг по ID юзера. Должно пройти успешно.")
    @Test
    void deleteBooksByUserId() {
        bookService.deleteBooksByUserId(1);

        verify(bookRepository).deleteByPersonId(1);
    }

    @Test
    @DisplayName("Поиск несуществующей книги - ошибка. Должно пройти успешно.")
    void searchBooksByUserIdWhichHaveNotBooks(){
        Integer notExistId = 1;
        when(bookRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.getBookById(notExistId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Can not fount book with ID: " + notExistId);
    }

    // * failed
}
