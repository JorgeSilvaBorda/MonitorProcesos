package com.monitor.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class FechaSistema {
    private LocalDate fecha;
    private LocalDateTime fechaHora;
    private LocalTime hora;

    public FechaSistema() {
    }

    public FechaSistema(LocalDate fecha, LocalDateTime fechaHora, LocalTime hora) {
	this.fecha = fecha;
	this.fechaHora = fechaHora;
	this.hora = hora;
    }

    public LocalDate getFecha() {
	return fecha;
    }

    public void setFecha(LocalDate fecha) {
	this.fecha = fecha;
    }

    public LocalDateTime getFechaHora() {
	return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
	this.fechaHora = fechaHora;
    }

    public LocalTime getHora() {
	return hora;
    }

    public void setHora(LocalTime hora) {
	this.hora = hora;
    }
    
    
}
