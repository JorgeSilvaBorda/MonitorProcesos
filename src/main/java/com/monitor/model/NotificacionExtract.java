package com.monitor.model;

public class NotificacionExtract {
    private String tipoNotificacion;
    private boolean leido;
    private ProcesoExtract extract;

    public NotificacionExtract() {
    }

    public NotificacionExtract(String tipoNotificacion, boolean leido, ProcesoExtract extract) {
	this.tipoNotificacion = tipoNotificacion;
	this.leido = leido;
	this.extract = extract;
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

    public ProcesoExtract getExtract() {
	return extract;
    }

    public void setExtract(ProcesoExtract extract) {
	this.extract = extract;
    }
    
    
}
