package com.monitor.notificacion.model;

import com.monitor.nomina.service.NotificacionNominaService;
import com.monitor.rendicion.service.NotificacionRendicionService;
import javax.inject.Inject;
import javax.ws.rs.Path;

@Path("/notificaciones")
public class NotificacionResponseResource {
    @Inject
    NotificacionRendicionService serviceRendicion;
    
    @Inject
    NotificacionNominaService serviceNomina;
}
