package com.monitor.extract.service;

import com.monitor.model.ProcesoExtract;
import io.quarkiverse.mybatis.runtime.meta.MapperDataSource;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
@MapperDataSource("extract")
public interface ExtractMapper {

    @Select(""
	    + "SELECT \n"
	    + "	A.IdLogSistema IDLOGSISTEMA,\n"
	    + "	A.Mensaje MENSAJE,\n"
	    + "	A.IdEmpresa IDEMPRESA,\n"
	    + "	C.NombreEps NOMBREEPS,\n"
	    + "	CONVERT(DATE, A.FechaCreacion) FECHACREACION,\n"
	    + "	A.FechaCreacion FECHAHORACREACION,\n"
	    + "	A.TipoLog_IdTipo IDTIPOLOG,\n"
	    + "	B.Descripcion DESCRIPCION\n"
	    + "FROM 	\n"
	    + "	LogEventosSistema A INNER JOIN TipoLog B \n"
	    + "	ON A.TipoLog_IdTipo = B.IdTipo INNER JOIN EmpresaProceso C\n"
	    + "	ON A.IdEmpresa = C.IdEmpresa \n"
	    + "WHERE \n"
	    + "	A.IdEmpresa = 'EXTRAC'\n"
	    + "	AND CONVERT(DATE, A.FechaCreacion) = CONVERT(DATE, GETDATE())\n"
	    + "	AND A.FechaCreacion <= CONVERT(DATETIME, (CONVERT(VARCHAR, CONVERT(DATE, GETDATE())) + ' 02:25:59.999'))\n"
	    + "	AND A.FechaCreacion >= CONVERT(DATETIME, (CONVERT(VARCHAR, CONVERT(DATE, GETDATE())) + ' 02:20:00.000'))\n"
	    + "ORDER BY \n"
	    + "	A.FechaCreacion ASC")
    public List<ProcesoExtract> getExtractHoy();

}