package com.currencyexchanger.control;

import com.currencyexchanger.control.error.ErrorDTO;
import com.currencyexchanger.control.error.ErrorMessages;
import com.currencyexchanger.control.exception.InvalidCurrencyCodeException;
import com.currencyexchanger.control.exception.NotFoundCurrencyException;
import com.currencyexchanger.model.Currency;
import com.currencyexchanger.repository.Read;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet(name = "currency", urlPatterns = "/currency/*")
public class CurrencyServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        Currency currencyObj = null;
        PrintWriter printWriter = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();

        String path = request.getPathInfo();
        String[] codes = path.split("/");
        String code = codes[1];

        try {
            ValidateRequest.validateCurrencyCode(code);
            currencyObj = Read.getCurrency(code);
            String json = objectMapper.writeValueAsString(currencyObj);
            printWriter.println(json);

        } catch (InvalidCurrencyCodeException e) {
            response.setStatus(400);
            ErrorDTO error = new ErrorDTO(ErrorMessages.CURRENCY_GET_400.getMessage());
            String jsonError = objectMapper.writeValueAsString(error);
            printWriter.println(jsonError);

        } catch (NotFoundCurrencyException e) {
            response.setStatus(404);
            ErrorDTO error = new ErrorDTO(ErrorMessages.CURRENCY_GET_404.getMessage());
            String jsonError = objectMapper.writeValueAsString(error);
            printWriter.println(jsonError);

        } catch (SQLException e) {
            response.setStatus(500);
            ErrorDTO error = new ErrorDTO(ErrorMessages.ERROR_DATABASE.getMessage());
            String json = objectMapper.writeValueAsString(error);
            printWriter.println(json);
        }
    }
}
