package ru.job4j.cinema.repository;

import net.jcip.annotations.ThreadSafe;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;
import ru.job4j.cinema.model.Ticket;

import java.util.Optional;

@ThreadSafe
@Repository
public class Sql2oTicketRepository implements TicketRepository {

    private static final Logger LOG = LogManager.getLogger(Sql2oUserRepository.class.getName());

    private final Sql2o sql2o;

    public Sql2oTicketRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Optional<Ticket> save(Ticket ticket) {
        try (var connection = sql2o.open()) {
            var sql = """
                    INSERT INTO tickets(session_id, row_number, place_number, user_id)
                    VALUES (:sessionId, :rowNumber, :placeNumber, :userId)
                    """;
            var query = connection.createQuery(sql, true)
                    .addParameter("sessionId", ticket.getSessionId())
                    .addParameter("rowNumber", ticket.getRowNumber())
                    .addParameter("placeNumber", ticket.getPlaceNumber())
                    .addParameter("userId", ticket.getUserId());
            int generatedId = query.executeUpdate().getKey(Integer.class);
            ticket.setId(generatedId);
            return Optional.of(ticket);
        } catch (Sql2oException exception) {
            LOG.error("Can not bye ticket, some exception: ", exception);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Ticket> findById(int id) {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("SELECT * FROM tickets WHERE id = :id").addParameter("id", id);
            var ticket = query.setColumnMappings(Ticket.COLUMN_MAPPING).executeAndFetchFirst(Ticket.class);
            return Optional.ofNullable(ticket);
        }
    }

}