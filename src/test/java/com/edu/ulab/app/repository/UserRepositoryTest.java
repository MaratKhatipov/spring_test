package com.edu.ulab.app.repository;

import com.edu.ulab.app.config.SystemJpaTest;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тесты репозитория {@link UserRepository}.
 */
@SystemJpaTest
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        SQLStatementCountValidator.reset();
    }

    @DisplayName("Сохранить юзера. Число select должно равняться 1")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void insertPerson_thenAssertDmlCount() {
        //Given
        Person person = new Person();
        person.setAge(111);
        person.setTitle("reader");
        person.setFullName("Test Test");

        //When
        Person result = userRepository.save(person);

        //Then
        assertThat(result.getAge()).isEqualTo(111);
        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    @DisplayName("Обновление юзера. Число select должно равняться 1")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void updatePerson_thenAssertDmlCount() {
        Optional<Person> personId = userRepository.findById(1001);

        Person person = personId.get();

        assertEquals("reader", person.getTitle());
        assertEquals("default uer", person.getFullName());
        assertEquals(55, person.getAge());

        person.setTitle("upgrade Title");
        person.setFullName("new FullName");
        person.setAge(100);

        userRepository.save(person);

        Optional<Person> updatedPerson = userRepository.findById(1001);
        Person newPerson = updatedPerson.get();

        assertEquals("upgrade Title", newPerson.getTitle());
        assertEquals("new FullName", newPerson.getFullName());
        assertEquals(100, newPerson.getAge());

        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    @DisplayName("Получение юзера по id. Число select должно равняться 1")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void getPerson_thenAssertDmlCount() {
        Optional<Person> personFindById = userRepository.findById(1001);
        Person person = personFindById.get();

        assertEquals("reader", person.getTitle());
        assertEquals("default uer", person.getFullName());
        assertEquals(55, person.getAge());

        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    @DisplayName("Удаление юзера из репозитория по id. Число select должно равняться 3, delete - 3")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void deleteUserById_thenAssertDmlCount() {

        userRepository.deleteById(1001);
        List<Person> emptyList = userRepository.findAll();

        assertTrue(emptyList.isEmpty());

        assertSelectCount(3);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(3);
    }

    @DisplayName("Поиск несуществующего юзера в репозитории по id. Число select должно равняться 1")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void personByIdIsNotExist_thenAssertDmlCount() {
        Integer id = 10;
        assertThatThrownBy(() -> {
            userRepository.findById(id).orElseThrow(() -> new NotFoundException("Can not fount user with ID: " + id));
        }).isInstanceOf(NotFoundException.class);

        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }
    // * failed
}
