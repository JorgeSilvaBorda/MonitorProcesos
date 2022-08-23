package com.monitor.model;

import java.util.List;
import org.bson.types.ObjectId;

public class NotificacionProceso{
    private ObjectId _id;
    private Integer idProceso;
    private boolean leido;
    private List<ProcesoRendicion> procesosRendicion;

    public NotificacionProceso() {
    }

    public NotificacionProceso(ObjectId _id, Integer idProceso, boolean leido, List<ProcesoRendicion> procesosRendicion) {
	this._id = _id;
	this.idProceso = idProceso;
	this.leido = leido;
	this.procesosRendicion = procesosRendicion;
    }

    public ObjectId getId() {
	return _id;
    }

    public void setId(ObjectId _id) {
	this._id = _id;
    }

    public Integer getIdProceso() {
	return idProceso;
    }

    public void setIdProceso(Integer idProceso) {
	this.idProceso = idProceso;
    }

    public boolean isLeido() {
	return leido;
    }

    public void setLeido(boolean leido) {
	this.leido = leido;
    }

    public List<ProcesoRendicion> getProcesosRendicion() {
	return procesosRendicion;
    }

    public void setProcesosRendicion(List<ProcesoRendicion> procesosRendicion) {
	this.procesosRendicion = procesosRendicion;
    }
    
        
}
