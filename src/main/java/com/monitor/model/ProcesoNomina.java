    package com.monitor.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.bson.Document;

public class ProcesoNomina {

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

    public ProcesoNomina() {
    }

    public ProcesoNomina(String idEmpresa, String codEmpresa, String nomEmpresa, String horaIni, String horaFin, String horaActual, Integer minutosALaHora, LocalDateTime fechaProceso, LocalDateTime fechaTermino, Integer minutos, Integer idEstado, String estado, LocalDate fechaCarga, LocalDateTime fechaHoraCarga) {
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

    @JsonIgnore
    public static ProcesoNomina fromDocument(Document document) {
	ProcesoNomina nomina = new ProcesoNomina();
	nomina.setCodEmpresa(document.getString("codEmpresa"));
	
	nomina.setFechaCarga(document.getDate("fechaCarga").toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
	nomina.setFechaHoraCarga(document.getDate("fechaHoraCarga").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
	if (nomina.getIdEstado() != null) {
	    nomina.setFechaProceso(document.getDate("fechaProceso").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
	    nomina.setFechaTermino(document.getDate("fechaTermino").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
	    nomina.setMinutos(document.getInteger("minutos"));
	    nomina.setIdEstado(document.getInteger("idEstado"));
	    nomina.setEstado(document.getString("estado"));
	}

	nomina.setHoraActual(document.getString("horaActual"));
	nomina.setHoraFin(document.getString("horaFin"));
	nomina.setHoraIni(document.getString("horaIni"));
	nomina.setIdEmpresa(document.getString("idEmpresa"));

	nomina.setMinutosALaHora(document.getInteger("minutosALaHora"));
	nomina.setNomEmpresa(document.getString("nomEmpresa"));
	return nomina;
    }

    @JsonIgnore
    public static Document toDocument(ProcesoNomina nomina) {
	Document document = new Document();
	document.append("codEmpresa", nomina.getCodEmpresa());
	document.append("estado", nomina.getEstado());
	document.append("estado", nomina.getEstado());
	document.append("fechaCarga", nomina.getFechaCarga());
	document.append("fechaHoraCarga", nomina.getFechaHoraCarga());
	document.append("fechaProceso", nomina.getFechaProceso());
	document.append("fechaTermino", nomina.getFechaTermino());
	document.append("horaActual", nomina.getHoraActual());
	document.append("horaFin", nomina.getHoraFin());
	document.append("horaIni", nomina.getHoraIni());
	document.append("idEmpresa", nomina.getIdEmpresa());
	document.append("idEstado", nomina.getIdEstado());
	document.append("minutos", nomina.getMinutos());
	document.append("minutosALaHora", nomina.getMinutosALaHora());
	document.append("nomEmpresa", nomina.getNomEmpresa());
	return document;
    }

}
