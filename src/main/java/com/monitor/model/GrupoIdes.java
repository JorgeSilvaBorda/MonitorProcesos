package com.monitor.model;

import java.util.List;

public class GrupoIdes {
    private String[] idesNominas;
    private String[] idesRendiciones;

    public GrupoIdes() {
    }

    public GrupoIdes(String[] idesNominas, String[] idesRendiciones) {
	this.idesNominas = idesNominas;
	this.idesRendiciones = idesRendiciones;
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
    
    
}
