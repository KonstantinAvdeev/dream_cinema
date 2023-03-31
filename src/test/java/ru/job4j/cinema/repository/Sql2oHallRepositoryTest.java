package ru.job4j.cinema.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.Hall;

import java.util.List;
import java.util.Properties;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Sql2oHallRepositoryTest {

    private static Sql2oHallRepository sql2oHallRepository;
    private static Hall hall1 = new Hall(1, "Большой зал", 20, 30, "Большой зал на 600 мест");
    private static Hall hall2 = new Hall(2, "Малый зал", 8, 10, "Небольшой зал "
            + "повышенного комфорта на 80 мест");

    @BeforeAll
    public static void initRepositories() throws Exception {
        var properties = new Properties();
        try (var inputStream = Sql2oHallRepositoryTest.class.getClassLoader()
                .getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(url, username, password);
        var sql2o = configuration.databaseClient(datasource);

        sql2oHallRepository = new Sql2oHallRepository(sql2o);
    }

    @Test
    public void whenFindById() {
        var hall = sql2oHallRepository.findById(2).get();
        assertThat(hall).isEqualTo(hall2);
    }

    @Test
    public void whenFindAll() {
        var halls = sql2oHallRepository.findAll();
        assertThat(halls.containsAll(List.of(hall1, hall2)));
    }

    @Test
    public void whenFindByName() {
        var hall = sql2oHallRepository.findByName("Большой зал").get();
        assertThat(hall).isEqualTo(hall1);
    }


}