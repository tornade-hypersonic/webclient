package com.example.webclient.domain.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import com.example.webclient.domain.enums.EnumBase;


public class EnumValueTypeHandler<E extends Enum<E>> extends BaseTypeHandler<E>{
   private Class<EnumBase<E>> cls;
   @SuppressWarnings("unchecked")
   public EnumValueTypeHandler(Class<E> cls) {
      this.cls = (Class<EnumBase<E>>) cls;
   }

   @SuppressWarnings("unchecked")
   @Override
   public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException{
      if (parameter==null) {
         ps.setObject(i, null);
      }else{
         ps.setObject(i, ((EnumBase<E>)parameter).getValue());
      }
   }

   @Override
   public E getNullableResult(ResultSet rs, String columnName) throws SQLException{
//      return EnumBase.parseCode(cls, rs.getInt(columnName)).orElse(null);
	      return EnumBase.parseCode(cls, rs.getString(columnName)).orElse(null);
   }
   @Override
   public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException{
      return EnumBase.parseCode(cls, rs.getInt(columnIndex)).orElse(null);
   }
   @Override
   public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException{
      return EnumBase.parseCode(cls, cs.getInt(columnIndex)).orElse(null);
   }
}
