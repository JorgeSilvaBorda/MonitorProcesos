package com.monitor.notificacion.model;

import com.monitor.model.NotificacionRendicion;
import com.monitor.model.NotificacionNomina;
import java.util.List;

public class NotificacionResponse {
    private List<NotificacionRendicion> notificacionesRendiciones;
    private List<NotificacionNomina> notificacionesNominas;

    public NotificacionResponse() {
    }

    public NotificacionResponse(List<NotificacionRendicion> notificacionesRendiciones, List<NotificacionNomina> notificacionesNominas) {
	this.notificacionesRendiciones = notificacionesRendiciones;
	this.notificacionesNominas = notificacionesNominas;
    }

    public List<NotificacionRendicion> getNotificacionesRendiciones() {
	return notificacionesRendiciones;
    }

    public void setNotificacionesRendiciones(List<NotificacionRendicion> notificacionesRendiciones) {
	this.notificacionesRendiciones = notificacionesRendiciones;
    }

    public List<NotificacionNomina> getNotificacionesNominas() {
	return notificacionesNominas;
    }

    public void setNotificacionesNominas(List<NotificacionNomina> notificacionesNominas) {
	this.notificacionesNominas = notificacionesNominas;
    }

    
    
}
