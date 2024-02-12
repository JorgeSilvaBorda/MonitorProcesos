package com.monitor.extract.service;

import com.monitor.model.NotificacionExtract;
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

@Path("/procesoextract")
public class ExtractResource {
    private static final Logger LOG = Logger.getLogger(ExtractResource.class);
    
    @Inject
    ExtractService service;
    
    @Path("/buscarproblemas")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void buscarExtractProblemasHoy(){
	service.buscarExtractProblemasHoy();
    }
    
    @Path("/notificacion/noleido")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<NotificacionExtract> getNoLeido(){
	return service.getNotificacionesExtractNoLeidas();
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
