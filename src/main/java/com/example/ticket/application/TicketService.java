package com.example.ticket.application;

import com.example.ticket.domain.model.EstadoTicket;
import com.example.ticket.domain.model.Ticket;
import com.example.ticket.domain.port.in.ConsultarEstadoTicketUseCase;
import com.example.ticket.domain.port.in.RegistrarTicketUseCase;
import com.example.ticket.domain.port.out.NotificarTicketPort;
import com.example.ticket.domain.port.out.TicketRepositoryPort;

import java.util.Objects;

public class TicketService implements RegistrarTicketUseCase, ConsultarEstadoTicketUseCase {

    private final TicketRepositoryPort ticketRepositoryPort;
    private final NotificarTicketPort notificarTicketPort;

    public TicketService(TicketRepositoryPort ticketRepositoryPort,
                         NotificarTicketPort notificarTicketPort) {
        this.ticketRepositoryPort = Objects.requireNonNull(ticketRepositoryPort, "ticketRepositoryPort no puede ser null");
        this.notificarTicketPort = Objects.requireNonNull(notificarTicketPort, "notificarTicketPort no puede ser null");
    }

    @Override
    public Ticket registrar(String titulo, String descripcion) {
        validarTexto(titulo, "titulo");
        validarTexto(descripcion, "descripcion");

        Ticket ticket = new Ticket(null, titulo, descripcion, EstadoTicket.ABIERTO);
        Ticket ticketGuardado = ticketRepositoryPort.guardar(ticket);

        notificarTicketPort.notificarNuevoTicket(ticketGuardado);

        return ticketGuardado;
    }

    @Override
    public EstadoTicket consultarEstado(Long ticketId) {
        Objects.requireNonNull(ticketId, "ticketId no puede ser null");

        Ticket ticket = ticketRepositoryPort.buscarPorId(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Ticket no encontrado"));

        return ticket.getEstado();
    }

    private void validarTexto(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(campo + " no puede estar vacio");
        }
    }
}
