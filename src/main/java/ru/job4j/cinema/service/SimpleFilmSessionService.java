package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.dto.FilmSessionDto;
import ru.job4j.cinema.model.FilmSession;
import ru.job4j.cinema.model.Hall;
import ru.job4j.cinema.repository.FilmSessionRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SimpleFilmSessionService implements FilmSessionService {

    private final FilmSessionRepository filmSessionRepository;
    private final FilmService filmService;
    private final HallService hallService;

    public SimpleFilmSessionService(FilmSessionRepository filmSessionRepository,
                                    FilmService filmService, HallService hallService) {
        this.filmSessionRepository = filmSessionRepository;
        this.filmService = filmService;
        this.hallService = hallService;
    }

    @Override
    public Optional<FilmSessionDto> findById(int id) {
        Optional<FilmSessionDto> result = Optional.empty();
        Optional<FilmSession> optionalFilmSession = filmSessionRepository.findById(id);
        if (optionalFilmSession.isPresent()) {
            var filmSessionDto = new FilmSessionDto(optionalFilmSession.get().getId(),
                    filmName(optionalFilmSession.get()),
                    hallName(optionalFilmSession.get()), optionalFilmSession.get().getStartTime(),
                    optionalFilmSession.get().getEndTime(),
                    String.valueOf(optionalFilmSession.get().getPrice()));
            result = Optional.ofNullable(filmSessionDto);
        }
        return result;
    }

    @Override
    public Collection<FilmSessionDto> findAll() {
        return filmSessionRepository.findAll().stream().map(filmSession ->
                        new FilmSessionDto(filmSession.getId(), filmName(filmSession),
                                hallName(filmSession), filmSession.getStartTime(),
                                filmSession.getEndTime(), String.valueOf(filmSession.getPrice())))
                .collect(Collectors.toList());
    }

    @Override
    public String filmName(FilmSession filmSession) {
        String result = null;
        Optional<FilmDto> filmOptional = filmService.findById(filmSession.getFilmId());
        if (filmOptional.isPresent()) {
            result = filmOptional.get().getName();
        }
        return result;
    }

    @Override
    public String hallName(FilmSession filmSession) {
        String result = null;
        Optional<Hall> hallOptional = hallService.findById(filmSession.getHallsId());
        if (hallOptional.isPresent()) {
            result = hallOptional.get().getName();
        }
        return result;
    }

}