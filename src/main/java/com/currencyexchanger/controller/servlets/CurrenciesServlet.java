package com.currencyexchanger.controller.servlets;

import com.currencyexchanger.DTO.ErrorDTO;
import com.currencyexchanger.DTO.RequestCurrenciesDTO;
import com.currencyexchanger.controller.Validator;
import com.currencyexchanger.controller.exception.InvalidCurrencyCodeException;
import com.currencyexchanger.model.CurrencyModel;
import com.currencyexchanger.repository.JDBCRepsitory;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileAlreadyExistsException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "currenciesServlet", value = "/currencies")
public class CurrenciesServlet extends BaseServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter printWriter = response.getWriter();
        List<CurrencyModel> list = null;

        try {
            list = JDBCRepsitory.readCurrencies();
            String json = objectMapper.writeValueAsString(list);
            printWriter.println(json);

        } catch (SQLException e) {
            response.setStatus(response.SC_INTERNAL_SERVER_ERROR);
            printWriter.println(objectMapper.writeValueAsString(new ErrorDTO(e.getMessage())));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter printWriter = response.getWriter();
        CurrencyModel currencyModel = null;

        String name = request.getParameter("name");
        String code = request.getParameter("code");
        String sign = request.getParameter("sign");

        try {
            Validator.validateCurrencyCode(code);
            RequestCurrenciesDTO requestCurrenciesDTO = new RequestCurrenciesDTO(name, code, sign);
            currencyModel = JDBCRepsitory.createCurrency(requestCurrenciesDTO);
            printWriter.println(objectMapper.writeValueAsString(currencyModel));

        } catch (InvalidCurrencyCodeException e) {
            response.setStatus(response.SC_CONFLICT);
            printWriter.println(objectMapper.writeValueAsString(new ErrorDTO(e.getMessage())));
        } catch (FileAlreadyExistsException e) {
            response.setStatus(response.SC_CONFLICT);
            printWriter.println(objectMapper.writeValueAsString(new ErrorDTO(e.getMessage())));
        } catch (NoSuchFieldException e) {
            response.setStatus(response.SC_BAD_REQUEST);
            printWriter.println(objectMapper.writeValueAsString(new ErrorDTO(e.getMessage())));
        }
    }
}