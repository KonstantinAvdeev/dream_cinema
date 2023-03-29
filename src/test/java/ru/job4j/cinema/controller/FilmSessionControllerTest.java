package ru.job4j.cinema.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.cinema.dto.FilmSessionDto;
import ru.job4j.cinema.service.FilmSessionService;
import ru.job4j.cinema.service.HallService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FilmSessionControllerTest {

    private static FilmSessionController filmSessionController;
    private static FilmSessionService filmSessionService;
    private static HallService hallService;

    @BeforeEach
    public void initServices() {
        filmSessionService = mock(FilmSessionService.class);
        hallService = mock(HallService.class);
        filmSessionController = new FilmSessionController(filmSessionService, hallService);
    }

    @Test
    public void whenRequestFilmSessionListPageThenGetPageWithFilmSessions() {
        var filmSession1 = new FilmSessionDto(1, "Сплит", "Большой зал", LocalDateTime.of(2023, 03, 30, 01, 00, 00), LocalDateTime.of(2023, 03, 30, 02, 47, 00), "150");
        var filmSession2 = new FilmSessionDto(2, "Выживший", "Малый зал", LocalDateTime.of(2023, 03, 30, 01, 24, 00), LocalDateTime.of(2023, 03, 30, 04, 00, 00), "250");
        Collection<FilmSessionDto> expectedFilmSessions = List.of(filmSession1, filmSession2);
        when(filmSessionService.findAll()).thenReturn(expectedFilmSessions);

        var model = new ConcurrentModel();
        var view = filmSessionController.getAll(model);
        var actualFilmSessions = model.getAttribute("filmSessions");

        assertThat(view).isEqualTo("filmSessions/list");
        assertThat(actualFilmSessions).isEqualTo(expectedFilmSessions);
    }

    @Test
    public void whenRequestFilmSessionByIdThenGetTicketBuyPage() {
        var filmSession1 = Optional.of(new FilmSessionDto(1, "Сплит", "Большой зал", LocalDateTime.of(2023, 03, 30, 01, 00, 00), LocalDateTime.of(2023, 03, 30, 02, 47, 00), "150"));
        var expectedRowList = List.of(1, 2, 3, 4, 5);
        var expectedPlaceList = List.of(1, 2, 3, 4, 5);
        when(filmSessionService.findById(anyInt())).thenReturn(filmSession1);
        when(hallService.getRowsByName(anyString())).thenReturn(expectedRowList);
        when(hallService.getPlacesByName(anyString())).thenReturn(expectedPlaceList);

        var model = new ConcurrentModel();
        var view = filmSessionController.getSessionById(model, 1);
        var actualRows = model.getAttribute("rowNumbers");
        var actualPlaces = model.getAttribute("placeNumbers");

        assertThat(view).isEqualTo("tickets/buy");
        assertThat(actualRows).isEqualTo(expectedRowList);
        assertThat(actualPlaces).isEqualTo(expectedPlaceList);
    }

}