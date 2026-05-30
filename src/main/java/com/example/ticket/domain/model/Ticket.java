package com.example.ticket.domain.model;

public class Ticket {
    private Long id;
    private String titulo;
    private String descripcion;
    private EstadoTicket estado;

    public Ticket(Long id, String titulo, String descripcion, EstadoTicket estado) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public EstadoTicket getEstado() {
        return estado;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

