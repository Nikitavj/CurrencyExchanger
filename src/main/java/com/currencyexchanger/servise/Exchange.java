package com.currencyexchanger.servise;
import com.currencyexchanger.DTO.RequestExchangeDTO;
import com.currencyexchanger.DTO.RequestExchangeRateDTO;
import com.currencyexchanger.controller.exception.NotFoundExchangeRateException;
import com.currencyexchanger.model.ExchangeModel;
import com.currencyexchanger.model.ExchangeRateModel;
import com.currencyexchanger.repository.JDBCRepsitory;
import java.math.BigDecimal;
import java.sql.SQLException;

import static java.math.BigDecimal.ROUND_FLOOR;

public class Exchange {

    public static ExchangeModel exchange(RequestExchangeDTO request) throws SQLException {
        ExchangeModel exchangeModel = null;

        exchangeModel = getDirectExchangeRate(request);
        if (exchangeModel != null) return exchangeModel;

        exchangeModel = getReverseExchangeRate(request);
        if(exchangeModel != null) return exchangeModel;

        exchangeModel = getCalculatedExchangeRate(request);
        if(exchangeModel != null) return exchangeModel;

        return exchangeModel;
    }

    private static ExchangeModel getDirectExchangeRate(RequestExchangeDTO requestExchangeDTO) throws SQLException {
        ExchangeRateModel exchangeRateModel = null;
        ExchangeModel exchangeModel = new ExchangeModel();
        try {
            RequestExchangeRateDTO exchangeRateDTO = new RequestExchangeRateDTO(requestExchangeDTO.getBaseCurrency(), requestExchangeDTO.getTargetCurrency());
            exchangeRateModel = JDBCRepsitory.readExchangeRate(exchangeRateDTO);

            exchangeModel.setBaseCurrency(exchangeRateModel.getBaseCurrency());
            exchangeModel.setTargetCurrency(exchangeRateModel.getTargetCurrency());
            exchangeModel.setRate(exchangeRateModel.getRate());
            exchangeModel.setAmount(requestExchangeDTO.getAmount());
            exchangeModel.setConvertedAmount(requestExchangeDTO.getAmount().multiply(exchangeRateModel.getRate()).setScale(2, ROUND_FLOOR));
        } catch (NotFoundExchangeRateException e) {
            return null;
        }
        return exchangeModel;
    }

    private static ExchangeModel getReverseExchangeRate(RequestExchangeDTO requestExchangeDTO) throws SQLException {
        ExchangeRateModel exchangeRateModel = null;
        ExchangeModel exchangeModel = new ExchangeModel();
        try {
            RequestExchangeRateDTO exchangeRateDTO = new RequestExchangeRateDTO(requestExchangeDTO.getTargetCurrency(), requestExchangeDTO.getBaseCurrency());
            exchangeRateModel = JDBCRepsitory.readExchangeRate(exchangeRateDTO);

            exchangeModel.setBaseCurrency(exchangeRateModel.getTargetCurrency());
            exchangeModel.setTargetCurrency(exchangeRateModel.getBaseCurrency());
            exchangeModel.setRate(new BigDecimal(1).divide(exchangeRateModel.getRate(),6, ROUND_FLOOR));
            exchangeModel.setAmount(requestExchangeDTO.getAmount());
            exchangeModel.setConvertedAmount(requestExchangeDTO.getAmount().divide(exchangeRateModel.getRate(),2, ROUND_FLOOR));
        } catch (NotFoundExchangeRateException e) {
            return null;
        }
        return exchangeModel;
    }

    private static ExchangeModel getCalculatedExchangeRate(RequestExchangeDTO requestExchangeDTO) throws SQLException {
        ExchangeModel exchangeModel = new ExchangeModel();

        try {
            RequestExchangeRateDTO baseExchangeRateDTO = new RequestExchangeRateDTO("USD", requestExchangeDTO.getBaseCurrency());
            ExchangeRateModel baseEexchangeRateModel = JDBCRepsitory.readExchangeRate(baseExchangeRateDTO);
            RequestExchangeRateDTO targetExchangeRateDTO = new RequestExchangeRateDTO("USD", requestExchangeDTO.getTargetCurrency());
            ExchangeRateModel targetEexchangeRateModel = JDBCRepsitory.readExchangeRate(targetExchangeRateDTO);

            exchangeModel.setBaseCurrency(baseEexchangeRateModel.getTargetCurrency());
            exchangeModel.setTargetCurrency(targetEexchangeRateModel.getTargetCurrency());
            exchangeModel.setRate(targetEexchangeRateModel.getRate().divide(baseEexchangeRateModel.getRate(), 6, ROUND_FLOOR));
            exchangeModel.setAmount(requestExchangeDTO.getAmount());
            exchangeModel.setConvertedAmount(requestExchangeDTO.getAmount().multiply(exchangeModel.getRate()).setScale(2, ROUND_FLOOR));

        } catch (NotFoundExchangeRateException e) {
            return null;
        }
        return exchangeModel;
    }
}
