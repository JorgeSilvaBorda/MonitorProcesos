package com.monitor.notificacion.service;

import com.monitor.conciliacion.service.ConciliacionResource;
import com.monitor.conciliacion.service.ConciliacionService;
import com.monitor.extract.service.ExtractResource;
import com.monitor.extract.service.ExtractService;
import com.monitor.model.GrupoIdes;
import com.monitor.model.NotificacionConciliacion;
import com.monitor.model.NotificacionExtract;
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
    
    @Inject
    ConciliacionService serviceConciliacion;
    @Inject
    ConciliacionResource resourceConciliacion;
    
    @Inject
    ExtractService serviceExtract;
    @Inject
    ExtractResource resourceExtract;
    
    @Path("/noleidas")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public NotificacionResponse getNotificacionesNoLeidas(){
	
	List<NotificacionRendicion> rendiciones = serviceRendicion.getNotificacionesNoLeidas();
	List<NotificacionNomina> nominas = serviceNomina.getProcesosNominaRegistradosNoLeidos();
	List<NotificacionConciliacion> conciliaciones = serviceConciliacion.getNotificacionesConciliacionNoLeidas();
	List<NotificacionExtract> extracts = serviceExtract.getNotificacionesExtractNoLeidas();
	
	NotificacionResponse response = new NotificacionResponse();
	response.setNotificacionesNominas(nominas);
	response.setNotificacionesRendiciones(rendiciones);
	response.setNotificacionesConciliacion(conciliaciones);
	response.setNotificacionesExtract(extracts);
	
	return response;
	
    }
    
    @Path("/buscar")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void buscarEstadosError(){
	resourceRendicion.buscarEnEjecucion();
	resourceNomina.getEstadosNominas();
	resourceConciliacion.buscarConciliacionesProblemasHoy();
	resourceExtract.buscarExtractProblemasHoy();
    }
    
    @Path("/marcarleido")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void marcarLedidas(GrupoIdes ides){
	
	serviceNomina.marcarVariosComoLeido(ides.getIdesNominas());
	serviceRendicion.marcarVariosComoLeido(ides.getIdesRendiciones());
	serviceConciliacion.marcarVariosComoLeido(ides.getIdesConciliacion());
	serviceExtract.marcarVariosComoLeido(ides.getIdesExtract());
	
    }
}
