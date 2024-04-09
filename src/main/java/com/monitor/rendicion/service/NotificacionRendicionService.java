package com.monitor.rendicion.service;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import static com.mongodb.client.model.Filters.eq;
import com.mongodb.client.model.Updates;
import com.monitor.model.NotificacionRendicion;
import com.monitor.model.ProcesoRendicion;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.eclipse.microprofile.config.ConfigProvider;

@ApplicationScoped
public class NotificacionRendicionService {

    private final String DATABASE = ConfigProvider.getConfig().getValue("application.database", String.class);
    private final String COLLECTION = ConfigProvider.getConfig().getValue("application.notificaciones.collection", String.class);

    @Inject
    MongoClient mongoClient;

    public NotificacionRendicion postNotificacionProceso(NotificacionRendicion notificacion) {
	Document documentNotificacion = new Document();
	documentNotificacion = notificacionClassToDocument(notificacion);
	getCollection().insertOne(documentNotificacion);
	NotificacionRendicion documentoInsertado = notificacion;
	documentoInsertado.set_id(documentNotificacion.getObjectId("_id"));
	return documentoInsertado;
    }

    public NotificacionRendicion getNotificacionProcesoByIdProceso(Integer idProceso) {
	Document documentNotificacion = (Document) getCollection().find(Filters.eq("idProceso", idProceso)).first();
	if (documentNotificacion == null) {
	    return null;
	}
	NotificacionRendicion notificacion = new NotificacionRendicion();
	notificacion.set_id(documentNotificacion.getObjectId("_id"));
	notificacion.setIdProceso(documentNotificacion.getInteger("idProceso"));
	notificacion.setLeido(documentNotificacion.getBoolean("leido"));
	//no se está cargando el detalle de los procesos leídos para la notificación.
	//Cargarlos en caso de ser necesario.

	return notificacion;
    }

    public void marcarComoLeido(String oid) {
	Document documentNotificacion = (Document) getCollection().find(Filters.eq("_id", new ObjectId(oid))).first();
	if (documentNotificacion != null) {
	    getCollection().updateOne(eq("_id", new ObjectId(oid)), Updates.set("leido", true));
	}
    }

    public void marcarVariosComoLeido(String[] oids) {
	for (String oid : oids) {
	    marcarComoLeido(oid);
	}
    }

    public List<NotificacionRendicion> getNotificacionesNoLeidas() {
	MongoCursor<Document> notificacionesProceso = getCollection().find(Filters.eq("leido", false)).iterator();
	
	List<NotificacionRendicion> notificaciones = new ArrayList();
	boolean alMenosUna = false;
	try {
	    while (notificacionesProceso.hasNext()) {
		Document notificacionProceso = notificacionesProceso.next();
		NotificacionRendicion not = notificacionDocumentToClass(notificacionProceso);
		notificaciones.add(not);
	    }
	} catch (Exception ex) {
	    System.out.println("No se pudo obtener el listado de notificaciones sin leer.");
	    ex.printStackTrace();
	    System.out.println(ex);
	}
	return notificaciones;
    }

    //Conversores de clase y documento-------------------------------------------------------------------
    private NotificacionRendicion notificacionDocumentToClass(Document docNotificacion) {
	NotificacionRendicion notificacion = new NotificacionRendicion();
	notificacion.set_id(docNotificacion.getObjectId("_id"));
	notificacion.setIdProceso(docNotificacion.getInteger("idProceso"));
	notificacion.setLeido(docNotificacion.getBoolean("leido"));
	notificacion.setTiempoPermitido(docNotificacion.getInteger("tiempoPermitido"));
	List<Document> procesos = docNotificacion.getList("procesosRendicion", Document.class);

	if (procesos != null) {
	    List<ProcesoRendicion> procesosRendicion = new ArrayList();
	    int cont = 0;
	    for (Document docProceso : procesos) {
		ProcesoRendicion proceso = new ProcesoRendicion();
		proceso.setIdProceso(docProceso.getInteger("idProceso"));
		proceso.setIdEmpresa(docProceso.getString("idEmpresa"));
		proceso.setNombreEps(docProceso.getString("nombreEps"));
		proceso.setCodEstado(docProceso.getInteger("codEstado"));
		proceso.setEstadoProceso(docProceso.getString("estadoProceso"));
		proceso.setFechaProceso(docProceso.getDate("fechaProceso").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
		proceso.setFechaCreacion(docProceso.getDate("fechaCreacion").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
		proceso.setInicioProceso(docProceso.getDate("inicioProceso").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
		proceso.setFinProceso(docProceso.getDate("finProceso").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
		proceso.setFechaIso(docProceso.getString("fechaIso"));
		proceso.setIdDefinicionArchivos(docProceso.getInteger("idDefinicionArchivos"));
		proceso.setTipoCorte(docProceso.getInteger("tipoCorte"));
		proceso.setIdTarea(docProceso.getInteger("idTarea"));
		proceso.setHoraEjecucion(docProceso.getString("horaEjecucion"));
		proceso.setVigente(docProceso.getString("vigente"));
		proceso.setFechaHoraConsulta(docProceso.getDate("fechaHoraConsulta").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
		procesosRendicion.add(proceso);
	    }
	    notificacion.setProcesosRendicion(procesosRendicion);
	}
	return notificacion;
    }

    private Document notificacionClassToDocument(NotificacionRendicion notificacion) {
	Document documentNotificacion = new Document();
	documentNotificacion
		.append("idProceso", notificacion.getIdProceso())
		.append("leido", notificacion.isLeido())
		.append("tiempoPermitido", notificacion.getTiempoPermitido());

	List<Document> documentsProcesosRendicion = new ArrayList();
	for (ProcesoRendicion proc : notificacion.getProcesosRendicion()) {
	    Document documentProcesoRendicion = new Document();
	    documentProcesoRendicion
		    .append("idProceso", proc.getIdProceso())
		    .append("idEmpresa", proc.getIdEmpresa())
		    .append("nombreEps", proc.getNombreEps())
		    .append("codEstado", proc.getCodEstado())
		    .append("estadoProceso", proc.getEstadoProceso())
		    .append("fechaProceso", proc.getFechaProceso())
		    .append("fechaCreacion", proc.getFechaCreacion())
		    .append("inicioProceso", proc.getInicioProceso())
		    .append("finProceso", proc.getFinProceso())
		    .append("fechaIso", proc.getFechaIso())
		    .append("idDefinicionArchivos", proc.getIdDefinicionArchivos())
		    .append("tipoCorte", proc.getTipoCorte())
		    .append("idTarea", proc.getIdTarea())
		    .append("horaEjecucion", proc.getHoraEjecucion())
		    .append("vigente", proc.getVigente())
		    .append("fechaHoraConsulta", proc.getFechaHoraConsulta());

	    documentsProcesosRendicion.add(documentProcesoRendicion);
	}
	documentNotificacion.append("procesosRendicion", documentsProcesosRendicion);
	return documentNotificacion;
    }

    private MongoCollection getCollection() {
	return mongoClient.getDatabase(DATABASE).getCollection(COLLECTION);
    }
}
