package com.monitor.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.monitor.util.ObjectIdSerializer;
import java.time.LocalDate;
import org.bson.types.ObjectId;

public class NotificacionConciliacion {
    
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId _id;
    private String tipoNotificacion;
    private String fechaNotificacion;
    private boolean leido;
    private ProcesoConciliacion conciliacion;

    public NotificacionConciliacion() {
    }

    public NotificacionConciliacion(ObjectId _id, String tipoNotificacion, String fechaNotificacion, boolean leido, ProcesoConciliacion conciliacion) {
	this._id = _id;
	this.tipoNotificacion = tipoNotificacion;
	this.fechaNotificacion = fechaNotificacion;
	this.leido = leido;
	this.conciliacion = conciliacion;
    }

    public ObjectId get_id() {
	return _id;
    }

    public void set_id(ObjectId _id) {
	this._id = _id;
    }

    public String getTipoNotificacion() {
	return tipoNotificacion;
    }

    public void setTipoNotificacion(String tipoNotificacion) {
	this.tipoNotificacion = tipoNotificacion;
    }

    public String getFechaNotificacion() {
	return fechaNotificacion;
    }

    public void setFechaNotificacion(String fechaNotificacion) {
	this.fechaNotificacion = fechaNotificacion;
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
