package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.TicketDto;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.repository.TicketRepository;

import java.util.Collection;
import java.util.Optional;

@Service
public class SimpleTicketService implements TicketService {

    private final TicketRepository ticketRepository;
    private final FilmSessionService filmSessionService;

    public SimpleTicketService(TicketRepository ticketRepository,
                               FilmSessionService filmSessionService) {
        this.ticketRepository = ticketRepository;
        this.filmSessionService = filmSessionService;
    }

    @Override
    public Optional<Ticket> save(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    @Override
    public Optional<TicketDto> findById(int id) {
        Optional<TicketDto> result = Optional.empty();
        Optional<Ticket> ticketOptional = ticketRepository.findById(id);
        if (ticketOptional.isPresent()) {
            var ticketDto = new TicketDto(ticketOptional.get().getId(),
                    filmSessionService.findById(ticketOptional.get().getSessionId()).get()
                            .getFilmName(),
                    filmSessionService.findById(ticketOptional.get().getSessionId()).get()
                            .getHallName(),
                    filmSessionService.findById(ticketOptional.get().getSessionId()).get()
                            .getStartTime(),
                    filmSessionService.findById(ticketOptional.get().getSessionId()).get()
                            .getEndTime(),
                    ticketOptional.get().getRowNumber(), ticketOptional.get().getPlaceNumber());
            result = Optional.ofNullable(ticketDto);
        }
        return result;
    }

    @Override
    public Collection<Ticket> findAll() {
        return ticketRepository.findAll();
    }

    @Override
    public boolean deleteById(int id) {
        return ticketRepository.deleteById(id);
    }

}