package com.currencyexchanger.controller.servlets;

import com.currencyexchanger.DTO.ErrorDTO;
import com.currencyexchanger.DTO.RequestCurrencyDTO;
import com.currencyexchanger.controller.Validator;
import com.currencyexchanger.controller.exception.InvalidCurrencyCodeException;
import com.currencyexchanger.controller.exception.NotFoundCurrencyException;
import com.currencyexchanger.model.CurrencyModel;
import com.currencyexchanger.repository.JDBCRepsitory;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet(name = "currencyServlet", urlPatterns = "/currency/*")
public class CurrencyServlet extends BaseServlet {


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("text/html");
        CurrencyModel currencyModelObj = null;
        PrintWriter printWriter = response.getWriter();

        String code = request.getPathInfo().replaceAll("/", "");
        try {
            Validator.validateCurrencyCode(code);
            RequestCurrencyDTO requestCurrencyDTO = new RequestCurrencyDTO(code);
            currencyModelObj = JDBCRepsitory.readCurrency(requestCurrencyDTO);
            printWriter.println(objectMapper.writeValueAsString(currencyModelObj));

        } catch (InvalidCurrencyCodeException e) {
            response.setStatus(response.SC_BAD_REQUEST);
            printWriter.println(objectMapper.writeValueAsString(new ErrorDTO(e.getMessage())));
        } catch (NotFoundCurrencyException e) {
            response.setStatus(response.SC_NOT_FOUND);
            printWriter.println(objectMapper.writeValueAsString(new ErrorDTO(e.getMessage())));
        } catch (SQLException e) {
            response.setStatus(response.SC_INTERNAL_SERVER_ERROR);
            printWriter.println(objectMapper.writeValueAsString(new ErrorDTO(e.getMessage())));
        }
    }
}
