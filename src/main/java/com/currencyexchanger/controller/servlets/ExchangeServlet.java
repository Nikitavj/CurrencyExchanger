package com.currencyexchanger.controller.servlets;

import com.currencyexchanger.DTO.ErrorDTO;
import com.currencyexchanger.DTO.RequestExchangeDTO;
import com.currencyexchanger.controller.Validator;
import com.currencyexchanger.controller.exception.InvalidParametersException;
import com.currencyexchanger.model.ExchangeModel;
import com.currencyexchanger.servise.Exchange;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

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
            RequestExchangeDTO requestDTO = new RequestExchangeDTO(from, to, amount);

            ExchangeModel exchangeModel = Exchange.exchange(requestDTO);
            printWriter.println(objectMapper.writeValueAsString(exchangeModel));

        } catch (InvalidParametersException e) {
            response.setStatus(response.SC_CONFLICT);
            printWriter.println(objectMapper.writeValueAsString(new ErrorDTO(e.getMessage())));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        printWriter = response.getWriter();
        super.service(request, response);
    }
}
