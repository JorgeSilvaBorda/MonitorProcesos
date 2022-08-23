package com.monitor.model;

import java.time.LocalDateTime;

public class ProcesoRendicion {
    private Integer idProceso;
    private String idEmpresa;
    private String nombreEps;
    private Integer codEstado;
    private String estadoProceso;
    private LocalDateTime fechaProceso;
    private LocalDateTime fechaCreacion;
    private LocalDateTime inicioProceso;
    private LocalDateTime finProceso;
    private String fechaIso;
    private Integer idDefinicionArchivos;
    private Integer tipoCorte;
    private Integer idTarea;
    private String horaEjecucion;
    private String vigente;
    private LocalDateTime fechaHoraConsulta;

	public ProcesoRendicion() {
	}

	public ProcesoRendicion(Integer idProceso, String idEmpresa, String nombreEps, Integer codEstado, String estadoProceso, LocalDateTime fechaProceso, LocalDateTime fechaCreacion, LocalDateTime inicioProceso, LocalDateTime finProceso, String fechaIso, Integer idDefinicionArchivos, Integer tipoCorte, Integer idTarea, String horaEjecucion, String vigente, LocalDateTime fechaHoraConsulta) {
	    this.idProceso = idProceso;
	    this.idEmpresa = idEmpresa;
	    this.nombreEps = nombreEps;
	    this.codEstado = codEstado;
	    this.estadoProceso = estadoProceso;
	    this.fechaProceso = fechaProceso;
	    this.fechaCreacion = fechaCreacion;
	    this.inicioProceso = inicioProceso;
	    this.finProceso = finProceso;
	    this.fechaIso = fechaIso;
	    this.idDefinicionArchivos = idDefinicionArchivos;
	    this.tipoCorte = tipoCorte;
	    this.idTarea = idTarea;
	    this.horaEjecucion = horaEjecucion;
	    this.vigente = vigente;
	    this.fechaHoraConsulta = fechaHoraConsulta;
	}

	public Integer getIdProceso() {
	    return idProceso;
	}

	public void setIdProceso(Integer idProceso) {
	    this.idProceso = idProceso;
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

	public Integer getCodEstado() {
	    return codEstado;
	}

	public void setCodEstado(Integer codEstado) {
	    this.codEstado = codEstado;
	}

	public String getEstadoProceso() {
	    return estadoProceso;
	}

	public void setEstadoProceso(String estadoProceso) {
	    this.estadoProceso = estadoProceso;
	}

	public LocalDateTime getFechaProceso() {
	    return fechaProceso;
	}

	public void setFechaProceso(LocalDateTime fechaProceso) {
	    this.fechaProceso = fechaProceso;
	}

	public LocalDateTime getFechaCreacion() {
	    return fechaCreacion;
	}

	public void setFechaCreacion(LocalDateTime fechaCreacion) {
	    this.fechaCreacion = fechaCreacion;
	}

	public LocalDateTime getInicioProceso() {
	    return inicioProceso;
	}

	public void setInicioProceso(LocalDateTime inicioProceso) {
	    this.inicioProceso = inicioProceso;
	}

	public LocalDateTime getFinProceso() {
	    return finProceso;
	}

	public void setFinProceso(LocalDateTime finProceso) {
	    this.finProceso = finProceso;
	}

	public String getFechaIso() {
	    return fechaIso;
	}

	public void setFechaIso(String fechaIso) {
	    this.fechaIso = fechaIso;
	}

	public Integer getIdDefinicionArchivos() {
	    return idDefinicionArchivos;
	}

	public void setIdDefinicionArchivos(Integer idDefinicionArchivos) {
	    this.idDefinicionArchivos = idDefinicionArchivos;
	}

	public Integer getTipoCorte() {
	    return tipoCorte;
	}

	public void setTipoCorte(Integer tipoCorte) {
	    this.tipoCorte = tipoCorte;
	}

	public Integer getIdTarea() {
	    return idTarea;
	}

	public void setIdTarea(Integer idTarea) {
	    this.idTarea = idTarea;
	}

	public String getHoraEjecucion() {
	    return horaEjecucion;
	}

	public void setHoraEjecucion(String horaEjecucion) {
	    this.horaEjecucion = horaEjecucion;
	}

	public String getVigente() {
	    return vigente;
	}

	public void setVigente(String vigente) {
	    this.vigente = vigente;
	}

	public LocalDateTime getFechaHoraConsulta() {
	    return fechaHoraConsulta;
	}

	public void setFechaHoraConsulta(LocalDateTime fechaHoraConsulta) {
	    this.fechaHoraConsulta = fechaHoraConsulta;
	}

        
}
