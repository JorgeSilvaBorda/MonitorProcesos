package com.monitor.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.monitor.util.ObjectIdSerializer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.bson.Document;
import org.bson.types.ObjectId;

public class NotificacionProcesoNomina {

    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId _id;
    private String idEmpresa;
    private String codEmpresa;
    private String nomEmpresa;
    private String horaIni;
    private String horaFin;
    private String horaActual;
    private Integer minutosALaHora;
    private LocalDateTime fechaProceso;
    private LocalDateTime fechaTermino;
    private Integer minutos;
    private Integer idEstado;
    private String estado;
    private LocalDate fechaCarga;
    private LocalDateTime fechaHoraCarga;
    private boolean leido;

    public NotificacionProcesoNomina() {
    }

    public NotificacionProcesoNomina(ObjectId _id, String idEmpresa, String codEmpresa, String nomEmpresa, String horaIni, String horaFin, String horaActual, Integer minutosALaHora, LocalDateTime fechaProceso, LocalDateTime fechaTermino, Integer minutos, Integer idEstado, String estado, LocalDate fechaCarga, LocalDateTime fechaHoraCarga, boolean leido) {
	this._id = _id;
	this.idEmpresa = idEmpresa;
	this.codEmpresa = codEmpresa;
	this.nomEmpresa = nomEmpresa;
	this.horaIni = horaIni;
	this.horaFin = horaFin;
	this.horaActual = horaActual;
	this.minutosALaHora = minutosALaHora;
	this.fechaProceso = fechaProceso;
	this.fechaTermino = fechaTermino;
	this.minutos = minutos;
	this.idEstado = idEstado;
	this.estado = estado;
	this.fechaCarga = fechaCarga;
	this.fechaHoraCarga = fechaHoraCarga;
	this.leido = leido;
    }

    public ObjectId get_id() {
	return _id;
    }

    public void set_id(ObjectId _id) {
	this._id = _id;
    }

    public String getIdEmpresa() {
	return idEmpresa;
    }

    public void setIdEmpresa(String idEmpresa) {
	this.idEmpresa = idEmpresa;
    }

    public String getCodEmpresa() {
	return codEmpresa;
    }

    public void setCodEmpresa(String codEmpresa) {
	this.codEmpresa = codEmpresa;
    }

    public String getNomEmpresa() {
	return nomEmpresa;
    }

    public void setNomEmpresa(String nomEmpresa) {
	this.nomEmpresa = nomEmpresa;
    }

    public String getHoraIni() {
	return horaIni;
    }

    public void setHoraIni(String horaIni) {
	this.horaIni = horaIni;
    }

    public String getHoraFin() {
	return horaFin;
    }

    public void setHoraFin(String horaFin) {
	this.horaFin = horaFin;
    }

    public String getHoraActual() {
	return horaActual;
    }

    public void setHoraActual(String horaActual) {
	this.horaActual = horaActual;
    }

    public Integer getMinutosALaHora() {
	return minutosALaHora;
    }

    public void setMinutosALaHora(Integer minutosALaHora) {
	this.minutosALaHora = minutosALaHora;
    }

    public LocalDateTime getFechaProceso() {
	return fechaProceso;
    }

    public void setFechaProceso(LocalDateTime fechaProceso) {
	this.fechaProceso = fechaProceso;
    }

    public LocalDateTime getFechaTermino() {
	return fechaTermino;
    }

    public void setFechaTermino(LocalDateTime fechaTermino) {
	this.fechaTermino = fechaTermino;
    }

    public Integer getMinutos() {
	return minutos;
    }

    public void setMinutos(Integer minutos) {
	this.minutos = minutos;
    }

    public Integer getIdEstado() {
	return idEstado;
    }

    public void setIdEstado(Integer idEstado) {
	this.idEstado = idEstado;
    }

    public String getEstado() {
	return estado;
    }

    public void setEstado(String estado) {
	this.estado = estado;
    }

    public LocalDate getFechaCarga() {
	return fechaCarga;
    }

    public void setFechaCarga(LocalDate fechaCarga) {
	this.fechaCarga = fechaCarga;
    }

    public LocalDateTime getFechaHoraCarga() {
	return fechaHoraCarga;
    }

    public void setFechaHoraCarga(LocalDateTime fechaHoraCarga) {
	this.fechaHoraCarga = fechaHoraCarga;
    }

    public boolean isLeido() {
	return leido;
    }

    public void setLeido(boolean leido) {
	this.leido = leido;
    }

    @JsonIgnore
    public static Document toDocument(NotificacionProcesoNomina notificacion){
	Document document = new Document();
	document.append("_id", notificacion.get_id());
	document.append("idEmpresa", notificacion.getIdEmpresa());
	document.append("codEmpresa", notificacion.getCodEmpresa());
	document.append("nomEmpresa", notificacion.getNomEmpresa());
	document.append("horaIni", notificacion.getHoraIni());
	document.append("horaFin", notificacion.getHoraFin());
	document.append("horaActual", notificacion.getHoraActual());
	document.append("minutosALaHora", notificacion.getMinutosALaHora());
	document.append("fechaProceso", notificacion.getFechaProceso());
	document.append("fechaTermino", notificacion.getFechaTermino());
	document.append("minutos", notificacion.getMinutos());
	document.append("idEstado", notificacion.getIdEstado());
	document.append("estado", notificacion.getEstado());
	document.append("fechaCarga", notificacion.getFechaCarga());
	document.append("fechaHoraCarga", notificacion.getFechaHoraCarga());
	document.append("leido", notificacion.isLeido());
	
	return document;
    }
    
    @JsonIgnore
    public static NotificacionProcesoNomina fromDocument(Document document){
	NotificacionProcesoNomina notificacion = new NotificacionProcesoNomina();
	notificacion.set_id(document.getObjectId("_id"));
	notificacion.setIdEmpresa(document.getString("idEmpresa"));
	notificacion.setCodEmpresa(document.getString("codEmpresa"));
	notificacion.setNomEmpresa(document.getString("nomEmpresa"));
	notificacion.setHoraIni(document.getString("horaIni"));
	notificacion.setHoraFin(document.getString("horaFin"));
	notificacion.setHoraActual(document.getString("horaActual"));
	notificacion.setMinutosALaHora(document.getInteger("minutosALaHora"));
	notificacion.setFechaProceso(document.getDate("fechaProceso").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
	notificacion.setFechaProceso(document.getDate("fechaTermino").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
	notificacion.setMinutos(document.getInteger("minutos"));
	notificacion.setIdEstado(document.getInteger("idEstado"));
	notificacion.setFechaCarga(document.getDate("fechaCarga").toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
	notificacion.setFechaHoraCarga(document.getDate("fechaHoraCarga").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
	
	return notificacion;
    }
    

}
