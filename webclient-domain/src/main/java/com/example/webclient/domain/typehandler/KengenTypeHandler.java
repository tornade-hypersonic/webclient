package com.example.webclient.domain.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import com.example.webclient.domain.enums.Kengen;

public class KengenTypeHandler extends BaseTypeHandler<Kengen> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Kengen parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.getValue());
    }

    @Override
    public Kengen getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return Kengen.fromValue(rs.getString(columnName));
    }

    @Override
    public Kengen getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return Kengen.fromValue(rs.getString(columnIndex));
    }

    @Override
    public Kengen getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return Kengen.fromValue(cs.getString(columnIndex));
    }
}