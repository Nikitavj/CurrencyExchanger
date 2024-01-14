package com.currencyexchanger.dao;

import com.currencyexchanger.exception.DatabaseException;
import com.currencyexchanger.model.CurrencyModel;
import com.currencyexchanger.model.ExchangeRateModel;
import com.currencyexchanger.utils.DBCPDataSourse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcExchangeRateDAO implements ExchangeRateDAO {

    @Override
    public ExchangeRateModel create(ExchangeRateModel model) {
        final String query = "INSERT INTO exchangerates (BaseCurrencyId, TargetCurrencyId, rate) VALUES (?, ?, ?);";
        JdbcCurrencyDAO currencyDAO = new JdbcCurrencyDAO();

        try (Connection connection = DBCPDataSourse.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, model.getBaseCurrency().getId());
            ps.setInt(2, model.getTargetCurrency().getId());
            ps.setBigDecimal(3, model.getRate());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            model.setId(rs.getInt(1));

            return model;

        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public List<ExchangeRateModel> readeAll() {
        final String query = "SELECT * FROM exchangerates;";
        ArrayList<ExchangeRateModel> list = new ArrayList<>();

        try (Connection connection = DBCPDataSourse.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(getExchangeRate(rs));
            }

        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        return list;
    }

    @Override
    public Optional<ExchangeRateModel> readeById(int id) {
        return Optional.empty();
    }

    @Override
    public Optional<ExchangeRateModel> readeByCodes(String baseCode, String targetCode) {
        final String query = "SELECT * FROM exchangerates WHERE BaseCurrencyId = ? AND TargetCurrencyId = ?";
        JdbcCurrencyDAO currencyDAO = new JdbcCurrencyDAO();

        Optional<CurrencyModel> baseCurr = currencyDAO.readeByCode(baseCode);
        Optional<CurrencyModel> targetCurr = currencyDAO.readeByCode(targetCode);

        if (baseCurr.isPresent() && targetCurr.isPresent()) {

            try (Connection connection = DBCPDataSourse.getConnection()) {
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setInt(1, baseCurr.get().getId());
                ps.setInt(2, targetCurr.get().getId());
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    return Optional.of(getExchangeRate(rs));
                }

            } catch (SQLException e) {
                throw new DatabaseException(e);
            }
        }

        return Optional.empty();
    }

    @Override
    public void update(ExchangeRateModel rate) {
        final String query = "UPDATE exchangerates SET Rate = ? WHERE BaseCurrencyId = ? AND TargetCurrencyId = ?";

        try (Connection connection = DBCPDataSourse.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setBigDecimal(1, rate.getRate());
            ps.setInt(2, rate.getBaseCurrency().getId());
            ps.setInt(3, rate.getTargetCurrency().getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void delete(int id) {
    }

    private static ExchangeRateModel getExchangeRate(ResultSet rs) {
        JdbcCurrencyDAO dao = new JdbcCurrencyDAO();

        try {
            Optional<CurrencyModel> baseCurrency = dao.readeById(
                    rs.getInt("BaseCurrencyId")
            );
            Optional<CurrencyModel> targetCurrency = dao.readeById(
                    rs.getInt("TargetCurrencyId")
            );

            return new ExchangeRateModel(
                    rs.getInt("id"),
                    baseCurrency.get(),
                    targetCurrency.get(),
                    rs.getBigDecimal("rate")
            );

        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}

