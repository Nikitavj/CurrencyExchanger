package com.currencyexchanger.controller.servlets;

import com.currencyexchanger.DTO.RequestExchangeRateDTO;
import com.currencyexchanger.controller.Validator;
import com.currencyexchanger.controller.error.ErrorDTO;
import com.currencyexchanger.controller.exception.*;
import com.currencyexchanger.model.ExchangeRateModel;
import com.currencyexchanger.repository.JDBCRepsitory;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.file.FileAlreadyExistsException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "exchangeRates", value = "/exchangeRates")
public class ExchangeRatesServlet extends BaseServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        List<ExchangeRateModel> list = null;
        PrintWriter printWriter = response.getWriter();

        try {

            list = JDBCRepsitory.readExchangeRates();
            String json = objectMapper.writeValueAsString(list);
            printWriter.println(json);
        } catch (NotFoundExchangeRateException e) {
            response.setStatus(response.SC_NOT_FOUND);
            printWriter.println(objectMapper.writeValueAsString(new ErrorDTO(e.getMessage())));
        } catch (SQLException e) {
            response.setStatus(response.SC_INTERNAL_SERVER_ERROR);
            printWriter.println(objectMapper.writeValueAsString(new ErrorDTO(e.getMessage())));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        ExchangeRateModel exchangeRateModel = null;
        PrintWriter printWriter = response.getWriter();

        String baseCurrencyCode = request.getParameter("baseCurrencyCode");
        String targetCurrencyCode = request.getParameter("targetCurrencyCode");
        String stringRate = request.getParameter("rate");

        try {
            Validator.validateRatesParameter(baseCurrencyCode, targetCurrencyCode, stringRate);
            Validator.validateRateCode(baseCurrencyCode + targetCurrencyCode);
            BigDecimal rate = new BigDecimal(stringRate);
            RequestExchangeRateDTO requestDTO = new RequestExchangeRateDTO(baseCurrencyCode, targetCurrencyCode, rate);
            exchangeRateModel = JDBCRepsitory.createExchangeRate(requestDTO);
            String json = objectMapper.writeValueAsString(exchangeRateModel);
            printWriter.println(json);

        } catch (FileAlreadyExistsException e) {
            response.setStatus(response.SC_CONFLICT);
            printWriter.println(objectMapper.writeValueAsString(new ErrorDTO(e.getMessage())));
        } catch (InvalidCurrencyCodeException e) {
            response.setStatus(response.SC_BAD_REQUEST);
            printWriter.println(objectMapper.writeValueAsString(new ErrorDTO(e.getMessage())));
        } catch (InvalidRateCodeException e) {
            response.setStatus(response.SC_BAD_REQUEST);
            printWriter.println(objectMapper.writeValueAsString(new ErrorDTO(e.getMessage())));
        } catch (InvalidCurrencyRatesParameters e) {
            response.setStatus(response.SC_BAD_REQUEST);
            printWriter.println(objectMapper.writeValueAsString(new ErrorDTO(e.getMessage())));
        } catch (NotFoundCurrencyException e) {
            response.setStatus(response.SC_BAD_REQUEST);
            printWriter.println(objectMapper.writeValueAsString(new ErrorDTO(e.getMessage())));
        }
    }
}
