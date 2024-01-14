package com.currencyexchanger.controller;

import com.currencyexchanger.DTO.ErrorDTO;
import com.currencyexchanger.dao.JdbcCurrencyDAO;
import com.currencyexchanger.dao.JdbcExchangeRateDAO;
import com.currencyexchanger.exception.*;
import com.currencyexchanger.model.CurrencyModel;
import com.currencyexchanger.model.ExchangeRateModel;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "exchangeRates", value = "/exchangeRates")
public class ExchangeRatesServlet extends BaseServlet {
    private final int ERROR_CODE_SQLITE_CONSTRAINT = 19;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JdbcExchangeRateDAO dao = new JdbcExchangeRateDAO();

        try {
            List<ExchangeRateModel> list = dao.readeAll();
            objMapper.writeValue(pWriter, list);

        } catch (DatabaseException e) {
            response.setStatus(response.SC_INTERNAL_SERVER_ERROR);
            pWriter.println(
                    objMapper.writeValueAsString(
                            new ErrorDTO("База данных недоступна!")));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JdbcExchangeRateDAO rateDAO = new JdbcExchangeRateDAO();
        JdbcCurrencyDAO currDAO = new JdbcCurrencyDAO();

        String baseCurrCode = request.getParameter("baseCurrencyCode");
        String targetCurrCode = request.getParameter("targetCurrencyCode");
        String stringRate = request.getParameter("rate");
        List<String> parameters = Arrays.asList(
                baseCurrCode,
                targetCurrCode,
                stringRate
        );

        try {
            Validator.validateParameters(parameters);
            Validator.validateRateCode(baseCurrCode + targetCurrCode);

            Optional<CurrencyModel> baseCurr = currDAO.readeByCode(baseCurrCode);
            Optional<CurrencyModel> targetCurr = currDAO.readeByCode(targetCurrCode);

            if (baseCurr.isEmpty() || targetCurr.isEmpty()) {
                response.setStatus(response.SC_NOT_FOUND);
                objMapper.writeValue(
                        pWriter,
                        new ErrorDTO("Одна или обе валюты отсутсвуют в БД!"));
                return;
            }

            ExchangeRateModel model = new ExchangeRateModel();
            model.setRate(new BigDecimal(stringRate));
            model.setBaseCurrency(baseCurr.get());
            model.setTargetCurrency(targetCurr.get());

            ExchangeRateModel exchangeRate = rateDAO.create(model);

            objMapper.writeValue(pWriter, exchangeRate);

        } catch (InvalidCurrencyCodeException | InvalidRateCodeException
                 | InvalidParametersException e) {
            response.setStatus(response.SC_BAD_REQUEST);
            objMapper.writeValue(
                    pWriter,
                    new ErrorDTO(e.getMessage())
            );

        } catch (DatabaseException e) {
            SQLException t = (SQLException) e.getCause();

            if (t.getErrorCode() == ERROR_CODE_SQLITE_CONSTRAINT) {
                response.setStatus(response.SC_CONFLICT);
                objMapper.writeValue(
                        pWriter,
                        new ErrorDTO("Обменный курс уже существует в БД!")
                );

            } else {
                response.setStatus(response.SC_INTERNAL_SERVER_ERROR);
                objMapper.writeValue(
                        pWriter,
                        new ErrorDTO("База данных недоступна!")
                );
            }
        }
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        pWriter = resp.getWriter();
        super.service(req, resp);
    }
}
