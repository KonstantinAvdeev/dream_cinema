package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.model.Film;
import ru.job4j.cinema.repository.FilmRepository;
import ru.job4j.cinema.repository.GenreRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SimpleFilmService implements FilmService {

    private final FilmRepository filmRepository;

    private final GenreRepository genreRepository;

    public SimpleFilmService(FilmRepository filmRepository, GenreRepository genreRepository) {
        this.filmRepository = filmRepository;
        this.genreRepository = genreRepository;
    }

    @Override
    public Optional<FilmDto> findById(int id) {
        return filmRepository.findById(id).stream().map(film -> new FilmDto(film.getId(), film.getName(), film.getDescription(),
                film.getYear(), genreName(film), film.getMinimalAge(), film.getDurationInMinutes(), film.getFileId())).findAny();
    }

    @Override
    public Collection<FilmDto> findAll() {
        return filmRepository.findAll().stream().map(film -> new FilmDto(film.getId(), film.getName(), film.getDescription(),
                        film.getYear(), genreName(film), film.getMinimalAge(), film.getDurationInMinutes(), film.getFileId()))
                .collect(Collectors.toList());
    }

    public String genreName(Film film) {
        String result = null;
        var genre = genreRepository.findById(film.getGenreId());
        if (genre.isPresent()) {
            result = genre.get().getName();
        }
        return result;
    }

}
