package com.currencyexchanger.controller;

import com.currencyexchanger.DTO.ErrorDTO;
import com.currencyexchanger.dao.JdbcCurrencyDAO;
import com.currencyexchanger.exception.DatabaseException;
import com.currencyexchanger.exception.InvalidCurrencyCodeException;
import com.currencyexchanger.model.CurrencyModel;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebServlet(name = "currencyServlet", urlPatterns = "/currency/*")
public class CurrencyServlet extends BaseServlet {
    private JdbcCurrencyDAO dao = new JdbcCurrencyDAO();

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String code = request.getPathInfo().replaceAll("/", "");

        try {
            Validator.validateCurrencyCode(code);

            Optional<CurrencyModel> currency = dao.readeByCode(code);

            if (currency.isEmpty()) {
                response.setStatus(response.SC_NOT_FOUND);
                objMapper.writeValue(
                        pWriter,
                        new ErrorDTO("Валюта отсутсвует в БД!")
                );
                return;
            }

            objMapper.writeValue(pWriter, currency.get());

        } catch (InvalidCurrencyCodeException e) {
            response.setStatus(response.SC_BAD_REQUEST);
            objMapper.writeValue(
                    pWriter,
                    new ErrorDTO(e.getMessage())
            );

        } catch (DatabaseException e) {
            response.setStatus(response.SC_INTERNAL_SERVER_ERROR);
            objMapper.writeValue(
                    pWriter,
                    new ErrorDTO("База данных недоступна!"));
        }
    }

    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        pWriter = resp.getWriter();
        super.service(req, resp);
    }
}
