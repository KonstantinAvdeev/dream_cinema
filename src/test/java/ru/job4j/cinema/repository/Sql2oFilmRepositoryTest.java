package ru.job4j.cinema.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.Film;

import java.util.List;
import java.util.Properties;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Sql2oFilmRepositoryTest {

    private static Sql2oFilmRepository sql2oFilmRepository;

    private static final Film FILM_1 = new Film(3, "Крушение", "Пилоту Броуди Торрансу удаётся "
            + "успешно посадить повреждённый штормом самолёт на \n\t   враждебной территории. "
            + "Вскоре выясняется, что уцелевшим угрожают местные повстанцы, которые \n\t   хотят "
            + "захватить пассажиров в заложники. Броуди должен защитить людей, пока не "
            + "прибудет помощь.", 2023, 4, 16, 107, 3);
    private static final Film FILM_2 = new Film(10, "Брат 2", "Участвуя в программе на "
            + "телевидении, Данила Багров встречает своих друзей по службе Чечне. Одного из них "
            + "внезапно убивают. Выясняется, что у того были неприятности из-за брата-хоккеиста в "
            + "Америке. Данила должен разобраться. Он вылетает в Америку и за компанию берёт "
            + "с собой старшего брата.", 2000, 4, 16, 127, 10);

    @BeforeAll
    public static void initRepositories() throws Exception {
        var properties = new Properties();
        try (var inputStream = Sql2oFilmRepositoryTest.class.getClassLoader()
                .getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(url, username, password);
        var sql2o = configuration.databaseClient(datasource);

        sql2oFilmRepository = new Sql2oFilmRepository(sql2o);
    }

    @Test
    public void whenFindById() {
        var film1 = sql2oFilmRepository.findById(3).get();
        assertThat(film1).isEqualTo(FILM_1);
    }

    @Test
    public void whenFindAll() {
        var films = sql2oFilmRepository.findAll();
        assertThat(films.containsAll(List.of(FILM_1, FILM_2)));
    }

}