package ru.job4j.cinema.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.cinema.service.FilmSessionService;
import ru.job4j.cinema.service.HallService;

@ThreadSafe
@Controller
@RequestMapping("/filmSessions")
public class FilmSessionController {

    private final FilmSessionService filmSessionService;
    private final HallService hallService;

    public FilmSessionController(FilmSessionService filmSessionService, HallService hallService) {
        this.filmSessionService = filmSessionService;
        this.hallService = hallService;
    }

    @GetMapping("/{id}")
    public String getSessionById(Model model, @PathVariable int id) {
        var sessionOptional = filmSessionService.findById(id);
        if (sessionOptional.isEmpty()) {
            model.addAttribute("error", "Выбран неверный сеанс, попробуйте выюрать другой!");
            return "errors/404";
        }
        model.addAttribute("filmSession", sessionOptional.get());
        model.addAttribute("rowNumbers", hallService.getRowsByName(sessionOptional.get().getHallName()));
        model.addAttribute("placeNumbers", hallService.getPlacesByName(sessionOptional.get().getHallName()));
        return "tickets/buy";
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("filmSessions", filmSessionService.findAll());
        return "filmSessions/list";
    }

}