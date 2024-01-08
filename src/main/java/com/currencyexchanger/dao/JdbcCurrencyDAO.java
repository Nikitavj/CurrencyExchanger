package com.currencyexchanger.dao;

import com.currencyexchanger.DTO.ReqCurrencyDTO;
import com.currencyexchanger.model.CurrencyModel;
import com.currencyexchanger.utils.DBCPDataSourse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class JdbcCurrencyDAO implements CurrencyDAO {

    @Override
    public Optional<CurrencyModel> create(ReqCurrencyDTO req) throws SQLException {
        final String query = "INSERT INTO currencies (code, fullname, sign) VALUES (?, ?, ?);";

        try (Connection connection = DBCPDataSourse.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, req.getCode());
            ps.setString(2, req.getName());
            ps.setString(3, req.getSign());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                return Optional.of(getCurrency(rs));
            }
            return Optional.empty();
        }
    }

    @Override
    public List<CurrencyModel> readeAll() throws SQLException {
        final String query = "SELECT * FROM currencies;";
        ArrayList<CurrencyModel> list = new ArrayList<>();

        try (Connection connection = DBCPDataSourse.getConnection()) {
            PreparedStatement pr = connection.prepareStatement(query);
            ResultSet rs = pr.executeQuery();

            while(rs.next()) {
                list.add(getCurrency(rs));
            }
        }
        return list;
    }

    @Override
    public Optional<CurrencyModel> readeById(ReqCurrencyDTO req) throws SQLException {
        final String query = "SELECT * FROM currencies WHERE id = ?";

        try (Connection connection = DBCPDataSourse.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, req.getId());
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                return Optional.of(getCurrency(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<CurrencyModel> readeByCode(ReqCurrencyDTO req) throws SQLException {
        final String query = "SELECT * FROM currencies WHERE Code = ?";

        try (Connection connection = DBCPDataSourse.getConnection()){
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, req.getCode());
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                return Optional.of(getCurrency(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<CurrencyModel> update(ReqCurrencyDTO req) throws SQLException {
        return Optional.empty();
    }

    @Override
    public Optional<CurrencyModel> delete(ReqCurrencyDTO req) {
        return Optional.empty();
    }

    private static CurrencyModel getCurrency(ResultSet rs) throws SQLException {
        return new CurrencyModel(
                rs.getInt("id"),
                rs.getString("fullName"),
                rs.getString("code"),
                rs.getString("sign")
        );
    }
}
