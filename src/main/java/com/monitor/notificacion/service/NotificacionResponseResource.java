package com.monitor.notificacion.service;

import com.monitor.model.NotificacionNomina;
import com.monitor.model.NotificacionRendicion;
import com.monitor.nomina.service.NotificacionNominaService;
import com.monitor.notificacion.model.NotificacionResponse;
import com.monitor.rendicion.service.NotificacionRendicionService;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/notificaciones")
public class NotificacionResponseResource {
    @Inject
    NotificacionRendicionService serviceRendicion;
    
    @Inject
    NotificacionNominaService serviceNomina;
    
    @Path("/noleidas")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public NotificacionResponse getNotificacionesNoLeidas(){
	
	List<NotificacionRendicion> rendiciones = serviceRendicion.getNotificacionesNoLeidas();
	List<NotificacionNomina> nominas = serviceNomina.getProcesosNominaRegistradosNoLeidos();
	
	NotificacionResponse response = new NotificacionResponse();
	response.setNotificacionesNominas(nominas);
	response.setNotificacionesRendicion(rendiciones);
	
	return response;
	
    }
}
