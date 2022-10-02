package com.edu.ulab.app.service;


import com.edu.ulab.app.dto.BookDto;

import java.util.List;

public interface BookService {
    BookDto createBook(BookDto bookDto);

    BookDto updateBook(BookDto bookDto);

    BookDto getBookById(Integer id);

    void deleteBookById(Integer id);

    List<BookDto> getBookByUserId(Integer id);

    void deleteBooksByUserId(Integer id);
}
