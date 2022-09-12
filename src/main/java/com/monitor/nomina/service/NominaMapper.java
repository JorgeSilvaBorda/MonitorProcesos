package com.monitor.nomina.service;

import com.monitor.model.ProcesoNomina;
import io.quarkiverse.mybatis.runtime.meta.MapperDataSource;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
@MapperDataSource("nominas")
public interface NominaMapper {

    @Select("SELECT \n"
	    + "	a.id_empresa idEmpresa,\n"
	    + "	a.cod_empresa codEmpresa,\n"
	    + "	a.glosa_empresa nomEmpresa,\n"
	    + "	a.per_inicio horaIni,\n"
	    + "	a.per_final horaFin,\n"
	    + "	convert(varchar(8), convert(TIME, getdate())) horaActual,\n"
	    + "	DATEDIFF(MINUTE, CONVERT(TIME, a.per_final), CONVERT(TIME, getdate())) minutosALaHora,\n"
	    + "	b.fecha_proceso fechaProceso,\n"
	    + "	b.fecha_termino fechaTermino,\n"
	    + "	datediff(MINUTE, b.fecha_proceso, b.fecha_termino) minutos,\n"
	    + "	b.id_estado idEstado,\n"
	    + "	c.glosa_estado estado,\n"
	    + "	convert(date, getdate()) fechaCarga,\n"
	    + "	getDate() fechaHoraCarga\n"
	    + "FROM 	\n"
	    + "	tbl_EmpUnimarc a LEFT JOIN tbl_LogUnimarc b \n"
	    + "	ON a.id_empresa  = b.id_empresa \n"
	    + "		and convert(date, b.fecha_proceso) = convert(date, getdate()) LEFT JOIN tbl_EstaUnimarc c\n"
	    + "	ON b.id_estado = c.id_estado \n"
	    + "WHERE \n"
	    + "	a.estado = 2 \n"
	    + "		or \n"
	    + "	(DATEDIFF(MINUTE, CONVERT(TIME, a.per_final), CONVERT(TIME, getdate())) > 0 and b.id_estado is null)")
    List<ProcesoNomina> getEstadosNominas();
}
