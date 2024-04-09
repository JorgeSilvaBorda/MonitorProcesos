package com.monitor.extract.service;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import static com.mongodb.client.model.Filters.eq;
import com.mongodb.client.model.Updates;
import com.monitor.model.NotificacionExtract;
import com.monitor.model.ProcesoExtract;
import com.monitor.util.Parametros;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.eclipse.microprofile.config.ConfigProvider;
import org.jboss.logging.Logger;

@ApplicationScoped
public class ExtractService {

    private final String DATABASE = ConfigProvider.getConfig().getValue("application.database", String.class);
    private final String COLLECTION = ConfigProvider.getConfig().getValue("application.notificaciones.extract.collection", String.class);
    private static final Logger LOG = Logger.getLogger(ExtractService.class);

    @Inject
    ExtractMapper mapper;

    @Inject
    MongoClient mongoClient;

    @Inject
    Mailer mailer;

    public void buscarExtractProblemasHoy() {
	LocalDateTime fechaActual = LocalDateTime.now();
	String fechaRegistroNotificacion = fechaActual.toLocalDate().toString();
	List<ProcesoExtract> extracts = mapper.getExtractHoy();
	if (extracts.size() == 0) { //No existen extract generadas dentro del horario exigido. Notificar
	    Document documentNotificacionExtract = new Document()
		    .append("fechaNotificacion", fechaRegistroNotificacion)
		    .append("tipoNotificacion", "extract-no-iniciado")
		    .append("leido", false);
	    getCollection().insertOne(documentNotificacionExtract);

	    //Preparar contenido del Mail
	    String content = "<html><body style='{font-family: Arial, sans-serif;}'>";
	    content += "<h2>Informe de proceso de Extract con problemas</h2>";
	    content += "<br />";
	    content += "<p>No se encontraron procesos de extract en el horario de su ejecución (02:20:00 - 02:25:59)</p>";
	    content += "<ul>";
	    content += "</ul>";
	    content += "<br />";
	    content += "<br />";
	    content += "<br />";
	    content += "<p>Este es un mensaje generado automáticamente. Por favor no responda este correo.</p>";
	    content += "</body></html>";

	    //Enviar Mail
	    sendMail(content, LocalDateTime.now().toString());
	}

	if (extracts.size() == 1) { //Existe solo una fila. Debería haber dos. Revisar para notificar
	    ProcesoExtract extract = extracts.get(0);
	    Document documentNotificacionExtract = new Document()
		    .append("fechaNotificacion", fechaRegistroNotificacion)
		    .append("descripcion", extract.getDescripcion())
		    .append("fechaCreacion", extract.getFechaCreacion())
		    .append("fechaHoraCreacion", extract.getFechaHoraCreacion())
		    .append("idEmpresa", extract.getIdEmpresa())
		    .append("idLogSistema", extract.getIdLogSistema())
		    .append("idTipoLog", extract.getIdTipoLog())
		    .append("mensaje", extract.getMensaje())
		    .append("nombreEps", extract.getNombreEps())
		    .append("leido", false);
	    if (extracts.get(0).getIdTipoLog() == 2) { //Extract inició, pero no ha finalizado
		documentNotificacionExtract.append("tipoNotificacion", "extract-iniciado-no-finalizado");
		getCollection().insertOne(documentNotificacionExtract);
		//Preparar contenido del Mail
		String content = "<html><body style='{font-family: Arial, sans-serif;}'>";
		content += "<h2>Informe de proceso de Extract con problemas</h2>";
		content += "<br />";
		content += "<p>El proceso de extract inició pero no ha terminado</p>";
		content += "<h3>Detalle:</h3>";
		content += "<ul>";
		content += "<li>Id Log Sistema: <strong>" + extract.getIdLogSistema() + "</strong></li>";
		content += "<li>Id Empresa: <strong>" + extract.getIdEmpresa() + "</strong></li>";
		content += "<li>Empresa: <strong>" + extract.getNombreEps() + "</strong></li>";
		content += "</ul>";
		content += "<br />";
		content += "<br />";
		content += "<br />";
		content += "<p>Este es un mensaje generado automáticamente. Por favor no responda este correo.</p>";
		content += "</body></html>";

		//Enviar Mail
		sendMail(content, LocalDateTime.now().toString());
	    }
	    if (extracts.get(0).getIdTipoLog() == 4) { //Extract finalizado, pero sin inicio
		documentNotificacionExtract.append("tipoNotificacion", "extract-finalizado-error-sin-inicio");
		getCollection().insertOne(documentNotificacionExtract);
		//Preparar contenido del Mail
		String content = "<html><body style='{font-family: Arial, sans-serif;}'>";
		content += "<h2>Informe de proceso de Extract con problemas</h2>";
		content += "<br />";
		content += "<p>El proceso de extract tiene un final de error, pero no se registra inicio</p>";
		content += "<h3>Detalle:</h3>";
		content += "<ul>";
		content += "<li>Id Log Sistema: <strong>" + extract.getIdLogSistema() + "</strong></li>";
		content += "<li>Id Empresa: <strong>" + extract.getIdEmpresa() + "</strong></li>";
		content += "<li>Empresa: <strong>" + extract.getNombreEps() + "</strong></li>";
		content += "</ul>";
		content += "<br />";
		content += "<br />";
		content += "<br />";
		content += "<p>Este es un mensaje generado automáticamente. Por favor no responda este correo.</p>";
		content += "</body></html>";

		//Enviar Mail
		sendMail(content, LocalDateTime.now().toString());
	    }
	    if (extracts.get(0).getIdTipoLog() == 6) {
		documentNotificacionExtract.append("tipoNotificacion", "extract-finalizado-sin-inicio");
		getCollection().insertOne(documentNotificacionExtract);
		//Preparar contenido del Mail
		String content = "<html><body style='{font-family: Arial, sans-serif;}'>";
		content += "<h2>Informe de proceso de Extract con problemas</h2>";
		content += "<br />";
		content += "<p>El proceso de extract finalizó, pero no se registra inicio</p>";
		content += "<h3>Detalle:</h3>";
		content += "<ul>";
		content += "<li>Id Log Sistema: <strong>" + extract.getIdLogSistema() + "</strong></li>";
		content += "<li>Id Empresa: <strong>" + extract.getIdEmpresa() + "</strong></li>";
		content += "<li>Empresa: <strong>" + extract.getNombreEps() + "</strong></li>";
		content += "</ul>";
		content += "<br />";
		content += "<br />";
		content += "<br />";
		content += "<p>Este es un mensaje generado automáticamente. Por favor no responda este correo.</p>";
		content += "</body></html>";

		//Enviar Mail
		sendMail(content, LocalDateTime.now().toString());
	    }
	}
	if (extracts.size() == 2) { //Hay dos filas. Revisar para ver si corresponde.
	    if (extracts.get(1).getIdTipoLog() != 6) {//La extract no finalizó con estado éxito. Notificar
		ProcesoExtract extract = extracts.get(0);
		Document documentNotificacionExtract = new Document()
			.append("fechaNotificacion", fechaRegistroNotificacion)
			.append("descripcion", extract.getDescripcion())
			.append("fechaCreacion", extract.getFechaCreacion())
			.append("fechaHoraCreacion", extract.getFechaHoraCreacion())
			.append("idEmpresa", extract.getIdEmpresa())
			.append("idLogSistema", extract.getIdLogSistema())
			.append("idTipoLog", extract.getIdTipoLog())
			.append("mensaje", extract.getMensaje())
			.append("nombreEps", extract.getNombreEps())
			.append("tipoNotificacion", "extract-finalizado-error")
			.append("leido", false);
		getCollection().insertOne(documentNotificacionExtract);

		//Preparar contenido del Mail
		String content = "<html><body style='{font-family: Arial, sans-serif;}'>";
		content += "<h2>Informe de proceso de Extract con problemas</h2>";
		content += "<br />";
		content += "<p>El proceso de extract finalizó con estado de error</p>";
		content += "<h3>Detalle:</h3>";
		content += "<ul>";
		content += "<li>Id Log Sistema: <strong>" + extract.getIdLogSistema() + "</strong></li>";
		content += "<li>Id Empresa: <strong>" + extract.getIdEmpresa() + "</strong></li>";
		content += "<li>Empresa: <strong>" + extract.getNombreEps() + "</strong></li>";
		content += "</ul>";
		content += "<br />";
		content += "<br />";
		content += "<br />";
		content += "<p>Este es un mensaje generado automáticamente. Por favor no responda este correo.</p>";
		content += "</body></html>";

		//Enviar Mail
		sendMail(content, LocalDateTime.now().toString());
	    }
	}

	if (extracts.size() > 2) { //Hay más de dos filas. Revisar.
	    ProcesoExtract inicio = extracts.get(0);
	    ProcesoExtract fin = extracts.get(extracts.size() - 1);

	    if (inicio.getIdTipoLog() == 2 && fin.getIdTipoLog() == 4) { //La extract inició y terminó con error.
		ProcesoExtract extract = extracts.get(0);
		Document documentNotificacionExtract = new Document()
			.append("fechaNotificacion", fechaRegistroNotificacion)
			.append("descripcion", extract.getDescripcion())
			.append("fechaCreacion", extract.getFechaCreacion())
			.append("fechaHoraCreacion", extract.getFechaHoraCreacion())
			.append("idEmpresa", extract.getIdEmpresa())
			.append("idLogSistema", extract.getIdLogSistema())
			.append("idTipoLog", extract.getIdTipoLog())
			.append("mensaje", extract.getMensaje())
			.append("nombreEps", extract.getNombreEps())
			.append("tipoNotificacion", "extract-finalizado-error")
			.append("leido", false);
		getCollection().insertOne(documentNotificacionExtract);

		//Preparar contenido del Mail
		String content = "<html><body style='{font-family: Arial, sans-serif;}'>";
		content += "<h2>Informe de proceso de Extract con problemas</h2>";
		content += "<br />";
		content += "<p>El proceso de extract finalizó con estado de error</p>";
		content += "<h3>Detalle:</h3>";
		content += "<ul>";
		content += "<li>Id Log Sistema: <strong>" + extract.getIdLogSistema() + "</strong></li>";
		content += "<li>Id Empresa: <strong>" + extract.getIdEmpresa() + "</strong></li>";
		content += "<li>Empresa: <strong>" + extract.getNombreEps() + "</strong></li>";
		content += "</ul>";
		content += "<br />";
		content += "<br />";
		content += "<br />";
		content += "<p>Este es un mensaje generado automáticamente. Por favor no responda este correo.</p>";
		content += "</body></html>";

		//Enviar Mail
		sendMail(content, LocalDateTime.now().toString());
	    }

	    if (inicio.getIdTipoLog() != 2) { //No existe un extract de inicio
		ProcesoExtract extract = extracts.get(0);
		Document documentNotificacionExtract = new Document()
			.append("fechaNotificacion", fechaRegistroNotificacion)
			.append("descripcion", extract.getDescripcion())
			.append("fechaCreacion", extract.getFechaCreacion())
			.append("fechaHoraCreacion", extract.getFechaHoraCreacion())
			.append("idEmpresa", extract.getIdEmpresa())
			.append("idLogSistema", extract.getIdLogSistema())
			.append("idTipoLog", extract.getIdTipoLog())
			.append("mensaje", extract.getMensaje())
			.append("nombreEps", extract.getNombreEps())
			.append("tipoNotificacion", "extract-sin-inicio")
			.append("leido", false);
		getCollection().insertOne(documentNotificacionExtract);

		//Preparar contenido del Mail
		String content = "<html><body style='{font-family: Arial, sans-serif;}'>";
		content += "<h2>Informe de proceso de Extract con problemas</h2>";
		content += "<br />";
		content += "<p>El proceso no posee registro de inicio</p>";
		content += "<h3>Detalle:</h3>";
		content += "<ul>";
		content += "<li>Id Log Sistema: <strong>" + extract.getIdLogSistema() + "</strong></li>";
		content += "<li>Id Empresa: <strong>" + extract.getIdEmpresa() + "</strong></li>";
		content += "<li>Empresa: <strong>" + extract.getNombreEps() + "</strong></li>";
		content += "</ul>";
		content += "<br />";
		content += "<br />";
		content += "<br />";
		content += "<p>Este es un mensaje generado automáticamente. Por favor no responda este correo.</p>";
		content += "</body></html>";

		//Enviar Mail
		sendMail(content, LocalDateTime.now().toString());
	    }
	}
    }

    public List<NotificacionExtract> getNotificacionesExtract() {
	MongoCursor<Document> cursorExtract = getCollection().find().iterator();
	List<NotificacionExtract> notificaciones = new ArrayList();
	while (cursorExtract.hasNext()) {
	    Document documentoExtract = cursorExtract.next();
	    NotificacionExtract notificacion = new NotificacionExtract();
	    notificacion.setLeido(documentoExtract.getBoolean("leido"));
	    notificacion.set_id(documentoExtract.getObjectId("_id"));
	    notificacion.setTipoNotificacion(documentoExtract.getString("tipoNotificacion"));
	    notificacion.setFechaNotificacion(documentoExtract.getString("fechaNotificacion"));
	    if (!notificacion.getTipoNotificacion().equals("extract-no-iniciado")) {
		ProcesoExtract extract = new ProcesoExtract();
		extract.setDescripcion(documentoExtract.getString("descripcion"));
		extract.setFechaCreacion(documentoExtract.getDate("fechaCreacion").toInstant().atZone(ZoneId.of("America/Santiago")).toLocalDate());
		extract.setFechaHoraCreacion(documentoExtract.getDate("fechaHoraCreacion").toInstant().atZone(ZoneId.of("America/Santiago")).toLocalDateTime());
		extract.setIdEmpresa(documentoExtract.getString("idEmpresa"));
		extract.setIdLogSistema(documentoExtract.getInteger("idLogSistema"));
		extract.setIdTipoLog(documentoExtract.getInteger("idTipoLog"));
		extract.setMensaje(documentoExtract.getString("mensaje"));
		extract.setNombreEps(documentoExtract.getString("nombreEps"));
		notificacion.setExtract(extract);
	    }
	    notificaciones.add(notificacion);
	}
	return notificaciones;
    }

    public List<NotificacionExtract> getNotificacionesExtractNoLeidas() {
	MongoCursor<Document> cursorExtract = getCollection().find(Filters.eq("leido", false)).iterator();
	List<NotificacionExtract> notificaciones = new ArrayList();
	while (cursorExtract.hasNext()) {
	    Document documentoExtract = cursorExtract.next();
	    NotificacionExtract notificacion = new NotificacionExtract();
	    notificacion.set_id(documentoExtract.getObjectId("_id"));
	    notificacion.setLeido(documentoExtract.getBoolean("leido"));
	    notificacion.setFechaNotificacion(documentoExtract.getString("fechaNotificacion"));
	    notificacion.setTipoNotificacion(documentoExtract.getString("tipoNotificacion"));
	    if (!notificacion.getTipoNotificacion().equals("extract-no-iniciado")) {
		ProcesoExtract extract = new ProcesoExtract();
		extract.setDescripcion(documentoExtract.getString("descripcion"));
		extract.setFechaCreacion(documentoExtract.getDate("fechaCreacion").toInstant().atZone(ZoneId.of("America/Santiago")).toLocalDate());
		extract.setFechaHoraCreacion(documentoExtract.getDate("fechaHoraCreacion").toInstant().atZone(ZoneId.of("America/Santiago")).toLocalDateTime());
		extract.setIdEmpresa(documentoExtract.getString("idEmpresa"));
		extract.setIdLogSistema(documentoExtract.getInteger("idLogSistema"));
		extract.setIdTipoLog(documentoExtract.getInteger("idTipoLog"));
		extract.setMensaje(documentoExtract.getString("mensaje"));
		extract.setNombreEps(documentoExtract.getString("nombreEps"));
		notificacion.setExtract(extract);
	    }
	    notificaciones.add(notificacion);
	}
	return notificaciones;
    }

    public boolean existenNotificacionesHoy() {
	LocalDateTime fechaActual = LocalDateTime.now();
	Document documentNotificacion = (Document) getCollection().find(Filters.eq("fechaNotificacion", fechaActual.toLocalDate().toString())).first();
	System.out.println(documentNotificacion);
	return documentNotificacion != null;
    }

    public void marcarComoLeido(String oid) {
	Document documentoNotificacion = (Document) getCollection().find(Filters.eq("_id", new ObjectId(oid))).first();
	if (documentoNotificacion != null) {
	    getCollection().updateOne(eq("_id", new ObjectId(oid)), Updates.set("leido", true));
	}
    }

    public void marcarVariosComoLeido(String[] oids) {
	for (String oid : oids) {
	    marcarComoLeido(oid);
	}
    }

    private MongoCollection getCollection() {
	return mongoClient.getDatabase(DATABASE).getCollection(COLLECTION);
    }

    private void sendMail(String contenido, String fechaHoraProceso) {
	LOG.info("Se ingresa a enviar mail");
	String destinatarios = new Parametros().getDestinatarios();
	LOG.info("Los destinatarios de Mail son los siguientes:");
	LOG.info(destinatarios);
	fechaHoraProceso = fechaHoraProceso.replace("T", " ");
	fechaHoraProceso = fechaHoraProceso.substring(0, 19);

	String content = contenido;

	List<String> destinos = new ArrayList();
	String[] tos = destinatarios.split(",");

	if (tos.length > 0) {
	    Mail correo = new Mail();
	    correo.setFrom("reporte@reporte.unired.cl");
	    correo.setSubject("¡ATENCIÓN! - PROBLEMAS EN PROCESO EXTRACT. Fecha: " + fechaHoraProceso);
	    correo.setText(content);

	    for (String dest : tos) {
		destinos.add(dest);
	    }
	    correo.setTo(destinos);
	    mailer.send(correo);
	}

    }
}
