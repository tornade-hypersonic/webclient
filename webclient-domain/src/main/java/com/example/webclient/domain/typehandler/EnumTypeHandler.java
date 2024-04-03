package com.example.webclient.domain.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

public class EnumTypeHandler<E extends Enum<E>> extends BaseTypeHandler<E> {

    private Class<E> type;

    public EnumTypeHandler(Class<E> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.type = type;
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, E e, JdbcType jdbcType) throws SQLException {
        if (jdbcType == null) {
            preparedStatement.setString(i, e.name());
        } else {
            preparedStatement.setObject(i, e.name(), jdbcType.TYPE_CODE);
        }
    }

    @Override
    public E getNullableResult(ResultSet resultSet, String s) throws SQLException {
        String result = resultSet.getString(s);
        if (resultSet.wasNull()) {
            return null;
        } else {
//            return Enum.valueOf(type, result);
        	return convertToEnum(result);
        }
    }

    @Override
    public E getNullableResult(ResultSet resultSet, int i) throws SQLException {
        String result = resultSet.getString(i);
        if (resultSet.wasNull()) {
            return null;
        } else {
//          return Enum.valueOf(type, result);
      	return convertToEnum(result);
        }
    }

    @Override
    public E getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        String result = callableStatement.getString(i);
        if (callableStatement.wasNull()) {
            return null;
        } else {
            return Enum.valueOf(type, result);
        }
    }
    
    
    private E convertToEnum(String value) {
        if (value == null) {
            return null;
        }
        for (E enumConstant : type.getEnumConstants()) {
        	if (String.valueOf(((Enum<?>) enumConstant).ordinal()).equals(value) || ((Enum<?>) enumConstant).name().equals(value)) {
                return enumConstant;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }    
}