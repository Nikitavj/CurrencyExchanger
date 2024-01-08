package com.currencyexchanger.controller;

import com.currencyexchanger.DTO.ErrorDTO;
import com.currencyexchanger.DTO.ReqCurrencyDTO;
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
import java.nio.file.FileAlreadyExistsException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "currenciesServlet", value = "/currencies")
public class CurrenciesServlet extends BaseServlet {
    JdbcCurrencyDAO dao = new JdbcCurrencyDAO();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        try {
            List<CurrencyModel> list = dao.readeAll();
            pWriter.println(objMapper.writeValueAsString(list));

        } catch (SQLException e) {
            response.setStatus(response.SC_INTERNAL_SERVER_ERROR);
            pWriter.println(objMapper.writeValueAsString(new ErrorDTO(e.getMessage())));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        String code = request.getParameter("code");
        String sign = request.getParameter("sign");

        try {
            Validator.validateParameters(name, code, sign);
            Validator.validateCurrencyCode(code);
            ReqCurrencyDTO reqCurrencyDTO = new ReqCurrencyDTO(name, code, sign);

            Optional<CurrencyModel> currency = dao.create(reqCurrencyDTO);

            if (currency.isEmpty()) {
                throw new FileAlreadyExistsException("Запись в БД уже существует!");
            }

            pWriter.println(
                    objMapper.writeValueAsString(currency.get()));

        } catch (InvalidCurrencyCodeException
                 | InvalidParametersException  e) {
            response.setStatus(response.SC_BAD_REQUEST);
            pWriter.println(
                    objMapper.writeValueAsString(new ErrorDTO(e.getMessage())));

        } catch (FileAlreadyExistsException e) {
            response.setStatus(response.SC_CONFLICT);
            pWriter.println(
                    objMapper.writeValueAsString(new ErrorDTO(e.getMessage())));

        } catch (SQLException e) {
            response.setStatus(response.SC_INTERNAL_SERVER_ERROR);
            pWriter.println(
                    objMapper.writeValueAsString(new ErrorDTO(e.getMessage())));
        }
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        pWriter = resp.getWriter();
        super.service(req, resp);
    }
}