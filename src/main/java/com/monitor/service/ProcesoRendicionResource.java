package com.monitor.service;

import com.monitor.model.NotificacionProceso;
import com.monitor.model.ProcesoRendicion;
import com.monitor.util.Parametros;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.ConfigProvider;

@Path("/procesoprogramado")
public class ProcesoRendicionResource {

    @Inject
    ProcesoRendicionMapper mapper;
    @Inject
    ProcesoRendicionService procesoRendicionService;
    @Inject
    NotificacionProcesoService notificacionProcesoService;
    @Inject
    Mailer mailer;

    @Path("/idproceso/{idProceso}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ProcesoRendicion getProcesoRendicionIdProceso(@PathParam("idProceso") Integer idProceso) {
	return mapper.getProcesoProgramadoIdProceso(idProceso);
    }

    @Path("/monitor/{idProceso}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ProcesoRendicion getProcesoMonitorByIdProceso(@PathParam("idProceso") Integer idProceso) {
	return procesoRendicionService.getProcesoMonitorByIdProceso(idProceso);
    }

    @Path("/enejecucion")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ProcesoRendicion buscarEnEjecucion() {

	//1.- Buscar proceso en ejecución (EstadoEjecución = 26)
	ProcesoRendicion procesoActual = mapper.buscarEnEjecucion();
	System.out.println("paso 1");

	//En caso de que exista un proceso en ejecución:
	if (procesoActual != null) {
	    System.out.println("es distinto de null");
	    //antes de proceder, se debe validar si ya existe una alerta generada para el proceso
	    NotificacionProceso notificacion = notificacionProcesoService.getNotificacionProcesoByIdProceso(procesoActual.getIdProceso());
	    if(notificacion != null){
		System.out.println("Existen notificaciones anteriores");
		return new ProcesoRendicion();
	    }
	    System.out.println("no existen notificaciones anteriores");
	    //Se debe guardar el que se está leyendo para que aparezca en la lista de más abajo
	    procesoRendicionService.insProcesoRendicionActivo(procesoActual);
	    
	    //2.- Traer listado de procesos con el mismo IdProceso
	    List<ProcesoRendicion> procesosRendicion = procesoRendicionService.getProcesosMonitorByIdProceso(procesoActual.getIdProceso());

	    //Si en el listado hay al menos un proceso registrado:
	    if (procesosRendicion.size() > 0) {
		//3.- Obtener diferencia de tiempo de los procesos registrados.
		long minutos = 0;
		ProcesoRendicion proc = procesosRendicion.get(0);
		LocalDateTime fechaHoraPrimeraLectura = proc.getFechaHoraConsulta();
		for (int i = 1; i < procesosRendicion.size(); i++) {
		    minutos = minutos + ChronoUnit.MINUTES.between(proc.getFechaHoraConsulta(), procesosRendicion.get(i).getFechaHoraConsulta());
		    proc = procesosRendicion.get(i);
		}
		LocalDateTime fechaHoraUltimaLectura = procesoActual.getFechaHoraConsulta();
		
		System.out.println("Los minutos totales son: " + minutos);
		
		if (minutos > new Parametros().getMaxMinutosEspera()) {    
		    //Guardar mensaje de alerta
		    NotificacionProceso notificacionProceso = new NotificacionProceso();
		    notificacionProceso.setIdProceso(procesoActual.getIdProceso());
		    notificacionProceso.setLeido(false);
		    notificacionProceso.setProcesosRendicion(procesosRendicion);
		    notificacionProcesoService.postNotificacionProceso(notificacionProceso);
		    
		    //Enviar email a los interesados
		    sendMail(notificacionProceso, minutos);
		}
	    }
	}

	return new ProcesoRendicion();
    }
    
    private void sendMail(NotificacionProceso notificacion, Long minutos){
	String destinatarios = ConfigProvider.getConfig().getValue("destinatarios-notificacion", String.class);
	
	String contenido = "Le informamos que el proceso con ID: " + notificacion.getIdProceso() + ", se encuentra en ejecución por más del tiempo permitido (" + minutos + " minutos).\n"
		+ "Se emitirá una alerta hacia el dashboard de control de procesos en conjunto con este email.";
	
	mailer.send(Mail.withText(
		destinatarios, 
		"Notificación de proceso de rendición demorado", 
		contenido
	));
    }

}
