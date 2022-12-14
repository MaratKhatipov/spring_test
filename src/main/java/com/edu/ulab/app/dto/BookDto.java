package com.edu.ulab.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {
    private Integer id;
    private Integer userId;
    private String title;
    private String author;
    private long pageCount;
}
