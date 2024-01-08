package com.currencyexchanger.controller;

import com.currencyexchanger.DTO.ErrorDTO;
import com.currencyexchanger.DTO.ReqExchangeDTO;
import com.currencyexchanger.dao.JdbcExchangeRateDAO;
import com.currencyexchanger.exception.DatabaseException;
import com.currencyexchanger.exception.InvalidCurrencyCodeException;
import com.currencyexchanger.exception.InvalidParametersException;
import com.currencyexchanger.exception.NotFoundExchangeRateException;
import com.currencyexchanger.model.ExchangeModel;
import com.currencyexchanger.model.ExchangeRateModel;
import com.currencyexchanger.servise.Exchange;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Optional;

@WebServlet(name = "exchangeServlet", value = "/exchange")
public class ExchangeServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String from = request.getParameter("from");
        String to = request.getParameter("to");
        String stringAmount = request.getParameter("amount");

        try {
            Validator.validateExchangeParameters(from, to, stringAmount);

            BigDecimal amount = new BigDecimal(stringAmount);
            ReqExchangeDTO req = new ReqExchangeDTO(from, to, amount);

            Optional<ExchangeModel> exchange = new Exchange().exchange(req);

            pWriter.println(objMapper.writeValueAsString(exchange.get()));

        } catch (InvalidParametersException | InvalidCurrencyCodeException e) {
            response.setStatus(response.SC_BAD_REQUEST);
            pWriter.println(objMapper.writeValueAsString(new ErrorDTO(e.getMessage())));

        } catch (NotFoundExchangeRateException e) {
            response.setStatus(response.SC_NOT_FOUND);
            pWriter.println(objMapper.writeValueAsString(new ErrorDTO(e.getMessage())));

        } catch (DatabaseException | SQLException e) {
            response.setStatus(response.SC_INTERNAL_SERVER_ERROR);
            pWriter.println(objMapper.writeValueAsString(new ErrorDTO(e.getMessage())));        }
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        pWriter = response.getWriter();
        super.service(request, response);
    }
}
