package com.currencyexchanger.controller.servlets;

import com.currencyexchanger.DTO.ErrorDTO;
import com.currencyexchanger.DTO.RequestExchangeRateDTO;
import com.currencyexchanger.controller.Validator;
import com.currencyexchanger.controller.exception.InvalidCurrencyCodeException;
import com.currencyexchanger.controller.exception.InvalidRateCodeException;
import com.currencyexchanger.controller.exception.NotFoundExchangeRateException;
import com.currencyexchanger.model.ExchangeRateModel;
import com.currencyexchanger.repository.JDBCRepsitory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

@WebServlet(name = "exchangeRate", value = "/exchangeRate/*")
public class ExchangeRateServlet extends BaseServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String rateCode = request.getPathInfo().replaceAll("/", "");

        try {
            Validator.validateRateCode(rateCode);

            String baseCurrensyCode = rateCode.substring(0, 3);
            String targetCurrensyCode = rateCode.substring(3, 6);
            RequestExchangeRateDTO requestExchangeRateDTO = new RequestExchangeRateDTO(baseCurrensyCode, targetCurrensyCode);

            ExchangeRateModel exchangeRateModel = JDBCRepsitory.readExchangeRate(requestExchangeRateDTO);
            printWriter.println(objectMapper.writeValueAsString(exchangeRateModel));

        } catch (InvalidRateCodeException e) {
            response.setStatus(response.SC_BAD_REQUEST);
            printWriter.println(objectMapper.writeValueAsString(new ErrorDTO(e.getMessage())));

        } catch (NotFoundExchangeRateException e) {
            response.setStatus(response.SC_NOT_FOUND);
            printWriter.println(objectMapper.writeValueAsString(new ErrorDTO(e.getMessage())));

        } catch (SQLException e) {
            response.setStatus(500);
            printWriter.println(objectMapper.writeValueAsString(new ErrorDTO(e.getMessage())));

        } catch (InvalidCurrencyCodeException e) {
            response.setStatus(response.SC_BAD_REQUEST);
            printWriter.println(objectMapper.writeValueAsString(new ErrorDTO(e.getMessage())));
        }
    }

    protected void doPatch(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String rateCode = request.getPathInfo().replaceAll("/", "");
        String rateParameter = request.getParameter("rate");

        try {
            Validator.validateRateCode(rateCode);

            BigDecimal rate = new BigDecimal(rateParameter);
            String baseCurrensyCode = rateCode.substring(0, 3);
            String targetCurrensyCode = rateCode.substring(3, 6);
            RequestExchangeRateDTO requestExchangeRateDTO = new RequestExchangeRateDTO(baseCurrensyCode, targetCurrensyCode,rate);

            ExchangeRateModel exchangeRateModel = JDBCRepsitory.updateExchangeRate(requestExchangeRateDTO);
            printWriter.println(objectMapper.writeValueAsString(exchangeRateModel));

        } catch (InvalidCurrencyCodeException e) {
            response.setStatus(response.SC_BAD_REQUEST);
            printWriter.println(objectMapper.writeValueAsString(new ErrorDTO(e.getMessage())));

        } catch (InvalidRateCodeException e) {
            response.setStatus(response.SC_BAD_REQUEST);
            printWriter.println(objectMapper.writeValueAsString(new ErrorDTO(e.getMessage())));

        } catch (NotFoundExchangeRateException e) {
            response.setStatus(response.SC_NOT_FOUND);
            printWriter.println(objectMapper.writeValueAsString(new ErrorDTO(e.getMessage())));

        } catch (SQLException e) {
            response.setStatus(response.SC_INTERNAL_SERVER_ERROR);
            printWriter.println(objectMapper.writeValueAsString(new ErrorDTO(e.getMessage())));
        }
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        printWriter = response.getWriter();

        String method = request.getMethod();
        if (!method.equals("PATCH")) {
            super.service(request, response);
            return;
        }
        this.doPatch(request, response);
    }
}
