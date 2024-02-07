package com.monitor.conciliacion.service;

import com.monitor.model.NotificacionConciliacion;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.jboss.logging.Logger;

@Path("/procesoconciliacion")
public class ConciliacionResource {
    private static final Logger LOG = Logger.getLogger(ConciliacionResource.class);
    
    @Inject
    ConciliacionService service;
    
    
    @Path("/buscarproblemas")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void buscarConciliacionesProblemasHoy(){
	service.buscarConciliacionesProblemasHoy();
    }
    
    @Path("/notificacion/noleido")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    List<NotificacionConciliacion> getNoLeido(){
	return service.getNotificacionesConciliacionNoLeidas();
    }
    
    @Path("/{oid}/leido")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public void marcarComoLeido(@PathParam("oid") String oid){
	service.marcarComoLeido(oid);
    }
    
    @Path("/notificaciones/marcarleido")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public void marcarComoLeido(String[] oids){
	service.marcarVariosComoLeido(oids);
    }
}
