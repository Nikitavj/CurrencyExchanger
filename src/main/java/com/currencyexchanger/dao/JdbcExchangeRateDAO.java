package com.currencyexchanger.dao;

import com.currencyexchanger.DTO.ReqCurrencyDTO;
import com.currencyexchanger.DTO.ReqExchangeRateDTO;
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
    public Optional<ExchangeRateModel> create(ReqExchangeRateDTO req) throws SQLException {
        final String query = "INSERT INTO exchangerates (BaseCurrencyId, TargetCurrencyId, rate) VALUES (?, ?, ?);";

        JdbcCurrencyDAO currencyDAO = new JdbcCurrencyDAO();
        CurrencyModel baseCurrency = currencyDAO.readeByCode(new ReqCurrencyDTO(req.getBaseCurrencyCode())).get();
        CurrencyModel targetCurrency = currencyDAO.readeByCode(new ReqCurrencyDTO(req.getTargetCurrncyCode())).get();

        try (Connection connection = DBCPDataSourse.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, baseCurrency.getId());
            ps.setInt(2, targetCurrency.getId());
            ps.setBigDecimal(3, req.getRate());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                return Optional.of(getExchangeRate(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<ExchangeRateModel> readeAll() throws SQLException {
        final String query = "SELECT * FROM exchangerates;";
        ArrayList<ExchangeRateModel> list = new ArrayList<>();

        try (Connection connection = DBCPDataSourse.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(getExchangeRate(rs));
            }
        }
        return list;
    }

    @Override
    public Optional<ExchangeRateModel> readeById(ReqExchangeRateDTO req) throws SQLException {
        return Optional.empty();
    }

    @Override
    public Optional<ExchangeRateModel> readeByCodes(ReqExchangeRateDTO req) throws SQLException {
        final String query = "SELECT * FROM exchangerates WHERE BaseCurrencyId = ? AND TargetCurrencyId = ?";

        JdbcCurrencyDAO currencyDAO = new JdbcCurrencyDAO();
        CurrencyModel baseCurrency = currencyDAO.readeByCode(new ReqCurrencyDTO(req.getBaseCurrencyCode())).get();
        CurrencyModel targetCurrency = currencyDAO.readeByCode(new ReqCurrencyDTO(req.getTargetCurrncyCode())).get();

        try (Connection connection = DBCPDataSourse.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, baseCurrency.getId());
            ps.setInt(1, targetCurrency.getId());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return Optional.of(getExchangeRate(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<ExchangeRateModel> update(ReqExchangeRateDTO req) throws SQLException {
        return Optional.empty();
    }

    @Override
    public Optional<ExchangeRateModel> delete(ReqExchangeRateDTO req) {
        return Optional.empty();
    }

    private static ExchangeRateModel getExchangeRate(ResultSet rs) throws SQLException {
        JdbcCurrencyDAO currencyDAO = new JdbcCurrencyDAO();

        ReqCurrencyDTO reqBaseCurr = new ReqCurrencyDTO(rs.getInt("BaseCurrencyId"));
        ReqCurrencyDTO reqTargetCurr = new ReqCurrencyDTO(rs.getInt("TargetCurrencyId"));

        Optional<CurrencyModel> baseCurrency = currencyDAO.readeById(reqBaseCurr);
        Optional<CurrencyModel> targetCurrency = currencyDAO.readeById(reqTargetCurr);

        return new ExchangeRateModel(
                rs.getInt("id"),
                baseCurrency.get(),
                targetCurrency.get(),
                rs.getBigDecimal("rate")
        );
    }
}

