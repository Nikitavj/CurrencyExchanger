package com.currencyexchanger.controller;

import com.currencyexchanger.DTO.ErrorDTO;
import com.currencyexchanger.DTO.ReqExchangeRateDTO;
import com.currencyexchanger.dao.JdbcExchangeRateDAO;
import com.currencyexchanger.exception.*;
import com.currencyexchanger.model.ExchangeRateModel;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Optional;

@WebServlet(name = "exchangeRate", value = "/exchangeRate/*")
public class ExchangeRateServlet extends BaseServlet {
    JdbcExchangeRateDAO dao = new JdbcExchangeRateDAO();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String rateCode = request.getPathInfo().replaceAll("/", "");

        try {
            Validator.validateRateCode(rateCode);

            String baseCurrensyCode = rateCode.substring(0, 3);
            String targetCurrensyCode = rateCode.substring(3, 6);
            ReqExchangeRateDTO req = new ReqExchangeRateDTO(baseCurrensyCode, targetCurrensyCode);

            Optional<ExchangeRateModel> rate = dao.readeByCodes(req);

            if (rate.isEmpty()) {
                throw new NotFoundExchangeRateException("Обменный курс не найден в БД!");
            }

            pWriter.println(objMapper.writeValueAsString(rate.get()));

        } catch (InvalidRateCodeException | InvalidCurrencyCodeException e) {
            response.setStatus(response.SC_BAD_REQUEST);
            pWriter.println(objMapper.writeValueAsString(new ErrorDTO(e.getMessage())));

        } catch (NotFoundExchangeRateException e) {
            response.setStatus(response.SC_NOT_FOUND);
            pWriter.println(objMapper.writeValueAsString(new ErrorDTO(e.getMessage())));

        } catch (SQLException e) {
            response.setStatus(response.SC_INTERNAL_SERVER_ERROR);
            pWriter.println(objMapper.writeValueAsString(new ErrorDTO(e.getMessage())));
        }
    }

    protected void doPatch(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JdbcExchangeRateDAO dao = new JdbcExchangeRateDAO();
        String rateCode = request.getPathInfo().replaceAll("/", "");
        String parameter = request.getReader().readLine();
        String rateParameter = parameter.replaceAll("rate=", "");

        try {
            Validator.validateRateCode(rateCode);
            Validator.validateParameter(rateParameter);

            BigDecimal rate = new BigDecimal(rateParameter);
            String baseCurrensyCode = rateCode.substring(0, 3);
            String targetCurrensyCode = rateCode.substring(3, 6);
            ReqExchangeRateDTO req = new ReqExchangeRateDTO(baseCurrensyCode, targetCurrensyCode,rate);

            Optional<ExchangeRateModel> exchangeRate = dao.update(req);

            if (exchangeRate.isEmpty()) {
                throw new NotFoundExchangeRateException("Обменный курс не найден в БД!");
            }

            pWriter.println(objMapper.writeValueAsString(exchangeRate.get()));

        } catch (InvalidCurrencyCodeException
                 | InvalidRateCodeException
                 | InvalidParametersException e) {
            response.setStatus(response.SC_BAD_REQUEST);
            pWriter.println(objMapper.writeValueAsString(new ErrorDTO(e.getMessage())));

        } catch (NotFoundExchangeRateException e) {
            response.setStatus(response.SC_NOT_FOUND);
            pWriter.println(objMapper.writeValueAsString(new ErrorDTO(e.getMessage())));

        } catch (SQLException e) {
            response.setStatus(response.SC_INTERNAL_SERVER_ERROR);
            pWriter.println(objMapper.writeValueAsString(new ErrorDTO(e.getMessage())));
        }
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        pWriter = response.getWriter();

        String method = request.getMethod();
        if (!method.equals("PATCH")) {
            super.service(request, response);
            return;
        }
        this.doPatch(request, response);
    }
}
