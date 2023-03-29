package ru.job4j.cinema.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.service.TicketService;

@ThreadSafe
@Controller
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("/buy")
    public String buyTicket(@ModelAttribute Ticket ticket, Model model) {
        var ticketOptional = ticketService.save(ticket);
        if (ticketOptional.isEmpty()) {
            model.addAttribute("error", "Не удаётся купить билет на указанное место. Попробуйте "
                    + "другой вариант.");
            return "errors/404";
        }
        String ticketBought = String.format("Вы купили билет на %s ряд %s место", ticketOptional.get().getRowNumber(),
                ticketOptional.get().getPlaceNumber());
        model.addAttribute("message", ticketBought);
        return "tickets/ticketBought";
    }

}