package com.currencyexchanger.controller;

import com.currencyexchanger.DTO.ErrorDTO;
import com.currencyexchanger.dao.JdbcCurrencyDAO;
import com.currencyexchanger.exception.DatabaseException;
import com.currencyexchanger.exception.InvalidCurrencyCodeException;
import com.currencyexchanger.exception.InvalidParametersException;
import com.currencyexchanger.model.CurrencyModel;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

@WebServlet(name = "currenciesServlet", value = "/currencies")
public class CurrenciesServlet extends BaseServlet {
    private final int ERROR_CODE_SQLITE_CONSTRAINT = 19;
    private final JdbcCurrencyDAO dao = new JdbcCurrencyDAO();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        try {
            List<CurrencyModel> list = dao.readeAll();
            objMapper.writeValue(pWriter, list);

        } catch (DatabaseException e) {
            response.setStatus(response.SC_INTERNAL_SERVER_ERROR);
            objMapper.writeValue(
                    pWriter,
                    new ErrorDTO("База данных недоступна!")
            );
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String name = request.getParameter("name");
        String code = request.getParameter("code");
        String sign = request.getParameter("sign");
        List<String> parameters = Arrays.asList(name, code, sign);

        try {
            Validator.validateParameters(parameters);
            Validator.validateCurrencyCode(code);

        } catch (InvalidCurrencyCodeException
                 | InvalidParametersException e) {

            response.setStatus(response.SC_BAD_REQUEST);
            objMapper.writeValue(
                    pWriter,
                    new ErrorDTO(e.getMessage())
            );
            return;
        }

        try {
            CurrencyModel curr = new CurrencyModel(null, name, code, sign);
            curr = dao.create(curr);

            objMapper.writeValue(pWriter, curr);

        } catch (DatabaseException e) {
            SQLException t = (SQLException) e.getCause();

            if (t.getErrorCode() == ERROR_CODE_SQLITE_CONSTRAINT) {
                response.setStatus(response.SC_CONFLICT);
                objMapper.writeValue(
                        pWriter,
                        new ErrorDTO("Валюта с таким кодом уже существует!")
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
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        pWriter = response.getWriter();
        super.service(request, response);
    }
}