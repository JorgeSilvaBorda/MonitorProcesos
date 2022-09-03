package com.monitor.service;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.monitor.model.ProcesoRendicion;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.bson.Document;
import org.eclipse.microprofile.config.ConfigProvider;

@ApplicationScoped
public class ProcesoRendicionService {

    private final String DATABASE = ConfigProvider.getConfig().getValue("application.database", String.class);
    private final String COLLECTION = ConfigProvider.getConfig().getValue("application.rendiciones.collection", String.class);
    

    @Inject
    MongoClient mongoClient;

    public ProcesoRendicion insProcesoRendicionActivo(ProcesoRendicion proceso) {
	Document rendicionDocument = new Document();
	rendicionDocument
		.append("idProceso", proceso.getIdProceso())
		.append("idEmpresa", proceso.getIdEmpresa())
		.append("nombreEps", proceso.getNombreEps())
		.append("codEstado", proceso.getCodEstado())
		.append("estadoProceso", proceso.getEstadoProceso())
		.append("fechaProceso", proceso.getFechaProceso())
		.append("fechaCreacion", proceso.getFechaCreacion())
		.append("inicioProceso", proceso.getInicioProceso())
		.append("finProceso", proceso.getFinProceso())
		.append("fechaIso", proceso.getFechaIso())
		.append("idDefinicionArchivos", proceso.getIdDefinicionArchivos())
		.append("tipoCorte", proceso.getTipoCorte())
		.append("idTarea", proceso.getIdTarea())
		.append("horaEjecucion", proceso.getHoraEjecucion())
		.append("vigente", proceso.getVigente())
		.append("fechaHoraConsulta", LocalDateTime.now());
	getCollection().insertOne(rendicionDocument);

	ProcesoRendicion procesoInsertado = proceso;

	return proceso;
    }

    public ProcesoRendicion getProcesoMonitorByIdProceso(Integer idProceso) {
	Document rendicionDocument = (Document) getCollection()
		.find(Filters.eq("idProceso", idProceso))
		.sort(Sorts.descending("fechaHoraConsulta"))
		.first();
	System.out.println(rendicionDocument);
	if (rendicionDocument == null) {
	    return null;
	}
	ProcesoRendicion proceso = new ProcesoRendicion();
	proceso.setIdProceso(rendicionDocument.getInteger("idProceso"));
	proceso.setIdEmpresa(rendicionDocument.getString("idEmpresa"));
	proceso.setNombreEps(rendicionDocument.getString("nombreEps"));
	proceso.setCodEstado(rendicionDocument.getInteger("codEstado"));
	proceso.setEstadoProceso(rendicionDocument.getString("estadoProceso"));
	proceso.setFechaProceso(rendicionDocument.getDate("fechaProceso").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
	proceso.setFechaCreacion(rendicionDocument.getDate("fechaCreacion").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
	proceso.setInicioProceso(rendicionDocument.getDate("inicioProceso").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
	proceso.setFinProceso(rendicionDocument.getDate("finProceso").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
	proceso.setFechaIso(rendicionDocument.getString("fechaIso"));
	proceso.setIdDefinicionArchivos(rendicionDocument.getInteger("idDefinicionArchivos"));
	proceso.setTipoCorte(rendicionDocument.getInteger("tipoCorte"));
	proceso.setIdTarea(rendicionDocument.getInteger("idTarea"));
	proceso.setHoraEjecucion(rendicionDocument.getString("horaEjecucion"));
	proceso.setVigente(rendicionDocument.getString("vigente"));
	proceso.setFechaHoraConsulta(rendicionDocument.getDate("fechaHoraConsulta").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());

	return proceso;
    }

    public List<ProcesoRendicion> getProcesosMonitorByIdProceso(Integer idProceso) {
	List<ProcesoRendicion> procesosRendicion = new ArrayList();
	MongoCursor<Document> documentosProceso = getCollection()
		.find(Filters.eq("idProceso", idProceso))
		.sort(Sorts.ascending("fechaHoraConsulta"))
		.iterator();
	int cont = 0;
	try {
	    while (documentosProceso.hasNext()) {
		Document rendicionDocument = documentosProceso.next();
		ProcesoRendicion proceso = new ProcesoRendicion();
		proceso.setIdProceso(rendicionDocument.getInteger("idProceso"));
		proceso.setIdEmpresa(rendicionDocument.getString("idEmpresa"));
		proceso.setNombreEps(rendicionDocument.getString("nombreEps"));
		proceso.setCodEstado(rendicionDocument.getInteger("codEstado"));
		proceso.setEstadoProceso(rendicionDocument.getString("estadoProceso"));
		proceso.setFechaProceso(rendicionDocument.getDate("fechaProceso").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
		proceso.setFechaCreacion(rendicionDocument.getDate("fechaCreacion").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
		proceso.setInicioProceso(rendicionDocument.getDate("inicioProceso").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
		proceso.setFinProceso(rendicionDocument.getDate("finProceso").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
		proceso.setFechaIso(rendicionDocument.getString("fechaIso"));
		proceso.setIdDefinicionArchivos(rendicionDocument.getInteger("idDefinicionArchivos"));
		proceso.setTipoCorte(rendicionDocument.getInteger("tipoCorte"));
		proceso.setIdTarea(rendicionDocument.getInteger("idTarea"));
		proceso.setHoraEjecucion(rendicionDocument.getString("horaEjecucion"));
		proceso.setVigente(rendicionDocument.getString("vigente"));
		proceso.setFechaHoraConsulta(rendicionDocument.getDate("fechaHoraConsulta").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
		
		procesosRendicion.add(proceso);
		cont ++;
		System.out.println("Vuelta: " + cont);
	    }
	    return procesosRendicion;
	} catch (Exception ex) {
	    System.out.println("No se pudo obtener el listado de documentos de proceso de rendición relacionados al Id " + idProceso);
	    return new ArrayList();
	}
    }

    //Configuración de la base de datos
    private MongoCollection getCollection() {
	return mongoClient.getDatabase(DATABASE).getCollection(COLLECTION);
    }
}
