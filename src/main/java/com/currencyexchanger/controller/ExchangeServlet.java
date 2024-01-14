package com.currencyexchanger.controller;

import com.currencyexchanger.DTO.ErrorDTO;
import com.currencyexchanger.DTO.ReqExchange;
import com.currencyexchanger.exception.DatabaseException;
import com.currencyexchanger.exception.InvalidCurrencyCodeException;
import com.currencyexchanger.exception.InvalidParametersException;
import com.currencyexchanger.model.ExchangeModel;
import com.currencyexchanger.servise.Exchange;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
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
            ReqExchange req = new ReqExchange(from, to, amount);

            Optional<ExchangeModel> exchange = new Exchange().exchange(req);

            if (exchange.isEmpty()) {
                response.setStatus(response.SC_BAD_REQUEST);
                objMapper.writeValue(
                        pWriter,
                        new ErrorDTO("Невозможно произвести обмен")
                );
                return;
            }

            objMapper.writeValue(pWriter, exchange.get());

        } catch (InvalidParametersException | InvalidCurrencyCodeException e) {
            response.setStatus(response.SC_BAD_REQUEST);
            objMapper.writeValue(
                    pWriter,
                    new ErrorDTO(e.getMessage())
            );

        } catch (DatabaseException e) {
            response.setStatus(response.SC_INTERNAL_SERVER_ERROR);
            objMapper.writeValue(
                    pWriter,
                    new ErrorDTO(e.getMessage())
            );
        }
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        pWriter = response.getWriter();
        super.service(request, response);
    }
}
