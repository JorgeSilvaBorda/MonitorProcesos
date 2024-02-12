package com.monitor.nomina.service;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import static com.mongodb.client.model.Filters.eq;
import com.mongodb.client.model.Updates;
import com.monitor.model.NotificacionNomina;
import com.monitor.model.ProcesoNomina;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.eclipse.microprofile.config.ConfigProvider;

@ApplicationScoped
public class NotificacionNominaService {
    
    private final String DATABASE = ConfigProvider.getConfig().getValue("application.database", String.class);
    private final String COLLECTION = ConfigProvider.getConfig().getValue("application.notificaciones.nominas.collection", String.class);
    
    @Inject
    MongoClient mongoClient;
    
    public List<Document> insNotificacionesProcesoNomina(List<Document> notificaciones){
	getCollection().insertMany(notificaciones);
	return notificaciones;
    }
    
    public List<ProcesoNomina> getProcesosNominaRegistradosError(LocalDate fechaCarga){
	List<ProcesoNomina> procesosNomina = new ArrayList();
	MongoCursor<Document> documentosNomina = getCollection()
		.find(Filters.eq("fechaCarga", fechaCarga))
		.iterator();
	try{
	    while(documentosNomina.hasNext()){
		procesosNomina.add(ProcesoNomina.fromDocument(documentosNomina.next()));
	    }
	    return procesosNomina;
	}catch (Exception ex) {
	    System.out.println("No se pudo obtener el listado de procesos nómina para la fecha de carga solicitada");
	    System.out.println(ex);
	    return new ArrayList();
	}
    }
    
    public List<NotificacionNomina> getProcesosNominaRegistradosNoLeidos(){
	List<NotificacionNomina> notificaciones = new ArrayList();
	MongoCursor<Document> documentosNomina = getCollection()
		.find(Filters.eq("leido", false))
		.iterator();
	try{
	    while(documentosNomina.hasNext()){
		notificaciones.add(NotificacionNomina.fromDocument(documentosNomina.next()));
	    }
	    //System.out.println("Hay " + notificaciones.size() + " Notificaciones de Nominas no leídas");
	    return notificaciones;
	}catch (Exception ex) {
	    System.out.println("No se pudo obtener el listado de notificaciones nómina no leídas");
	    System.out.println(ex);
	    ex.printStackTrace();
	    return new ArrayList();
	}
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
    
    private MongoCollection getCollection() {
	return mongoClient.getDatabase(DATABASE).getCollection(COLLECTION);
    }
}
