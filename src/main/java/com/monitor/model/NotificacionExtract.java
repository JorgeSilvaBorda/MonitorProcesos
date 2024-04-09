package com.monitor.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.monitor.util.ObjectIdSerializer;
import java.time.LocalDate;
import org.bson.types.ObjectId;

public class NotificacionExtract {
    
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId _id;
    private String tipoNotificacion;
    private String fechaNotificacion;
    private boolean leido;
    private ProcesoExtract extract;

    public NotificacionExtract() {
    }

    public NotificacionExtract(ObjectId _id, String tipoNotificacion, String fechaNotificacion, boolean leido, ProcesoExtract extract) {
	this._id = _id;
	this.tipoNotificacion = tipoNotificacion;
	this.fechaNotificacion = fechaNotificacion;
	this.leido = leido;
	this.extract = extract;
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

    public String getFechaNotificacion() {
	return fechaNotificacion;
    }

    public void setFechaNotificacion(String fechaNotificacion) {
	this.fechaNotificacion = fechaNotificacion;
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
