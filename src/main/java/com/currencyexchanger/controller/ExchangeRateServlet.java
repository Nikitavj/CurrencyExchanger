package com.currencyexchanger.controller;

import com.currencyexchanger.DTO.ErrorDTO;
import com.currencyexchanger.dao.JdbcExchangeRateDAO;
import com.currencyexchanger.exception.*;
import com.currencyexchanger.model.ExchangeRateModel;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

@WebServlet(name = "exchangeRate", value = "/exchangeRate/*")
public class ExchangeRateServlet extends BaseServlet {
    private static final int POSITION_BASE_CODE = 3;
    private static final int POSITION_TARGET_CODE = 6;
    private JdbcExchangeRateDAO dao = new JdbcExchangeRateDAO();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String rateCode = request.getPathInfo().replaceAll("/", "");

        try {
            Validator.validateRateCode(rateCode);

            String baseCurrCode = rateCode.substring(0, POSITION_BASE_CODE);
            String targetCurrCode = rateCode.substring(
                    POSITION_BASE_CODE,
                    POSITION_TARGET_CODE
            );

            Optional<ExchangeRateModel> rate = dao.readeByCodes(
                    baseCurrCode,
                    targetCurrCode
            );

            if (rate.isEmpty()) {
                response.setStatus(response.SC_NOT_FOUND);
                objMapper.writeValue(
                        pWriter,
                        new ErrorDTO("Обменный курс не найден в БД!")
                );
                return;
            }

            objMapper.writeValue(pWriter, rate.get());

        } catch (InvalidRateCodeException | InvalidCurrencyCodeException e) {
            response.setStatus(response.SC_BAD_REQUEST);
            objMapper.writeValue(
                    pWriter,
                    new ErrorDTO(e.getMessage())
            );

        } catch (DatabaseException e) {
            response.setStatus(response.SC_INTERNAL_SERVER_ERROR);
            objMapper.writeValue(
                    pWriter,
                    new ErrorDTO("База данных недоступна!")
            );
        }
    }

    protected void doPatch(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String rateCode = request.getPathInfo().replaceAll("/", "");
        String parameter = request.getReader().readLine();
        String rateParameter = parameter.replaceAll("rate=", "");

        try {
            Validator.validateRateCode(rateCode);
            Validator.validateParameter(rateParameter);

            BigDecimal rate = new BigDecimal(rateParameter);
            String baseCurrCode = rateCode.substring(0, POSITION_BASE_CODE);
            String targetCurrCode = rateCode.substring(
                    POSITION_BASE_CODE,
                    POSITION_TARGET_CODE
            );

            Optional<ExchangeRateModel> modelOpt = dao.readeByCodes(
                    baseCurrCode,
                    targetCurrCode
            );

            if (modelOpt.isEmpty()) {
                response.setStatus(response.SC_NOT_FOUND);
                objMapper.writeValue(
                        pWriter,
                        new ErrorDTO("Обменный курс не существует в БД!")
                );
                return;
            }

            ExchangeRateModel model = modelOpt.get();
            model.setRate(rate);
            dao.update(model);

            objMapper.writeValue(pWriter, model);

        } catch (InvalidCurrencyCodeException
                 | InvalidRateCodeException
                 | InvalidParametersException e) {
            response.setStatus(response.SC_BAD_REQUEST);
            objMapper.writeValue(
                    pWriter,
                    new ErrorDTO(e.getMessage())
            );

        } catch (DatabaseException e) {
            response.setStatus(response.SC_INTERNAL_SERVER_ERROR);
            objMapper.writeValue(
                    pWriter,
                    new ErrorDTO("База данных недоступна!")
            );
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
