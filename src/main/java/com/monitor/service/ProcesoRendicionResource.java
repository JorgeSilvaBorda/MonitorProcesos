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
import javax.ws.rs.POST;
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

	//En caso de que exista un proceso en ejecución:
	if (procesoActual != null) {
	    //antes de proceder, se debe validar si ya existe una alerta generada para el proceso
	    NotificacionProceso notificacion = notificacionProcesoService.getNotificacionProcesoByIdProceso(procesoActual.getIdProceso());
	    if (notificacion != null) {
		return new ProcesoRendicion();
	    }
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


		if (minutos > new Parametros().getMaxMinutosEspera()) {
		    //Guardar mensaje de alerta
		    NotificacionProceso notificacionProceso = new NotificacionProceso();
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
    public NotificacionProceso getNoLeido(){
	return notificacionProcesoService.getNotificacionesNoLeidas();
    }
    
    @Path("/{oid}/leido")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public ProcesoRendicion marcarComoLeido(@PathParam("oid") String oid){
	notificacionProcesoService.marcarComoLeido(oid);
	return null;
    }

    private void sendMail(NotificacionProceso notificacion) {
	String destinatarios = ConfigProvider.getConfig().getValue("destinatarios-notificacion", String.class);

	String content = "<html><body style='{font-family: Arial, sans-serif;}'>";
	content += "<h2>Informe de proceso demorado</h2>";
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

	mailer.send(Mail.withText(
		destinatarios,
		"¡ATENCIÓN! - RETRASO EN PROCESO RENDICIÓN #" + notificacion.getIdProceso() + " EPS " + notificacion.getProcesosRendicion().get(0).getNombreEps(),
		content
	));
    }

}
