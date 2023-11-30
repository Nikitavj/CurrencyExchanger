package com.currencyexchanger.control;

import com.currencyexchanger.control.error.ErrorDTO;
import com.currencyexchanger.control.error.ErrorMessages;
import com.currencyexchanger.model.Currency;
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

@WebServlet(name = "cyrrencies", value = "/currencies")
public class CurrenciesServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter printWriter = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        List<Currency> list = null;
        try {
            list = Read.getCurrencies();
            String json = objectMapper.writeValueAsString(list);
            printWriter.println(json);
        } catch (SQLException e) {
            response.setStatus(500);
            ErrorDTO error = new ErrorDTO(ErrorMessages.ERROR_DATABASE.getMessage());
            String json = objectMapper.writeValueAsString(error);
            printWriter.println(json);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String name = request.getParameter("name");
        String code = request.getParameter("code");
        String sign = request.getParameter("sign");

        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter printWriter = response.getWriter();

        Currency currency = null;
        try {
            currency = Create.addCurrency(code, name, sign);
            String json = objectMapper.writeValueAsString(currency);
            printWriter.println(json);
        } catch (SQLException e) {
            if (e.getErrorCode() == 1048) {
                response.setStatus(400);
                ErrorDTO error = new ErrorDTO(ErrorMessages.CURRENCIES_POST_400.getMessage());
                String json = objectMapper.writeValueAsString(error);
                printWriter.println(json);
            }
            if (e.getErrorCode() == 1062) {
                response.setStatus(409);
                ErrorDTO error = new ErrorDTO(ErrorMessages.CURRENCIES_POST_409.getMessage());
                String json = objectMapper.writeValueAsString(error);
                printWriter.println(json);
            }
        }
    }
}