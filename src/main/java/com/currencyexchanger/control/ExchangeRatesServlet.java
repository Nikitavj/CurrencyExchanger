package com.currencyexchanger.control;

import com.currencyexchanger.control.error.ErrorDTO;
import com.currencyexchanger.control.error.ErrorMessages;
import com.currencyexchanger.control.exception.InvalidCurrencyRatesParameters;
import com.currencyexchanger.control.exception.InvalidRateCodeException;
import com.currencyexchanger.model.ExchangeRate;
import com.currencyexchanger.repository.Create;
import com.currencyexchanger.repository.Read;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "exchangeRates", value = "/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        List<ExchangeRate> list = null;

        PrintWriter printWriter = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            list = Read.getExchangeRates();
            String json = objectMapper.writeValueAsString(list);
            printWriter.println(json);
        } catch (SQLException e) {
            response.setStatus(500);
            ErrorDTO errorDTO = new ErrorDTO(ErrorMessages.ERROR_DATABASE.getMessage());
            printWriter.println(objectMapper.writeValueAsString(errorDTO));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        ExchangeRate exchangeRate = null;
        PrintWriter printWriter = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();

        String baseCurrencyCode = request.getParameter("baseCurrencyCode");
        String targetCurrencyCode = request.getParameter("targetCurrencyCode");
        String stringRate = request.getParameter("rate");

        try {
            ValidateRequest.validateRatesParameter(baseCurrencyCode,targetCurrencyCode, stringRate);
            ValidateRequest.validateRateCode(baseCurrencyCode + targetCurrencyCode);
            double rate = Double.parseDouble(stringRate);
            exchangeRate = Create.addExchangeRate(baseCurrencyCode, targetCurrencyCode, rate);
            String json = objectMapper.writeValueAsString(exchangeRate);
            printWriter.println(json);

        } catch (InvalidCurrencyRatesParameters e) {
            response.setStatus(409);
            ErrorDTO error = new ErrorDTO(e.getMessage());
            String jsonError = objectMapper.writeValueAsString(error);
            printWriter.println(jsonError);

        } catch (InvalidRateCodeException e) {
            response.setStatus(409);
            ErrorDTO error = new ErrorDTO(e.getMessage());
            String jsonError = objectMapper.writeValueAsString(error);
            printWriter.println(jsonError);

        } catch (SQLException e) {
            printWriter.println(e.getMessage());
            printWriter.println(e.getErrorCode());
            if (e.getErrorCode() == 1062) {
                response.setStatus(404);
                ErrorDTO error = new ErrorDTO(ErrorMessages.EXCHANGE_RATES_POST_409.getMessage());
                String jsonError = objectMapper.writeValueAsString(error);
                printWriter.println(jsonError);
            }
        }
    }
}
