package com.monitor.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.monitor.util.ObjectIdSerializer;
import java.util.List;
import org.bson.types.ObjectId;

public class NotificacionProceso{
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId _id;
    private Integer idProceso;
    private boolean leido;
    private Integer tiempoPermitido;
    private List<ProcesoRendicion> procesosRendicion;

    public NotificacionProceso() {
    }

    public NotificacionProceso(ObjectId _id, Integer idProceso, boolean leido, Integer tiempoPermitido, List<ProcesoRendicion> procesosRendicion) {
	this._id = _id;
	this.idProceso = idProceso;
	this.leido = leido;
	this.tiempoPermitido = tiempoPermitido;
	this.procesosRendicion = procesosRendicion;
    }

    public ObjectId get_id() {
	return _id;
    }

    public void set_id(ObjectId _id) {
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
    
    public Integer getTiempoPermitido(){
	return tiempoPermitido;
    }
    
    public void setTiempoPermitido(Integer tiempoPermitido){
	this.tiempoPermitido = tiempoPermitido;
    }

    public List<ProcesoRendicion> getProcesosRendicion() {
	return procesosRendicion;
    }

    public void setProcesosRendicion(List<ProcesoRendicion> procesosRendicion) {
	this.procesosRendicion = procesosRendicion;
    }
    
        
}
