package com.currencyexchanger.controller.servlets;

import com.currencyexchanger.DTO.ErrorDTO;
import com.currencyexchanger.DTO.RequestCurrenciesDTO;
import com.currencyexchanger.controller.Validator;
import com.currencyexchanger.controller.exception.DatabaseException;
import com.currencyexchanger.controller.exception.InvalidCurrencyCodeException;
import com.currencyexchanger.controller.exception.InvalidParametersException;
import com.currencyexchanger.model.CurrencyModel;
import com.currencyexchanger.repository.JDBCRepsitory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.List;

@WebServlet(name = "currenciesServlet", value = "/currencies")
public class CurrenciesServlet extends BaseServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        try {
            List<CurrencyModel> list = JDBCRepsitory.readCurrencies();
            printWriter.println(objectMapper.writeValueAsString(list));

        } catch (DatabaseException e) {
            response.setStatus(response.SC_INTERNAL_SERVER_ERROR);
            printWriter.println(objectMapper.writeValueAsString(new ErrorDTO(e.getMessage())));
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
            RequestCurrenciesDTO requestCurrenciesDTO = new RequestCurrenciesDTO(name, code, sign);

            CurrencyModel currencyModel = JDBCRepsitory.createCurrency(requestCurrenciesDTO)
                    .orElseThrow(DatabaseException::new);

            printWriter.println(objectMapper.writeValueAsString(currencyModel));

        } catch (InvalidCurrencyCodeException
                 | NoSuchFieldException
                 | InvalidParametersException  e) {
            response.setStatus(response.SC_BAD_REQUEST);
            printWriter.println(objectMapper.writeValueAsString(new ErrorDTO(e.getMessage())));

        } catch (FileAlreadyExistsException e) {
            response.setStatus(response.SC_CONFLICT);
            printWriter.println(objectMapper.writeValueAsString(new ErrorDTO(e.getMessage())));

        } catch (DatabaseException e) {
            response.setStatus(response.SC_INTERNAL_SERVER_ERROR);
            printWriter.println(objectMapper.writeValueAsString(new ErrorDTO(e.getMessage())));
        }
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        printWriter = resp.getWriter();
        super.service(req, resp);
    }
}