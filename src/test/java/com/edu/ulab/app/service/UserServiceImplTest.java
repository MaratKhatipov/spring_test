package com.edu.ulab.app.service;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.repository.UserRepository;
import com.edu.ulab.app.service.impl.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Тестирование функционала {@link com.edu.ulab.app.service.impl.UserServiceImpl}.
 */
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DisplayName("Testing user functionality.")
public class UserServiceImplTest {
    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Mock
    UserMapper userMapper;

    @Test
    @DisplayName("Создание пользователя. Должно пройти успешно.")
    void savePerson_Test() {
        //given

        UserDto userDto = new UserDto();
        userDto.setAge(11);
        userDto.setFullName("test name");
        userDto.setTitle("test title");

        Person person  = new Person();
        person.setFullName("test name");
        person.setAge(11);
        person.setTitle("test title");

        Person savedPerson  = new Person();
        savedPerson.setId(1);
        savedPerson.setFullName("test name");
        savedPerson.setAge(11);
        savedPerson.setTitle("test title");

        UserDto result = new UserDto();
        result.setId(1);
        result.setAge(1);
        result.setFullName("test name");
        result.setTitle("test title");


        //when

        when(userMapper.userDtoToPerson(userDto)).thenReturn(person);
        when(userRepository.save(person)).thenReturn(savedPerson);
        when(userMapper.personToUserDto(savedPerson)).thenReturn(result);


        //then

        UserDto userDtoResult = userService.createUser(userDto);
        assertEquals(1, userDtoResult.getId());
    }

    @Test
    @DisplayName("Обновление пользователя. Должно пройти успешно.")
    void updateUser() {

        UserDto updateUserDto = new UserDto();
        updateUserDto.setId(1);
        updateUserDto.setTitle("update TITLE");
        updateUserDto.setAge(22);
        updateUserDto.setFullName("new update name");

        Person updatePerson  = new Person();
        updatePerson.setId(1);
        updatePerson.setFullName("new update name");
        updatePerson.setAge(11);
        updatePerson.setTitle("update TITLE");

        UserDto updateResUserDto = new UserDto();
        updateResUserDto.setId(1);
        updateResUserDto.setTitle("update TITLE");
        updateResUserDto.setAge(22);
        updateResUserDto.setFullName("new update name");

        when(userMapper.userDtoToPerson(updateUserDto)).thenReturn(updatePerson);
        when((userRepository.findById(updatePerson.getId()))).thenReturn(Optional.of(updatePerson));
        when(userRepository.save(updatePerson)).thenReturn(updatePerson);
        when(userMapper.personToUserDto(updatePerson)).thenReturn(updateResUserDto);

        UserDto updatePersonRes = userService.updateUser(updateUserDto);
        assertEquals("update TITLE", updatePersonRes.getTitle());
        assertEquals("new update name", updatePersonRes.getFullName());
    }

    @Test
    @DisplayName("Получение пользователя. Должно пройти успешно.")
    void getUserById() {
        Person person  = new Person();
        person.setId(1);
        person.setFullName("test name");
        person.setAge(11);
        person.setTitle("test title");

        UserDto userDto = new UserDto();
        userDto.setId(1);
        userDto.setFullName("test name");
        userDto.setAge(11);
        userDto.setTitle("test title");

        when(userMapper.personToUserDto(person)).thenReturn(userDto);
        when(userRepository.findById(person.getId())).thenReturn(Optional.of(person));

        UserDto getById = userService.getUserById(1);
        assertEquals("test name", getById.getFullName());
        assertEquals("test title", getById.getTitle());
        assertEquals(11, getById.getAge());
        assertEquals(1, getById.getId());
    }

    @Test
    @DisplayName("Удаление пользователя. Должно пройти успешно.")
    void deleteUserById() {
        Person person  = new Person();
        person.setId(1);
        person.setFullName("test name");
        person.setAge(11);
        person.setTitle("test title");

        when(userRepository.findById(person.getId())).thenReturn(Optional.of(person));

        userService.deleteUserById(person.getId());
        verify(userRepository).deleteById(person.getId());
    }

    @Test
    @DisplayName("Персон отсутствует в репозитории - ошибка. Должно пройти успешно.")
    void searchUserWhichNotExists(){
        Integer notExistId = 1;
        when(userRepository.findById(notExistId)).thenReturn(Optional.empty());

        //then

        assertThatThrownBy(() -> userService.getUserById(notExistId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Can not fount user with ID: " + notExistId);
    }

    // * failed
    //         doThrow(dataInvalidException).when(testRepository)
    //                .save(same(test));
    // example failed
    //  assertThatThrownBy(() -> testeService.createTest(testRequest))
    //                .isInstanceOf(DataInvalidException.class)
    //                .hasMessage("Invalid data set");
}
