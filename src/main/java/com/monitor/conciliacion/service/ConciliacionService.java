package com.monitor.conciliacion.service;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import static com.mongodb.client.model.Filters.eq;
import com.mongodb.client.model.Updates;
import com.monitor.model.NotificacionConciliacion;
import com.monitor.model.ProcesoConciliacion;
import com.monitor.util.Parametros;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
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
public class ConciliacionService {

    private final String DATABASE = ConfigProvider.getConfig().getValue("application.database", String.class);
    private final String COLLECTION = ConfigProvider.getConfig().getValue("application.notificaciones.conciliacion.collection", String.class);
    private static final Logger LOG = Logger.getLogger(ConciliacionService.class);

    @Inject
    ConciliacionMapper mapper;

    @Inject
    MongoClient mongoClient;

    @Inject
    Mailer mailer;

    public void buscarConciliacionesProblemasHoy() {
	List<ProcesoConciliacion> conciliaciones = mapper.getConciliacionesHoy();
	if (conciliaciones.size() == 0) { //No existen conciliaciones generadas dentro del horario exigido. Notificar
	    Document documentNotificacionConciliacion = new Document()
		    .append("tipoNotificacion", "conciliacion-no-iniciada")
		    .append("leido", false);
	    getCollection().insertOne(documentNotificacionConciliacion);
	    
	    //Preparar contenido del Mail
	    String content = "<html><body style='{font-family: Arial, sans-serif;}'>";
	    content += "<h2>Informe de proceso de Conciliacón con problemas</h2>";
	    content += "<br />";
	    content += "<p>No se encontraron procesos de conciliación en el horario de su ejecución</p>";
	    content += "<ul>";
	    content += "</ul>";
	    content += "<br />";
	    content += "<br />";
	    content += "<br />";
	    content += "<p>Este es un mensaje generado automáticamente. Por favor no responda este correo.</p>";
	    content += "</body></html>";

	    //Enviar Mail
	    sendMail(content);
	    
	}

	if (conciliaciones.size() == 1) { //Existe solo una fila. Debería haber dos. Revisar para notificar
	    ProcesoConciliacion conciliacion = conciliaciones.get(0);
	    Document documentNotificacionConciliacion = new Document()
		    .append("descripcion", conciliacion.getDescripcion())
		    .append("fechaCreacion", conciliacion.getFechaCreacion())
		    .append("fechaHoraCreacion", conciliacion.getFechaHoraCreacion())
		    .append("idEmpresa", conciliacion.getIdEmpresa())
		    .append("idLogSistema", conciliacion.getIdLogSistema())
		    .append("idTipoLog", conciliacion.getIdTipoLog())
		    .append("mensaje", conciliacion.getMensaje())
		    .append("nombreEps", conciliacion.getNombreEps())
		    .append("leido", false);
	    if (conciliaciones.get(0).getIdTipoLog() == 2) { //Conciliación inició, pero no ha finalizado
		documentNotificacionConciliacion.append("tipoNotificacion", "conciliacion-iniciada-no-finalizada");
		//Preparar contenido del Mail
		String content = "<html><body style='{font-family: Arial, sans-serif;}'>";
		content += "<h2>Informe de proceso de Conciliacón con problemas</h2>";
		content += "<br />";
		content += "<p>El proceso de conciliación inició pero no ha terminado</p>";
		content += "<h3>Detalle:</h3>";
		content += "<ul>";
		content += "<li>Id Log Sistema: <strong>" + conciliacion.getIdLogSistema() + "</strong></li>";
		content += "<li>Id Empresa: <strong>" + conciliacion.getIdEmpresa() + "</strong></li>";
		content += "<li>Empresa: <strong>" + conciliacion.getNombreEps() + "</strong></li>";
		content += "</ul>";
		content += "<br />";
		content += "<br />";
		content += "<br />";
		content += "<p>Este es un mensaje generado automáticamente. Por favor no responda este correo.</p>";
		content += "</body></html>";

		//Enviar Mail
		sendMail(content);
	    }
	    if (conciliaciones.get(0).getIdTipoLog() == 4) { //Conciliación finalizada, pero sin inicio
		documentNotificacionConciliacion.append("tipoNotificacion", "conciliacion-finalizada-error-sin-inicio");
		//Preparar contenido del Mail
		String content = "<html><body style='{font-family: Arial, sans-serif;}'>";
		content += "<h2>Informe de proceso de Conciliacón con problemas</h2>";
		content += "<br />";
		content += "<p>El proceso de conciliación tiene un final de error, pero no se registra inicio</p>";
		content += "<h3>Detalle:</h3>";
		content += "<ul>";
		content += "<li>Id Log Sistema: <strong>" + conciliacion.getIdLogSistema() + "</strong></li>";
		content += "<li>Id Empresa: <strong>" + conciliacion.getIdEmpresa() + "</strong></li>";
		content += "<li>Empresa: <strong>" + conciliacion.getNombreEps() + "</strong></li>";
		content += "</ul>";
		content += "<br />";
		content += "<br />";
		content += "<br />";
		content += "<p>Este es un mensaje generado automáticamente. Por favor no responda este correo.</p>";
		content += "</body></html>";

		//Enviar Mail
		sendMail(content);
	    }
	    if (conciliaciones.get(0).getIdTipoLog() == 6) {
		documentNotificacionConciliacion.append("tipoNotificacion", "conciliacion-finalizada-sin-inicio");
		//Preparar contenido del Mail
		String content = "<html><body style='{font-family: Arial, sans-serif;}'>";
		content += "<h2>Informe de proceso de Conciliacón con problemas</h2>";
		content += "<br />";
		content += "<p>El proceso de conciliación finalizó, pero no se registra inicio</p>";
		content += "<h3>Detalle:</h3>";
		content += "<ul>";
		content += "<li>Id Log Sistema: <strong>" + conciliacion.getIdLogSistema() + "</strong></li>";
		content += "<li>Id Empresa: <strong>" + conciliacion.getIdEmpresa() + "</strong></li>";
		content += "<li>Empresa: <strong>" + conciliacion.getNombreEps() + "</strong></li>";
		content += "</ul>";
		content += "<br />";
		content += "<br />";
		content += "<br />";
		content += "<p>Este es un mensaje generado automáticamente. Por favor no responda este correo.</p>";
		content += "</body></html>";

		//Enviar Mail
		sendMail(content);
	    }
	}
	if (conciliaciones.size() == 2) { //Hay dos filas. Revisar para ver si corresponde.
	    if (conciliaciones.get(1).getIdTipoLog() != 6) {//La conciliación no finalizó con estado éxito. Notificar
		ProcesoConciliacion conciliacion = conciliaciones.get(0);
		Document documentNotificacionConciliacion = new Document()
			.append("descripcion", conciliacion.getDescripcion())
			.append("fechaCreacion", conciliacion.getFechaCreacion())
			.append("fechaHoraCreacion", conciliacion.getFechaHoraCreacion())
			.append("idEmpresa", conciliacion.getIdEmpresa())
			.append("idLogSistema", conciliacion.getIdLogSistema())
			.append("idTipoLog", conciliacion.getIdTipoLog())
			.append("mensaje", conciliacion.getMensaje())
			.append("nombreEps", conciliacion.getNombreEps())
			.append("tipoNotificacion", "conciliacion-finalizada-error")
			.append("leido", false);
		getCollection().insertOne(documentNotificacionConciliacion);

		//Preparar contenido del Mail
		String content = "<html><body style='{font-family: Arial, sans-serif;}'>";
		content += "<h2>Informe de proceso de Conciliacón con problemas</h2>";
		content += "<br />";
		content += "<p>El proceso de conciliación finalizó con estado de error</p>";
		content += "<h3>Detalle:</h3>";
		content += "<ul>";
		content += "<li>Id Log Sistema: <strong>" + conciliacion.getIdLogSistema() + "</strong></li>";
		content += "<li>Id Empresa: <strong>" + conciliacion.getIdEmpresa() + "</strong></li>";
		content += "<li>Empresa: <strong>" + conciliacion.getNombreEps() + "</strong></li>";
		content += "</ul>";
		content += "<br />";
		content += "<br />";
		content += "<br />";
		content += "<p>Este es un mensaje generado automáticamente. Por favor no responda este correo.</p>";
		content += "</body></html>";

		//Enviar Mail
		sendMail(content);
	    }
	}

	if (conciliaciones.size() > 2) { //Hay más de dos filas. Revisar.
	    ProcesoConciliacion inicio = conciliaciones.get(0);
	    ProcesoConciliacion fin = conciliaciones.get(conciliaciones.size() - 1);

	    if (inicio.getIdTipoLog() == 2 && fin.getIdTipoLog() == 4) { //La conciliación inició y terminó con error.
		ProcesoConciliacion conciliacion = conciliaciones.get(0);
		Document documentNotificacionConciliacion = new Document()
			.append("descripcion", conciliacion.getDescripcion())
			.append("fechaCreacion", conciliacion.getFechaCreacion())
			.append("fechaHoraCreacion", conciliacion.getFechaHoraCreacion())
			.append("idEmpresa", conciliacion.getIdEmpresa())
			.append("idLogSistema", conciliacion.getIdLogSistema())
			.append("idTipoLog", conciliacion.getIdTipoLog())
			.append("mensaje", conciliacion.getMensaje())
			.append("nombreEps", conciliacion.getNombreEps())
			.append("tipoNotificacion", "conciliacion-finalizada-error")
			.append("leido", false);
		getCollection().insertOne(documentNotificacionConciliacion);

		//Preparar contenido del Mail
		String content = "<html><body style='{font-family: Arial, sans-serif;}'>";
		content += "<h2>Informe de proceso de Conciliacón con problemas</h2>";
		content += "<br />";
		content += "<p>El proceso de conciliación finalizó con estado de error</p>";
		content += "<h3>Detalle:</h3>";
		content += "<ul>";
		content += "<li>Id Log Sistema: <strong>" + conciliacion.getIdLogSistema() + "</strong></li>";
		content += "<li>Id Empresa: <strong>" + conciliacion.getIdEmpresa() + "</strong></li>";
		content += "<li>Empresa: <strong>" + conciliacion.getNombreEps() + "</strong></li>";
		content += "</ul>";
		content += "<br />";
		content += "<br />";
		content += "<br />";
		content += "<p>Este es un mensaje generado automáticamente. Por favor no responda este correo.</p>";
		content += "</body></html>";
		
		//Enviar Mail
		sendMail(content);
	    }

	    if (inicio.getIdTipoLog() != 2) { //No existe una conciliación de inicio
		ProcesoConciliacion conciliacion = conciliaciones.get(0);
		Document documentNotificacionConciliacion = new Document()
			.append("descripcion", conciliacion.getDescripcion())
			.append("fechaCreacion", conciliacion.getFechaCreacion())
			.append("fechaHoraCreacion", conciliacion.getFechaHoraCreacion())
			.append("idEmpresa", conciliacion.getIdEmpresa())
			.append("idLogSistema", conciliacion.getIdLogSistema())
			.append("idTipoLog", conciliacion.getIdTipoLog())
			.append("mensaje", conciliacion.getMensaje())
			.append("nombreEps", conciliacion.getNombreEps())
			.append("tipoNotificacion", "conciliacion-sin-inicio")
			.append("leido", false);
		getCollection().insertOne(documentNotificacionConciliacion);

		//Preparar contenido del Mail
		String content = "<html><body style='{font-family: Arial, sans-serif;}'>";
		content += "<h2>Informe de proceso de Conciliacón con problemas</h2>";
		content += "<br />";
		content += "<p>El proceso no posee registro de inicio</p>";
		content += "<h3>Detalle:</h3>";
		content += "<ul>";
		content += "<li>Id Log Sistema: <strong>" + conciliacion.getIdLogSistema() + "</strong></li>";
		content += "<li>Id Empresa: <strong>" + conciliacion.getIdEmpresa() + "</strong></li>";
		content += "<li>Empresa: <strong>" + conciliacion.getNombreEps() + "</strong></li>";
		content += "</ul>";
		content += "<br />";
		content += "<br />";
		content += "<br />";
		content += "<p>Este es un mensaje generado automáticamente. Por favor no responda este correo.</p>";
		content += "</body></html>";
		
		//Enviar Mail
		sendMail(content);
	    }
	}
    }
    
    public List<NotificacionConciliacion> getNotificacionesConciliacion(){
	MongoCursor<Document> cursorConciliacion = getCollection().find().iterator();
	List<NotificacionConciliacion> notificaciones = new ArrayList();
	while(cursorConciliacion.hasNext()){
	    Document documentoConciliacion = cursorConciliacion.next();
	    NotificacionConciliacion notificacion = new NotificacionConciliacion();
	    notificacion.setLeido(documentoConciliacion.getBoolean("leido"));
	    notificacion.setTipoNotificacion(documentoConciliacion.getString("tipoNotificacion"));
	    if(!notificacion.getTipoNotificacion().equals("conciliacion-no-iniciada")){
		ProcesoConciliacion conciliacion = new ProcesoConciliacion();
		conciliacion.setDescripcion(documentoConciliacion.getString("descripcion"));
		conciliacion.setFechaCreacion(documentoConciliacion.getDate("fechaCreacion").toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		conciliacion.setFechaHoraCreacion(documentoConciliacion.getDate("fechaHoraCreacion").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
		conciliacion.setIdEmpresa(documentoConciliacion.getString("idEmpresa"));
		conciliacion.setIdLogSistema(documentoConciliacion.getInteger("idLogSistema"));
		conciliacion.setMensaje(documentoConciliacion.getString("mensaje"));
		conciliacion.setNombreEps(documentoConciliacion.getString("nombreEps"));
		notificacion.setConciliacion(conciliacion);
	    }
	    notificaciones.add(notificacion);
	}
	return notificaciones;
    }
    
    public List<NotificacionConciliacion> getNotificacionesConciliacionNoLeidas(){
	MongoCursor<Document> cursorConciliacion = getCollection().find(Filters.eq("leido", false)).iterator();
	List<NotificacionConciliacion> notificaciones = new ArrayList();
	while(cursorConciliacion.hasNext()){
	    Document documentoConciliacion = cursorConciliacion.next();
	    NotificacionConciliacion notificacion = new NotificacionConciliacion();
	    notificacion.setLeido(documentoConciliacion.getBoolean("leido"));
	    notificacion.setTipoNotificacion(documentoConciliacion.getString("tipoNotificacion"));
	    if(!notificacion.getTipoNotificacion().equals("conciliacion-no-iniciada")){
		ProcesoConciliacion conciliacion = new ProcesoConciliacion();
		conciliacion.setDescripcion(documentoConciliacion.getString("descripcion"));
		conciliacion.setFechaCreacion(documentoConciliacion.getDate("fechaCreacion").toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		conciliacion.setFechaHoraCreacion(documentoConciliacion.getDate("fechaHoraCreacion").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
		conciliacion.setIdEmpresa(documentoConciliacion.getString("idEmpresa"));
		conciliacion.setIdLogSistema(documentoConciliacion.getInteger("idLogSistema"));
		conciliacion.setMensaje(documentoConciliacion.getString("mensaje"));
		conciliacion.setNombreEps(documentoConciliacion.getString("nombreEps"));
		notificacion.setConciliacion(conciliacion);
	    }
	    notificaciones.add(notificacion);
	}
	return notificaciones;
    }
    
    public void marcarComoLeido(String oid){
	Document documentoNotificacion = (Document) getCollection().find(Filters.eq("_id", new ObjectId(oid))).first();
	if(documentoNotificacion != null){
	    getCollection().updateOne(eq("_id", new ObjectId(oid)), Updates.set("leido", true));
	}
    }
    
    public void marcarVariosComoLeido(String[] oids){
	for(String oid : oids){
	    marcarComoLeido(oid);
	}
    }

    private MongoCollection getCollection() {
	return mongoClient.getDatabase(DATABASE).getCollection(COLLECTION);
    }

    private void sendMail(String contenido) {
	LOG.info("Se ingresa a enviar mail");
	String destinatarios = new Parametros().getDestinatarios();
	LOG.info("Los destinatarios de Mail son los siguientes:");
	LOG.info(destinatarios);

	String content = contenido;

	List<String> destinos = new ArrayList();
	String[] tos = destinatarios.split(",");

	if (tos.length > 0) {
	    Mail correo = new Mail();
	    correo.setFrom("reporte@reporte.unired.cl");
	    correo.setSubject("¡ATENCIÓN! - PROBLEMAS EN PROCESO CONCILIACIÓN #");
	    correo.setText(content);

	    for (String dest : tos) {
		destinos.add(dest);
	    }
	    correo.setTo(destinos);
	    mailer.send(correo);
	}

    }

}
