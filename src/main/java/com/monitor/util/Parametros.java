package com.monitor.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class Parametros {
    private Properties properties;
    
    public Parametros(){
	try(InputStream input = new FileInputStream("/monitor-procesos/parametros.properties")){
	    properties = new Properties();
	    properties.load(input);
	    
	}catch (Exception ex) {
	    System.out.println("No se pudo cargar el archivo de parámetros de configuración del notificador");
	    System.out.println(ex);
	}
    }
    
    public Integer getMaxMinutosEspera(){
	return Integer.parseInt(properties.getProperty("max-minutos-espera"));
    }
    
    public String getDestinatarios(){
	return properties.getProperty("destinatarios-notificacion");
    }
}
