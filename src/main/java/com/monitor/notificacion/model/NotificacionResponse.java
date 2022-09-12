package com.monitor.notificacion.model;

import com.monitor.model.NotificacionRendicion;
import com.monitor.model.NotificacionNomina;
import java.util.List;

public class NotificacionResponse {
    private List<NotificacionRendicion> notificacionesRendicion;
    private List<NotificacionNomina> notificacionesNominas;

    public NotificacionResponse(List<NotificacionRendicion> notificacionesRendicion, List<NotificacionNomina> notificacionesNominas) {
	this.notificacionesRendicion = notificacionesRendicion;
	this.notificacionesNominas = notificacionesNominas;
    }

    public NotificacionResponse() {
    }

    public List<NotificacionRendicion> getNotificacionesRendicion() {
	return notificacionesRendicion;
    }

    public void setNotificacionesRendicion(List<NotificacionRendicion> notificacionesRendicion) {
	this.notificacionesRendicion = notificacionesRendicion;
    }

    public List<NotificacionNomina> getNotificacionesNominas() {
	return notificacionesNominas;
    }

    public void setNotificacionesNominas(List<NotificacionNomina> notificacionesNominas) {
	this.notificacionesNominas = notificacionesNominas;
    }
    
    
}
