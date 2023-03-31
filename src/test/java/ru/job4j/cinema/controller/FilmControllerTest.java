package ru.job4j.cinema.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.service.FilmService;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FilmControllerTest {

    private FilmService filmService;
    private FilmController filmController;

    @BeforeEach
    public void initServices() {
        filmService = mock(FilmService.class);
        filmController = new FilmController(filmService);
    }

    @Test
    public void whenRequestFilmListPageThenGetPageWithFilms() {
        var film1 = new FilmDto(3, "Крушение", "Пилоту Броуди Торрансу удаётся успешно "
                + "посадить повреждённый штормом самолёт на враждебной территории. Вскоре "
                + "выясняется, что уцелевшим угрожают местные повстанцы, которые хотят захватить "
                + "пассажиров в заложники. Броуди должен защитить людей, пока не прибудет помощь.",
                2023, "Боевик", 16, 107, 3);
        var film2 = new FilmDto(10, "Брат 2", "Участвуя в программе на телевидении, "
                + "Данила Багров встречает своих друзей по службе Чечне. Одного из них внезапно "
                + "убивают. Выясняется, что у того были неприятности из-за брата-хоккеиста в "
                + "Америке. Данила должен разобраться. Он вылетает в Америку и за компанию "
                + "берёт с собой старшего брата.", 2000, "Боевик", 16,
                127, 10);
        var expectedFilms = List.of(film1, film2);
        when(filmService.findAll()).thenReturn(expectedFilms);

        var model = new ConcurrentModel();
        var view = filmController.getAll(model);
        var actualFilms = model.getAttribute("films");

        assertThat(view).isEqualTo("films/list");
        assertThat(actualFilms).isEqualTo(expectedFilms);
    }

}