package ru.job4j.cinema.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.FilmSession;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Properties;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Sql2oFilmSessionRepositoryTest {

    private static Sql2oFilmSessionRepository filmSessionRepository;
    private static FilmSession filmSession1 = new FilmSession(1, 9, 1,
            LocalDateTime.of(2023, 03, 30, 01, 00, 00),
            LocalDateTime.of(2023, 03, 30, 02, 47, 00), 150);

    private static FilmSession filmSession2 = new FilmSession(2, 7, 2,
            LocalDateTime.of(2023, 03, 30, 01, 24, 00),
            LocalDateTime.of(2023, 03, 30, 04, 00, 00), 250);

    @BeforeAll
    public static void initRepositories() throws Exception {
        var properties = new Properties();
        try (var inputStream = Sql2oFilmSessionRepository.class.getClassLoader()
                .getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(url, username, password);
        var sql2o = configuration.databaseClient(datasource);

        filmSessionRepository = new Sql2oFilmSessionRepository(sql2o);
    }

    @Test
    public void whenFindById() {
        var filmSession = filmSessionRepository.findById(1).get();
        assertThat(filmSession).isEqualTo(filmSession1);
    }

    @Test
    public void whenFindAll() {
        var filmSessions = filmSessionRepository.findAll();
        assertThat(filmSessions.containsAll(List.of(filmSession1, filmSession2)));
    }

}