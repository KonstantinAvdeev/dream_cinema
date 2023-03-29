package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.User;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.List;
import java.util.Properties;

class Sql2oUserRepositoryTest {

    private static Sql2oUserRepository sql2oUserRepository;

    @BeforeAll
    public static void initRepositories() throws Exception {
        var properties = new Properties();
        try (var inputStream = Sql2oUserRepositoryTest.class.getClassLoader().getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(url, username, password);
        var sql2o = configuration.databaseClient(datasource);

        sql2oUserRepository = new Sql2oUserRepository(sql2o);
    }

    @AfterEach
    public void clearUsers() {
        var users = sql2oUserRepository.findAll();
        for (var user : users) {
            sql2oUserRepository.deleteById(user.getId());
        }
    }

    @Test
    public void whenSaveThenGetSame() {
        User test = new User(1, "Kolya", "111@mail.ru", "123456");
        var user = sql2oUserRepository.save(test);
        var savedUser = sql2oUserRepository.findByEmailAndPassword(test.getEmail(), test.getPassword());
        assertThat(savedUser).usingRecursiveComparison().isEqualTo(user);
    }

    @Test
    public void whenSaveTwoTimesSameUser() {
        User user = new User(1, "Kolya", "111@mail.ru", "123456");
        var savedUser = sql2oUserRepository.save(user);
        assertThat(savedUser.get()).usingRecursiveComparison().isEqualTo(user);
        var savedUser2 = sql2oUserRepository.save(user);
        assertThat(savedUser2).isEmpty();
    }


    @Test
    public void whenSaveSeveralThenGetAll() {
        var user1 = sql2oUserRepository.save(new User(1, "Kolya", "111@mail.ru", "123456"));
        var user2 = sql2oUserRepository.save(new User(2, "Zhenya", "222@mail.ru", "123456"));
        var user3 = sql2oUserRepository.save(new User(3, "Anton", "333@mail.ru", "123456"));
        var result = sql2oUserRepository.findAll();
        assertThat(result).isEqualTo(List.of(user1.get(), user2.get(), user3.get()));
    }

    @Test
    public void whenDidNotSaveThenNothingFound() {
        assertThat(sql2oUserRepository.findAll()).isEqualTo(emptyList());
        var savedUser = sql2oUserRepository.findByEmailAndPassword("111@mail.ru", "123456");
        assertThat(savedUser).isEmpty();
    }

    @Test
    public void whenDeleteThenGetEmptyOptional() {
        var user = sql2oUserRepository.save(new User(1, "Kolya", "111@mail.ru", "123456"));
        var isDeleted = sql2oUserRepository.deleteById(user.get().getId());
        var savedUser = sql2oUserRepository.findByEmailAndPassword("111@mail.ru", "123456");
        assertThat(isDeleted).isTrue();
        assertThat(savedUser).isEmpty();
    }

    @Test
    public void whenDeleteByInvalidIdThenGetFalse() {
        assertThat(sql2oUserRepository.deleteById(0)).isFalse();
    }

}