package com.monitor.service;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import static com.mongodb.client.model.Filters.eq;
import com.mongodb.client.model.Updates;
import com.monitor.model.NotificacionProceso;
import com.monitor.model.ProcesoRendicion;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.eclipse.microprofile.config.ConfigProvider;

@ApplicationScoped
public class NotificacionProcesoService {

    private final String DATABASE = ConfigProvider.getConfig().getValue("application.database", String.class);
    private final String COLLECTION = ConfigProvider.getConfig().getValue("application.notificaciones.collection", String.class);

    @Inject
    MongoClient mongoClient;

    public NotificacionProceso postNotificacionProceso(NotificacionProceso notificacion) {
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

	getCollection().insertOne(documentNotificacion);
	NotificacionProceso documentoInsertado = notificacion;
	documentoInsertado.setId(documentNotificacion.getObjectId("_id"));
	return documentoInsertado;
    }

    public NotificacionProceso getNotificacionProcesoByIdProceso(Integer idProceso) {
	Document documentNotificacion = (Document) getCollection().find(Filters.eq("idProceso", idProceso)).first();
	if (documentNotificacion == null) {
	    return null;
	}
	NotificacionProceso notificacion = new NotificacionProceso();
	notificacion.setId(documentNotificacion.getObjectId("_id"));
	notificacion.setIdProceso(documentNotificacion.getInteger("idProceso"));
	notificacion.setLeido(documentNotificacion.getBoolean("leido"));
	//no se está cargando el detalle de los procesos leídos para la notificación.
	//Cargarlos en caso de ser necesario.

	return notificacion;
    }

    public void marcarComoLeido(String oid) {
	System.out.println(oid);
	Document documentNotificacion = (Document) getCollection().find(Filters.eq("_id", new ObjectId(oid))).first();
	System.out.println(documentNotificacion);
	if (documentNotificacion != null) {
	    System.out.println("Existe");
	    getCollection().updateOne(eq("_id", new ObjectId(oid)), Updates.set("leido", true));
	}
    }

    private MongoCollection getCollection() {
	return mongoClient.getDatabase(DATABASE).getCollection(COLLECTION);
    }
}
