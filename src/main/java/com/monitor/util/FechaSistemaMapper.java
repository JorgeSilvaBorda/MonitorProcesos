package com.monitor.util;

import com.monitor.model.FechaSistema;
import io.quarkiverse.mybatis.runtime.meta.MapperDataSource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
@MapperDataSource("nominas")
public interface FechaSistemaMapper {
    
    @Select("SELECT CONVERT(DATE, GETDATE()) fecha, GETDATE() fechaHora, CONVERT(TIME, GETDATE()) hora")
    FechaSistema getFechaSistema();
}
