package com.currencyexchanger.controller;

import com.currencyexchanger.DTO.ErrorDTO;
import com.currencyexchanger.DTO.ReqCurrencyDTO;
import com.currencyexchanger.dao.JdbcCurrencyDAO;
import com.currencyexchanger.exception.DatabaseException;
import com.currencyexchanger.exception.InvalidCurrencyCodeException;
import com.currencyexchanger.exception.NotFoundCurrencyException;
import com.currencyexchanger.model.CurrencyModel;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

@WebServlet(name = "currencyServlet", urlPatterns = "/currency/*")
public class CurrencyServlet extends BaseServlet {
    JdbcCurrencyDAO dao = new JdbcCurrencyDAO();

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String code = request.getPathInfo().replaceAll("/", "");

        try {
            Validator.validateCurrencyCode(code);

            ReqCurrencyDTO req = new ReqCurrencyDTO(code);

            Optional<CurrencyModel> currency = dao.readeByCode(req);

            if (currency.isEmpty()) {
                throw new NotFoundCurrencyException("Валюта отсутсвует в БД!");
            }

            pWriter.println(objMapper.writeValueAsString(currency.get()));

        } catch (InvalidCurrencyCodeException e) {
            response.setStatus(response.SC_BAD_REQUEST);
            pWriter.println(objMapper.writeValueAsString(new ErrorDTO(e.getMessage())));

        } catch (NotFoundCurrencyException e) {
            response.setStatus(response.SC_NOT_FOUND);
            pWriter.println(objMapper.writeValueAsString(new ErrorDTO(e.getMessage())));

        } catch (SQLException e) {
            response.setStatus(response.SC_INTERNAL_SERVER_ERROR);
            pWriter.println(objMapper.writeValueAsString(new ErrorDTO(e.getMessage())));
        }
    }

    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        pWriter = resp.getWriter();
        super.service(req, resp);
    }
}
