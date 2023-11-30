package com.currencyexchanger.control;

import com.currencyexchanger.control.error.ErrorDTO;
import com.currencyexchanger.control.exception.InvalidExchangeParameters;
import com.currencyexchanger.servise.Exchange;
import com.currencyexchanger.servise.ExchangeDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet(name = "exchange", value = "/exchange")
public class ExchangeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        ExchangeDTO exchangeDTO = null;
        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter printWriter = response.getWriter();

        String from = request.getParameter("from");
        String to = request.getParameter("to");
        String stringAmount = request.getParameter("amount");

        try {
            ValidateRequest.validateExchangeParameters(from, to, stringAmount);
            double amount = Double.parseDouble(stringAmount);
            exchangeDTO = Exchange.getExchange(from, to, amount);
            String json = objectMapper.writeValueAsString(exchangeDTO);
            printWriter.println(json);
        } catch (InvalidExchangeParameters e) {
            response.setStatus(400);
            ErrorDTO error = new ErrorDTO(e.getMessage());
            String jsonError = objectMapper.writeValueAsString(error);
            printWriter.println(jsonError);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
