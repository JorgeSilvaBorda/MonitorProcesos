package com.monitor.notificacion.model;

import com.monitor.model.NotificacionConciliacion;
import com.monitor.model.NotificacionExtract;
import com.monitor.model.NotificacionRendicion;
import com.monitor.model.NotificacionNomina;
import java.util.List;

public class NotificacionResponse {
    private List<NotificacionRendicion> notificacionesRendiciones;
    private List<NotificacionNomina> notificacionesNominas;
    private List<NotificacionConciliacion> notificacionesConciliacion;
    private List<NotificacionExtract> notificacionesExtract;

    public NotificacionResponse() {
    }

    public NotificacionResponse(List<NotificacionRendicion> notificacionesRendiciones, List<NotificacionNomina> notificacionesNominas, List<NotificacionConciliacion> notificacionesConciliacion, List<NotificacionExtract> notificacionesExtract) {
	this.notificacionesRendiciones = notificacionesRendiciones;
	this.notificacionesNominas = notificacionesNominas;
	this.notificacionesConciliacion = notificacionesConciliacion;
	this.notificacionesExtract = notificacionesExtract;
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

    public List<NotificacionConciliacion> getNotificacionesConciliacion() {
	return notificacionesConciliacion;
    }

    public void setNotificacionesConciliacion(List<NotificacionConciliacion> notificacionesConciliacion) {
	this.notificacionesConciliacion = notificacionesConciliacion;
    }

    public List<NotificacionExtract> getNotificacionesExtract() {
	return notificacionesExtract;
    }

    public void setNotificacionesExtract(List<NotificacionExtract> notificacionesExtract) {
	this.notificacionesExtract = notificacionesExtract;
    }

    
}
