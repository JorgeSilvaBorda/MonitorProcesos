package com.monitor.nomina.service;

import com.monitor.model.FechaSistema;
import com.monitor.model.NotificacionProcesoNomina;
import com.monitor.model.ProcesoNomina;
import com.monitor.util.FechaSistemaMapper;
import com.monitor.util.Parametros;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.jboss.logging.Logger;

@Path("/procesonominas")
public class ProcesoNominaResource {

    private static final Logger LOG = Logger.getLogger(ProcesoNominaResource.class);

    @Inject
    ProcesoNominaMapper mapper;

    @Inject
    NotificacionNominaService notificacionService;

    @Inject
    FechaSistemaMapper fechaSistemaMapper;

    @Inject
    Mailer mailer;

    @GET
    @Path("/estados")
    @Produces(MediaType.APPLICATION_JSON)
    public List<NotificacionProcesoNomina> getEstadosNominas() {
	
	//Traer los procesos de nóminas programados para la fecha
	List<ProcesoNomina> nominas = mapper.getEstadosNominas();
	System.out.println("Cantidad de nóminas con problemas desde SQL Server: " + nominas.size());
	
	//Obtener la fecha desde el SQL Server, porque la del servidor no está correcta
	FechaSistema fechaSistema = fechaSistemaMapper.getFechaSistema();
	System.out.println("La fecha del sistema es: " + fechaSistema.getFechaHora());
	
	//Traer los procesos de nóminas que ya se guardaron en la base de datos mongo para comparar
	List<ProcesoNomina> nominasRegistradasError = notificacionService.getProcesosNominaRegistradosError(fechaSistema.getFecha());
	System.out.println("Para la fecha seleccionada, hay " + nominasRegistradasError.size() + " Nominas registradas con error en base local.");

	//Comparar los que se traen del SQL. Si no figura en mongo, guardarlo y crear alerta
	List<ProcesoNomina> nominasRegistrar = new ArrayList();
	for (int i = 0; i < nominas.size(); i++) {
	    boolean encontrado = false;
	    for (int x = 0; x < nominasRegistradasError.size(); x++) {
		if (nominas.get(i).getCodEmpresa().equals(nominasRegistradasError.get(x).getCodEmpresa())
			&& nominas.get(i).getNomEmpresa().equals(nominasRegistradasError.get(x).getNomEmpresa())
			&& nominas.get(i).getIdEmpresa().equals(nominasRegistradasError.get(x).getIdEmpresa())
			&& nominas.get(i).getHoraIni().equals(nominasRegistradasError.get(x).getHoraIni())
			&& nominas.get(i).getHoraFin().equals(nominasRegistradasError.get(x).getHoraFin())) {
		    encontrado = true;
		}
	    }
	    if (!encontrado) {
		nominasRegistrar.add(nominas.get(i));

	    }
	    encontrado = false;
	}

	//En caso de que existan nuevas nóminas con error que registrar--------------------
	if (nominasRegistrar.size() > 0) {
	    
	    //Convertir las clases a documentos para pasarlo al método siguiente
	    List<Document> documentosNotificar = new ArrayList();
	    List<NotificacionProcesoNomina> notificacionesResponse = new ArrayList(); //Esta lista es solo para la salida del api
	    for (ProcesoNomina pn : nominasRegistrar) {
		NotificacionProcesoNomina npn = new NotificacionProcesoNomina();
		npn.set_id(new ObjectId());
		npn.setCodEmpresa(pn.getCodEmpresa());
		npn.setEstado(pn.getEstado());
		npn.setFechaCarga(pn.getFechaCarga());
		npn.setFechaHoraCarga(pn.getFechaHoraCarga());
		npn.setFechaProceso(pn.getFechaProceso());
		npn.setFechaTermino(pn.getFechaTermino());
		npn.setHoraActual(pn.getHoraActual());
		npn.setHoraFin(pn.getHoraFin());
		npn.setHoraIni(pn.getHoraIni());
		npn.setIdEmpresa(pn.getIdEmpresa());
		npn.setIdEstado(pn.getIdEstado());
		npn.setLeido(false);
		npn.setMinutos(pn.getMinutos());
		npn.setMinutosALaHora(pn.getMinutosALaHora());
		npn.setNomEmpresa(pn.getNomEmpresa());
		
		notificacionesResponse.add(npn);
		Document docNotificar = NotificacionProcesoNomina.toDocument(npn);
		documentosNotificar.add(docNotificar);
	    }

	    //Insertar el listado de nominas a registrar error.
	    System.out.println("Cantidad de documentos a guardar: " + documentosNotificar.size());
	    notificacionService.insNotificacionesProcesoNomina(documentosNotificar);

	    //Envía Mail-----------------------------------------------------------------------------
	    //sendMail(nominasRegistrar);
	    
	    return notificacionesResponse;
	}

	return new ArrayList();
    }

    @POST
    @Path("/marcarleidos")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public void marcarComoLeidos(String[] ides) {
	notificacionService.marcarVariosComoLeido(ides);
    }

    private void sendMail(List<ProcesoNomina> notificaciones) {
	LOG.info("Se ingresa a enviar mail de nominas");
	String destinatarios = new Parametros().getDestinatarios();
	LOG.info("Los destinatarios de Mail son los siguientes:");
	LOG.info(destinatarios);

	String content = "<html><body style='{font-family: Arial, sans-serif;}'>";
	content += "<h2>Informe de problemas en proceso de Nóminas</h2>";
	content += "<br />";
	content += "<p>Existen procesos de Nóminas que no han finalizado dentro de su rango horario o han finalizado con estado error, por lo que se ha generado esta alerta para informar del estado en ejecución.</p>";
	content += "<h3>Detalle:</h3>";
	content += "<table style='border-width: 1px; border-collapse: collapse; font-size:12px;'><thead>";
	content += "<tr>";
	content += "<th style='font-weight:bold;'>ID Empresa</th>";
	content += "<th style='font-weight:bold;'>Cod Empresa</th>";
	content += "<th style='font-weight:bold;'>Empresa</th>";
	content += "<th style='font-weight:bold;'>Hora Ini</th>";
	content += "<th style='font-weight:bold;'>Hora Fin</th>";
	content += "<th style='font-weight:bold;'>Hora Alerta</th>";
	content += "<th style='font-weight:bold;'>Tipo Error</th>";
	content += "<th style='font-weight:bold;'>Estado</th>";
	content += "</tr>";
	content += "</thead><tbody>";
	for (ProcesoNomina notificacion : notificaciones) {
	    content += "<tr>";
	    content += "<td>" + notificacion.getIdEmpresa() + "</td>";
	    content += "<td>" + notificacion.getCodEmpresa() + "</td>";
	    content += "<td>" + notificacion.getNomEmpresa() + "</td>";
	    content += "<td>" + notificacion.getHoraIni() + "</td>";
	    content += "<td>" + notificacion.getHoraFin() + "</td>";
	    content += "<td>" + notificacion.getHoraActual() + "</td>";
	    content += "<td>" + notificacion.getIdEstado() == null ? "Sin Finalizar" : "Terminado con Error" + "</td>";
	    content += "<td>" + notificacion.getIdEstado() == null ? "" : notificacion.getEstado() + "</td>";
	    content += "</tr>";
	}

	content += "</tbody></table>";

	content += "<br />";
	content += "<br />";
	content += "<p>Este es un mensaje generado automáticamente. Por favor no responda este correo.</p>";
	content += "</body></html>";

	List<String> destinos = new ArrayList();
	String[] tos = destinatarios.split(",");

	if (tos.length > 0) {
	    Mail correo = new Mail();
	    correo.setFrom("reporte@reporte.unired.cl");
	    correo.setSubject("¡ATENCIÓN! - PROBLEMAS EN PROCESO NOMINAS ");
	    correo.setText(content);

	    for (String dest : tos) {
		destinos.add(dest);
	    }
	    correo.setTo(destinos);
	    mailer.send(correo);
	}

    }

}
