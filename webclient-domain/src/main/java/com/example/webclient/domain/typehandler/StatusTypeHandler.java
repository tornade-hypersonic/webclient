package com.example.webclient.domain.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import com.example.webclient.domain.enums.Status;

public class StatusTypeHandler extends BaseTypeHandler<Status> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Status parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.getValue());
    }

    @Override
    public Status getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return Status.fromValue(rs.getString(columnName));
    }

    @Override
    public Status getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return Status.fromValue(rs.getString(columnIndex));
    }

    @Override
    public Status getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return Status.fromValue(cs.getString(columnIndex));
    }
}