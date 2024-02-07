package com.monitor.model;

public class NotificacionConciliacion {
    private String tipoNotificacion;
    private boolean leido;
    private ProcesoConciliacion conciliacion;

    public NotificacionConciliacion() {
    }

    public NotificacionConciliacion(String tipoNotificacion, boolean leido, ProcesoConciliacion conciliacion) {
	this.tipoNotificacion = tipoNotificacion;
	this.leido = leido;
	this.conciliacion = conciliacion;
    }

    public String getTipoNotificacion() {
	return tipoNotificacion;
    }

    public void setTipoNotificacion(String tipoNotificacion) {
	this.tipoNotificacion = tipoNotificacion;
    }

    public boolean isLeido() {
	return leido;
    }

    public void setLeido(boolean leido) {
	this.leido = leido;
    }

    public ProcesoConciliacion getConciliacion() {
	return conciliacion;
    }

    public void setConciliacion(ProcesoConciliacion conciliacion) {
	this.conciliacion = conciliacion;
    }
    
    
}
