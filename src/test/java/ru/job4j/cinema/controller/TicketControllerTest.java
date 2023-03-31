package ru.job4j.cinema.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.service.TicketService;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TicketControllerTest {

    private static TicketController ticketController;
    private static TicketService ticketService;

    @BeforeEach
    public void initServices() {
        ticketService = mock(TicketService.class);
        ticketController = new TicketController(ticketService);
    }

    @Test
    public void whenBuyTicketThenGetSuccessPage() {
        var ticket1 = new Ticket(1, 1, 1, 1, 1);
        var expectedMessage = "Вы купили билет на 1 ряд 1 место";
        when(ticketService.save(any())).thenReturn(Optional.of(ticket1));

        var model = new ConcurrentModel();
        var view = ticketController.buyTicket(ticket1, model);
        var actualMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("tickets/ticketBought");
        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    public void whenBuyTicketThenGetErrorPage() {
        var expectedError = "Не удаётся купить билет на указанное место. Попробуйте другой "
                + "вариант.";
        when(ticketService.save(any())).thenReturn(Optional.empty());

        var model = new ConcurrentModel();
        var view = ticketController.buyTicket(any(), model);
        var actualMessage = model.getAttribute("error");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualMessage).isEqualTo(expectedError);
    }

}