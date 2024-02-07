package com.monitor.model;


public class GrupoIdes {
    private String[] idesNominas;
    private String[] idesRendiciones;
    private String[] idesConciliacion;
    private String[] idesExtract;

    public GrupoIdes() {
    }

    public GrupoIdes(String[] idesNominas, String[] idesRendiciones, String[] idesConciliacion, String[] idesExtract) {
	this.idesNominas = idesNominas;
	this.idesRendiciones = idesRendiciones;
	this.idesConciliacion = idesConciliacion;
	this.idesExtract = idesExtract;
    }

    public String[] getIdesNominas() {
	return idesNominas;
    }

    public void setIdesNominas(String[] idesNominas) {
	this.idesNominas = idesNominas;
    }

    public String[] getIdesRendiciones() {
	return idesRendiciones;
    }

    public void setIdesRendiciones(String[] idesRendiciones) {
	this.idesRendiciones = idesRendiciones;
    }

    public String[] getIdesConciliacion() {
	return idesConciliacion;
    }

    public void setIdesConciliacion(String[] idesConciliacion) {
	this.idesConciliacion = idesConciliacion;
    }

    public String[] getIdesExtract() {
	return idesExtract;
    }

    public void setIdesExtract(String[] idesExtract) {
	this.idesExtract = idesExtract;
    }

    
}
