package com.monitor.rendicion.service;

import com.monitor.model.ProcesoRendicion;
import io.quarkiverse.mybatis.runtime.meta.MapperDataSource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
@MapperDataSource("rendiciones")
public interface ProcesoRendicionMapper {

    @Select("SELECT\n"
	    + "	a.IdPRoceso idProceso,\n"
	    + "	a.IdEmpresa idEmpresa,\n"
	    + "	c.NombreEps nombreEps,\n"
	    + "	a.EstadoProceso codEstado,\n"
	    + "	case\n"
	    + "		when a.EstadoProceso = 56 then 'Pendiente'\n"
	    + "		when a.EstadoProceso = 26 then 'En Ejecución'\n"
	    + "		when a.EstadoProceso = 27 then 'Generado'\n"
	    + "		when a.EstadoProceso = 28 then 'Transmitido'\n"
	    + "		when a.EstadoProceso = 29 then 'Falla en Proceso'\n"
	    + "		when a.EstadoProceso = 40 then 'Enviado a Mail'\n"
	    + "		when a.EstadoProceso = 4046 then 'Rendición Vacía'\n"
	    + "	end as estadoProceso,\n"
	    + "	a.FechaProceso fechaProceso,\n"
	    + "	a.FechaCreacion fechaCreacion,\n"
	    + "	a.InicioProceso inicioProceso,\n"
	    + "	a.FinProceso finProceso,\n"
	    + "	a.FechaIso fechaIso,\n"
	    + "	b.IdDefinicionArchivos idDefinicionArchivos,\n"
	    + "	d.TipoCorte tipoCorte,\n"
	    + "	e.IdTarea idTarea,\n"
	    + "	e.HoraEjecucion horaEjecucion,\n"
	    + "	e.Vigente vigente,\n"
	    + " getdate() fechaHoraConsulta "
	    + "from \n"
	    + "	Procesos a inner join DefinicionArchivos b\n"
	    + "	on a.DefinicionArchivos_IdDefinicionArchivos = b.IdDefinicionArchivos inner join Empresas c\n"
	    + "	on a.IdEmpresa = c.IdEmpresa inner join PlanRendiciones d\n"
	    + "	on a.IdEmpresa = d.IdEmpresa inner join TareasProgramadas e\n"
	    + "	on e.PlanRendiciones_IdEmpresa = d.IdEmpresa "
	    + "where"
	    + " a.IdProceso = #{idProceso}")
    ProcesoRendicion getProcesoProgramadoIdProceso(Integer idProceso);
    
    @Select("SELECT\n"
	    + "	a.IdPRoceso idProceso,\n"
	    + "	a.IdEmpresa idEmpresa,\n"
	    + "	c.NombreEps nombreEps,\n"
	    + "	a.EstadoProceso codEstado,\n"
	    + "	case\n"
	    + "		when a.EstadoProceso = 56 then 'Pendiente'\n"
	    + "		when a.EstadoProceso = 26 then 'En Ejecución'\n"
	    + "		when a.EstadoProceso = 27 then 'Generado'\n"
	    + "		when a.EstadoProceso = 28 then 'Transmitido'\n"
	    + "		when a.EstadoProceso = 29 then 'Falla en Proceso'\n"
	    + "		when a.EstadoProceso = 40 then 'Enviado a Mail'\n"
	    + "		when a.EstadoProceso = 4046 then 'Rendición Vacía'\n"
	    + "	end as estadoProceso,\n"
	    + "	a.FechaProceso fechaProceso,\n"
	    + "	a.FechaCreacion fechaCreacion,\n"
	    + "	a.InicioProceso inicioProceso,\n"
	    + "	a.FinProceso finProceso,\n"
	    + "	a.FechaIso fechaIso,\n"
	    + "	b.IdDefinicionArchivos idDefinicionArchivos,\n"
	    + "	d.TipoCorte tipoCorte,\n"
	    + "	e.IdTarea idTarea,\n"
	    + "	e.HoraEjecucion horaEjecucion,\n"
	    + "	e.Vigente vigente,\n"
	    + " null fechaHoraConsulta " //Acá se marcó como null para no tener diferencias de fecha. La fecha será puesta por el programa que inserta
	    + "from \n"
	    + "	Procesos a inner join DefinicionArchivos b\n"
	    + "	on a.DefinicionArchivos_IdDefinicionArchivos = b.IdDefinicionArchivos inner join Empresas c\n"
	    + "	on a.IdEmpresa = c.IdEmpresa inner join PlanRendiciones d\n"
	    + "	on a.IdEmpresa = d.IdEmpresa inner join TareasProgramadas e\n"
	    + "	on e.PlanRendiciones_IdEmpresa = d.IdEmpresa "
	    + "where"
	    + " a.EstadoProceso = 26")
    ProcesoRendicion buscarEnEjecucion();
}
