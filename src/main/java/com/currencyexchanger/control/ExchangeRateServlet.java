package com.currencyexchanger.control;

import com.currencyexchanger.control.error.ErrorDTO;
import com.currencyexchanger.control.error.ErrorMessages;
import com.currencyexchanger.control.exception.InvalidRateCodeException;
import com.currencyexchanger.control.exception.NotFoundExchangeRateException;
import com.currencyexchanger.model.ExchangeRate;
import com.currencyexchanger.repository.Read;
import com.currencyexchanger.repository.Update;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet(name = "exchangeRate", value = "/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter printWriter = response.getWriter();

        String path = request.getPathInfo();
        String[] codes = path.split("/");
        String rateCode = codes[1];

        ExchangeRate exchangeRate = null;
        try {
            ValidateRequest.validateRateCode(rateCode);
            String baseCurrensyCode = rateCode.substring(0, 3);
            String targetCurrensyCode = rateCode.substring(3, 6);
            exchangeRate = Read.getExchangeRate(baseCurrensyCode, targetCurrensyCode);
            String jsonRate = objectMapper.writeValueAsString(exchangeRate);
            printWriter.println(jsonRate);
        } catch (InvalidRateCodeException e) {
            response.setStatus(400);
            ErrorDTO errorDTO = new ErrorDTO(e.getMessage());
            printWriter.println(objectMapper.writeValueAsString(errorDTO));
        } catch (NotFoundExchangeRateException e) {
            response.setStatus(404);
            ErrorDTO errorDTO = new ErrorDTO(e.getMessage());
            printWriter.println(objectMapper.writeValueAsString(errorDTO));
        } catch (SQLException e) {
            response.setStatus(500);
            ErrorDTO errorDTO = new ErrorDTO(ErrorMessages.ERROR_DATABASE.getMessage());
            printWriter.println(objectMapper.writeValueAsString(errorDTO));
        }
    }

    protected void doPatch(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        ExchangeRate exchangeRate = null;
        PrintWriter printWriter = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();

        String path = request.getPathInfo();
        String[] codes = path.split("/");
        String rateCode = codes[1];
        String rateParameter = request.getParameter("rate");
        if (rateParameter == null || rateParameter.equals("")) {
            response.setStatus(400);
            ErrorDTO error = new ErrorDTO(ErrorMessages.CURRENCIES_PATCH_400.getMessage());
            String jsonError = objectMapper.writeValueAsString(error);
            printWriter.println(jsonError);
            return;
        }

        try {
            ValidateRequest.validateRateCode(rateCode);
            Double rate = Double.parseDouble(rateParameter);
            String baseCurrensyCode = rateCode.substring(0, 3);
            String targetCurrensyCode = rateCode.substring(3, 6);
            exchangeRate = Update.updateExchangeRate(baseCurrensyCode, targetCurrensyCode, rate);
            String jsonRate = objectMapper.writeValueAsString(exchangeRate);
            printWriter.println(jsonRate);
        } catch (InvalidRateCodeException e) {
            response.setStatus(400);
            ErrorDTO error = new ErrorDTO(e.getMessage());
            String jsonError = objectMapper.writeValueAsString(error);
            printWriter.println(jsonError);
        } catch (NotFoundExchangeRateException e) {
            response.setStatus(404);
            ErrorDTO error = new ErrorDTO(e.getMessage());
            String jsonError = objectMapper.writeValueAsString(error);
            printWriter.println(jsonError);
        } catch (SQLException e) {
            response.setStatus(500);
            ErrorDTO errorDTO = new ErrorDTO(ErrorMessages.ERROR_DATABASE.getMessage());
            printWriter.println(objectMapper.writeValueAsString(errorDTO));
        }
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getMethod();
        if (!method.equals("PATCH")) {
            super.service(request, response);
            return;
        }
        this.doPatch(request, response);
    }
}
