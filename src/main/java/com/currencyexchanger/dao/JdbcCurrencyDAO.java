package com.currencyexchanger.dao;

import com.currencyexchanger.exception.DatabaseException;
import com.currencyexchanger.model.CurrencyModel;
import com.currencyexchanger.utils.DBCPDataSourse;

import java.sql.*;
import java.util.*;

public class JdbcCurrencyDAO implements CurrencyDAO {

    @Override
    public CurrencyModel create(CurrencyModel cur) {
        final String query = "INSERT INTO currencies (code, fullname, sign) VALUES (?, ?, ?);";

        try (Connection connection = DBCPDataSourse.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, cur.getCode());
            ps.setString(2, cur.getFullName());
            ps.setString(3, cur.getSign());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            cur.setId(rs.getInt(1));

            return cur;

        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public List<CurrencyModel> readeAll() {
        final String query = "SELECT * FROM currencies;";
        LinkedList<CurrencyModel> list = new LinkedList<>();

        try (Connection connection = DBCPDataSourse.getConnection()) {
            PreparedStatement pr = connection.prepareStatement(query);
            ResultSet rs = pr.executeQuery();

            while (rs.next()) {
                list.add(getCurrency(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        return list;
    }

    @Override
    public Optional<CurrencyModel> readeById(int id) {
        final String query = "SELECT * FROM currencies WHERE id = ?";

        try (Connection connection = DBCPDataSourse.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return Optional.of(getCurrency(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        return Optional.empty();
    }

    @Override
    public Optional<CurrencyModel> readeByCode(String code) {
        final String query = "SELECT * FROM currencies WHERE Code = ?";

        try (Connection connection = DBCPDataSourse.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, code);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return Optional.of(getCurrency(rs));
            }

        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        return Optional.empty();
    }

    @Override
    public void update(CurrencyModel cur) {
    }

    @Override
    public void delete(int id) {
    }

    private static CurrencyModel getCurrency(ResultSet rs) {

        try {
            return new CurrencyModel(
                    rs.getInt("id"),
                    rs.getString("fullName"),
                    rs.getString("code"),
                    rs.getString("sign")
            );

        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}
