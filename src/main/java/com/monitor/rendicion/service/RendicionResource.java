package com.monitor.rendicion.service;

import com.monitor.model.NotificacionRendicion;
import com.monitor.model.ProcesoRendicion;
import com.monitor.util.Parametros;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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


@Path("/procesoprogramado")
public class RendicionResource {
    
    private static final Logger LOG = Logger.getLogger(RendicionResource.class);

    @Inject
    RendicionMapper mapper;
    @Inject
    RendicionService procesoRendicionService;
    @Inject
    NotificacionRendicionService notificacionProcesoService;
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
	LOG.info("Entra a buscar en ejeución");
	
	//1.- Buscar proceso en ejecución (EstadoEjecución = 26)
	ProcesoRendicion procesoActual = mapper.buscarEnEjecucion();
	
	//En caso de que exista un proceso en ejecución:
	if (procesoActual != null) {
	    LOG.info("Hay proceso en ejecución. ID Proceso: " + procesoActual.getIdProceso());
	    //antes de proceder, se debe validar si ya existe una alerta generada para el proceso
	    NotificacionRendicion notificacion = notificacionProcesoService.getNotificacionProcesoByIdProceso(procesoActual.getIdProceso());
	    if (notificacion != null) {
		LOG.info("Ya existe una alerta para el proceso");
		return new ProcesoRendicion();
	    }
	    //Se debe guardar el que se está leyendo para que aparezca en la lista de más abajo
	    procesoRendicionService.insProcesoRendicionActivo(procesoActual);

	    //2.- Traer listado de procesos con el mismo IdProceso
	    List<ProcesoRendicion> procesosRendicion = procesoRendicionService.getProcesosMonitorByIdProceso(procesoActual.getIdProceso());

	    //Si en el listado hay al menos un proceso registrado:
	    if (procesosRendicion.size() > 0) {
		LOG.info("Cantidad de muestras para el proceso en ejecución: " + procesosRendicion.size());
		//3.- Obtener diferencia de tiempo de los procesos registrados.
		long minutos = 0;
		ProcesoRendicion proc = procesosRendicion.get(0);
		LocalDateTime fechaHoraPrimeraLectura = proc.getFechaHoraConsulta();
		for (int i = 1; i < procesosRendicion.size(); i++) {
		    minutos = minutos + ChronoUnit.MINUTES.between(proc.getFechaHoraConsulta(), procesosRendicion.get(i).getFechaHoraConsulta());
		    proc = procesosRendicion.get(i);
		}
		LocalDateTime fechaHoraUltimaLectura = procesoActual.getFechaHoraConsulta();
		
		LOG.info("La cantidad de minutos en ejecución: " + minutos);
		if (minutos >= new Parametros().getMaxMinutosEspera()) {
		    LOG.info("Se guarda alerta");
		    //Guardar mensaje de alerta
		    NotificacionRendicion notificacionProceso = new NotificacionRendicion();
		    notificacionProceso.setIdProceso(procesoActual.getIdProceso());
		    notificacionProceso.setLeido(false);
		    notificacionProceso.setProcesosRendicion(procesosRendicion);
		    notificacionProceso.setTiempoPermitido(new Parametros().getMaxMinutosEspera());
		    notificacionProcesoService.postNotificacionProceso(notificacionProceso);

		    //Enviar email a los interesados
		    sendMail(notificacionProceso);
		}
	    }
	}

	return new ProcesoRendicion();
    }

    @Path("/notificacion/noleido")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<NotificacionRendicion> getNoLeido() {
	return notificacionProcesoService.getNotificacionesNoLeidas();
    }

    @Path("/{oid}/leido")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public ProcesoRendicion marcarComoLeido(@PathParam("oid") String oid) {
	notificacionProcesoService.marcarComoLeido(oid);
	return null;
    }
    
    @Path("/notificaciones/marcarleido")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ProcesoRendicion marcarComoLeido(String[] ides) {
	notificacionProcesoService.marcarVariosComoLeido(ides);
	return null;
    }

    private void sendMail(NotificacionRendicion notificacion) {
	LOG.info("Se ingresa a enviar mail");
	String destinatarios = new Parametros().getDestinatarios();
	LOG.info("Los destinatarios de Mail son los siguientes:");
	LOG.info(destinatarios);
	
	String content = "<html><body style='{font-family: Arial, sans-serif;}'>";
	content += "<h2>Informe de proceso de Rendiciones retrasado</h2>";
	content += "<br />";
	content += "<p>Un proceso de Rendiciones ha tomado en ejecución más tiempo del configurado como permitido, por lo que se ha generado esta alerta para informar del estado en ejecución.</p>";
	content += "<h3>Detalle:</h3>";
	content += "<ul>";
	content += "<li>Id Proceso: <strong>" + notificacion.getIdProceso() + "</strong></li>";
	content += "<li>Id Empresa: <strong>" + notificacion.getProcesosRendicion().get(0).getIdEmpresa() + "</strong></li>";
	content += "<li>Empresa: <strong>" + notificacion.getProcesosRendicion().get(0).getNombreEps() + "</strong></li>";
	content += "</ul>";
	content += "<br />";
	content += "<p>El tiempo máximo de espera configurado es de " + notificacion.getTiempoPermitido() + " minutos, y ha sido superado por este proceso.</p>";
	content += "<br />";
	content += "<br />";
	content += "<p>Este es un mensaje generado automáticamente. Por favor no responda este correo.</p>";
	content += "</body></html>";

	List<String> destinos = new ArrayList();
	String[] tos = destinatarios.split(",");

	if (tos.length > 0) {
	    Mail correo = new Mail();
	    correo.setFrom("reporte@reporte.unired.cl");
	    correo.setSubject("¡ATENCIÓN! - RETRASO EN PROCESO RENDICIÓN #" + notificacion.getIdProceso() + " EPS " + notificacion.getProcesosRendicion().get(0).getNombreEps());
	    correo.setText(content);

	    for (String dest : tos) {
		destinos.add(dest);
	    }
	    correo.setTo(destinos);
	    mailer.send(correo);
	}

    }

}
