package com.monitor.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ProcesoExtract {
    
    private Integer idLogSistema;
    private String mensaje;
    private String idEmpresa;
    private String nombreEps;
    private LocalDate fechaCreacion;
    private LocalDateTime fechaHoraCreacion;
    private Integer idTipoLog;
    private String descripcion;
    
    public ProcesoExtract() {
    }

    public ProcesoExtract(Integer idLogSistema, String mensaje, String idEmpresa, String nombreEps, LocalDate fechaCreacion, LocalDateTime fechaHoraCreacion, Integer idTipoLog, String descripcion) {
	this.idLogSistema = idLogSistema;
	this.mensaje = mensaje;
	this.idEmpresa = idEmpresa;
	this.nombreEps = nombreEps;
	this.fechaCreacion = fechaCreacion;
	this.fechaHoraCreacion = fechaHoraCreacion;
	this.idTipoLog = idTipoLog;
	this.descripcion = descripcion;
    }

    public Integer getIdLogSistema() {
	return idLogSistema;
    }

    public void setIdLogSistema(Integer idLogSistema) {
	this.idLogSistema = idLogSistema;
    }

    public String getMensaje() {
	return mensaje;
    }

    public void setMensaje(String mensaje) {
	this.mensaje = mensaje;
    }

    public String getIdEmpresa() {
	return idEmpresa;
    }

    public void setIdEmpresa(String idEmpresa) {
	this.idEmpresa = idEmpresa;
    }

    public String getNombreEps() {
	return nombreEps;
    }

    public void setNombreEps(String nombreEps) {
	this.nombreEps = nombreEps;
    }

    public LocalDate getFechaCreacion() {
	return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
	this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaHoraCreacion() {
	return fechaHoraCreacion;
    }

    public void setFechaHoraCreacion(LocalDateTime fechaHoraCreacion) {
	this.fechaHoraCreacion = fechaHoraCreacion;
    }

    public Integer getIdTipoLog() {
	return idTipoLog;
    }

    public void setIdTipoLog(Integer idTipoLog) {
	this.idTipoLog = idTipoLog;
    }

    public String getDescripcion() {
	return descripcion;
    }

    public void setDescripcion(String descripcion) {
	this.descripcion = descripcion;
    }

   
}
