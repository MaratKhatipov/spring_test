package com.edu.ulab.app.entity;


import lombok.*;

import javax.persistence.*;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "person", schema = "ulab_edu")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    @SequenceGenerator(name = "sequence", sequenceName = "sequence", allocationSize = 100)
    private Integer id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private int age;

//    @Column(nullable = false)
//    private int count;
/*
Добавил CascadeType.REMOVE т.к. на id ссылается таблица «book».
 */
    @OneToMany(mappedBy = "person", cascade = {
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.DETACH,
            CascadeType.REMOVE,
            CascadeType.REFRESH})
    private Set<Book> bookSet;
}
