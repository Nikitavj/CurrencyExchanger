package com.currencyexchanger.controller;

import com.currencyexchanger.DTO.ErrorDTO;
import com.currencyexchanger.DTO.ReqExchangeRateDTO;
import com.currencyexchanger.dao.JdbcExchangeRateDAO;
import com.currencyexchanger.exception.*;
import com.currencyexchanger.model.ExchangeRateModel;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.FileAlreadyExistsException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "exchangeRates", value = "/exchangeRates")
public class ExchangeRatesServlet extends BaseServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JdbcExchangeRateDAO dao = new JdbcExchangeRateDAO();

        try {
            List<ExchangeRateModel> list = dao.readeAll();
            pWriter.println(objMapper.writeValueAsString(list));

        } catch (SQLException e) {
            response.setStatus(response.SC_INTERNAL_SERVER_ERROR);
            pWriter.println(objMapper.writeValueAsString(new ErrorDTO(e.getMessage())));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JdbcExchangeRateDAO dao = new JdbcExchangeRateDAO();
        String baseCurrencyCode = request.getParameter("baseCurrencyCode");
        String targetCurrencyCode = request.getParameter("targetCurrencyCode");
        String stringRate = request.getParameter("rate");

        try {
            Validator.validateParameters(baseCurrencyCode, targetCurrencyCode, stringRate);
            Validator.validateRateCode(baseCurrencyCode + targetCurrencyCode);

            BigDecimal rate = new BigDecimal(stringRate);
            ReqExchangeRateDTO req = new ReqExchangeRateDTO(baseCurrencyCode, targetCurrencyCode, rate);

            Optional<ExchangeRateModel> exchangeRate = dao.create(req);

            if (exchangeRate.isEmpty()) {
                throw new FileAlreadyExistsException("Обменный курс уже существует в БД!");
            }

            pWriter.println(objMapper.writeValueAsString(exchangeRate.get()));

        } catch (InvalidCurrencyCodeException | InvalidRateCodeException
                 | InvalidParametersException e) {
            response.setStatus(response.SC_BAD_REQUEST);
            pWriter.println(objMapper.writeValueAsString(new ErrorDTO(e.getMessage())));

        } catch (FileAlreadyExistsException e) {
            response.setStatus(response.SC_CONFLICT);
            pWriter.println(objMapper.writeValueAsString(new ErrorDTO(e.getMessage())));

        } catch (SQLException e) {
            response.setStatus(response.SC_INTERNAL_SERVER_ERROR);
            pWriter.println(objMapper.writeValueAsString(new ErrorDTO(e.getMessage())));
        }
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        pWriter = resp.getWriter();
        super.service(req, resp);
    }
}
