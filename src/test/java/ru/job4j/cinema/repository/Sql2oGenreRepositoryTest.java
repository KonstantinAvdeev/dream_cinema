package ru.job4j.cinema.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.Genre;

import java.util.List;
import java.util.Properties;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Sql2oGenreRepositoryTest {

    private static Sql2oGenreRepository sql2oGenreRepository;
    private static Genre genre1 = new Genre(2, "Криминал");
    private static Genre genre2 = new Genre(11, "Ужасы");

    @BeforeAll
    public static void initRepositories() throws Exception {
        var properties = new Properties();
        try (var inputStream = Sql2oGenreRepositoryTest.class.getClassLoader().getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(url, username, password);
        var sql2o = configuration.databaseClient(datasource);

        sql2oGenreRepository = new Sql2oGenreRepository(sql2o);
    }

    @Test
    public void whenFindById() {
        var genre = sql2oGenreRepository.findById(2).get();
        assertThat(genre).isEqualTo(genre1);
    }

    @Test
    public void whenFindAll() {
        var genres = sql2oGenreRepository.findAll();
        assertThat(genres.containsAll(List.of(genre1, genre2)));
    }

}