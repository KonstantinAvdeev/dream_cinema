package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.Ticket;

import java.util.List;
import java.util.Properties;

import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Sql2oTicketRepositoryTest {

    private static Sql2oTicketRepository sql2oTicketRepository;
    private static Ticket ticket1 = new Ticket(1, 1, 1, 1, 1);
    private static Ticket ticket2 = new Ticket(2, 2, 2, 2, 2);

    @BeforeAll
    public static void initRepositories() throws Exception {
        var properties = new Properties();
        try (var inputStream = Sql2oTicketRepositoryTest.class.getClassLoader()
                .getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(url, username, password);
        var sql2o = configuration.databaseClient(datasource);

        sql2oTicketRepository = new Sql2oTicketRepository(sql2o);
    }

    @AfterEach
    public void clearTickets() {
        var tickets = sql2oTicketRepository.findAll();
        for (var ticket : tickets) {
            sql2oTicketRepository.deleteById(ticket.getId());
        }
    }

    @Test
    public void whenSaveThenGetSame() {
        var ticket = sql2oTicketRepository.save(new Ticket(1, 1, 1, 1, 1));
        var savedTicket = sql2oTicketRepository.findById(ticket.get().getId());
        assertThat(savedTicket).usingRecursiveComparison().isEqualTo(ticket);
    }

    @Test
    public void whenSaveSeveralThenGetAll() {
        var ticket1 = sql2oTicketRepository.save(new Ticket(1, 1, 1, 1, 1));
        var ticket2 = sql2oTicketRepository.save(new Ticket(2, 2, 2, 2, 1));
        var ticket3 = sql2oTicketRepository.save(new Ticket(3, 3, 3, 3, 1));
        var result = sql2oTicketRepository.findAll();
        assertThat(result).isEqualTo(List.of(ticket1.get(), ticket2.get(), ticket3.get()));
    }

    @Test
    public void whenDontSaveThenNothingFound() {
        assertThat(sql2oTicketRepository.findAll()).isEqualTo(emptyList());
        assertThat(sql2oTicketRepository.findById(0)).isEqualTo(empty());
    }

    @Test
    public void whenDeleteThenGetEmptyOptional() {
        var ticket = sql2oTicketRepository.save(new Ticket(1, 1, 1, 1, 1));
        var isDeleted = sql2oTicketRepository.deleteById(ticket.get().getId());
        var savedTicket = sql2oTicketRepository.findById(ticket.get().getId());
        assertThat(isDeleted).isTrue();
        assertThat(savedTicket).isEqualTo(empty());
    }

    @Test
    public void whenDeleteByInvalidIdThenGetFalse() {
        assertThat(sql2oTicketRepository.deleteById(0)).isFalse();
    }

}