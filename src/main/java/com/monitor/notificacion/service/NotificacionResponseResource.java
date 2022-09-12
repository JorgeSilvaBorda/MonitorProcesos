package com.monitor.notificacion.service;

import com.monitor.model.GrupoIdes;
import com.monitor.model.NotificacionNomina;
import com.monitor.model.NotificacionRendicion;
import com.monitor.nomina.service.NominaResource;
import com.monitor.nomina.service.NotificacionNominaService;
import com.monitor.notificacion.model.NotificacionResponse;
import com.monitor.rendicion.service.NotificacionRendicionService;
import com.monitor.rendicion.service.RendicionResource;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/notificaciones")
public class NotificacionResponseResource {
    @Inject
    NotificacionRendicionService serviceRendicion;
    
    @Inject
    RendicionResource resourceRendicion;
    
    @Inject
    NotificacionNominaService serviceNomina;
    
    @Inject
    NominaResource resourceNomina;
    
    
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
    
    @Path("/buscar")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void buscarEstadosError(){
	resourceRendicion.buscarEnEjecucion();
	resourceNomina.getEstadosNominas();
    }
    
    @Path("/marcarleido")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void marcarLedidas(GrupoIdes ides){
	
	serviceNomina.marcarVariosComoLeido(ides.getIdesNominas());
	serviceRendicion.marcarVariosComoLeido(ides.getIdesRendiciones());
	
    }
}
