package com.currencyexchanger.servise;

import com.currencyexchanger.DTO.RequestExchangeDTO;
import com.currencyexchanger.DTO.RequestExchangeRateDTO;
import com.currencyexchanger.controller.exception.DatabaseException;
import com.currencyexchanger.controller.exception.NotFoundExchangeRateException;
import com.currencyexchanger.model.ExchangeModel;
import com.currencyexchanger.model.ExchangeRateModel;
import com.currencyexchanger.repository.JDBCRepsitory;
import java.math.BigDecimal;
import java.util.Optional;

import static java.math.BigDecimal.ROUND_FLOOR;

public class Exchange {

    public static Optional<ExchangeModel> exchange(RequestExchangeDTO request) throws NotFoundExchangeRateException, DatabaseException {

        Optional<ExchangeModel> exchangeOptional = getDirectExchangeRate(request);

        if (exchangeOptional.isPresent()) {
            return exchangeOptional;
        }

        exchangeOptional = getReverseExchangeRate(request);
        if (exchangeOptional.isPresent()) {
            return exchangeOptional;
        }

        exchangeOptional = getCalculatedExchangeRate(request);
        if (exchangeOptional.isPresent()) {
            return exchangeOptional;
        }

        return Optional.empty();
    }

    private static Optional<ExchangeModel> getDirectExchangeRate(RequestExchangeDTO requestExchangeDTO) throws NotFoundExchangeRateException, DatabaseException {

        ExchangeModel exchangeModel = new ExchangeModel();

        RequestExchangeRateDTO exchangeRateDTO = new RequestExchangeRateDTO(requestExchangeDTO.getBaseCurrency(),
                requestExchangeDTO.getTargetCurrency());

        Optional<ExchangeRateModel> exchangeRateOptional = JDBCRepsitory.readExchangeRate(exchangeRateDTO);

        if (exchangeRateOptional.isEmpty()) {
            return Optional.empty();
        }

        ExchangeRateModel exchangeRateModel = exchangeRateOptional.get();
        exchangeModel.setBaseCurrency(exchangeRateModel.getBaseCurrency());
        exchangeModel.setTargetCurrency(exchangeRateModel.getTargetCurrency());
        exchangeModel.setRate(exchangeRateModel.getRate());
        exchangeModel.setAmount(requestExchangeDTO.getAmount());
        exchangeModel.setConvertedAmount(requestExchangeDTO.getAmount()
                .multiply(exchangeRateModel.getRate())
                .setScale(2, ROUND_FLOOR));

        return Optional.of(exchangeModel);
    }

    private static Optional<ExchangeModel> getReverseExchangeRate(RequestExchangeDTO requestExchangeDTO) throws NotFoundExchangeRateException, DatabaseException {

        ExchangeModel exchangeModel = new ExchangeModel();

        RequestExchangeRateDTO exchangeRateDTO = new RequestExchangeRateDTO(requestExchangeDTO.getTargetCurrency(),
                requestExchangeDTO.getBaseCurrency());

        Optional<ExchangeRateModel> exchangeRateOptional = JDBCRepsitory.readExchangeRate(exchangeRateDTO);
        if (exchangeRateOptional.isEmpty()) {
            return Optional.empty();
        }

        ExchangeRateModel exchangeRateModel = exchangeRateOptional.get();
        exchangeModel.setBaseCurrency(exchangeRateModel.getTargetCurrency());
        exchangeModel.setTargetCurrency(exchangeRateModel.getBaseCurrency());
        exchangeModel.setRate(new BigDecimal(1).divide(exchangeRateModel.getRate(), 6, ROUND_FLOOR));
        exchangeModel.setAmount(requestExchangeDTO.getAmount());
        exchangeModel.setConvertedAmount(requestExchangeDTO.getAmount()
                .divide(exchangeRateModel.getRate(), 2, ROUND_FLOOR));

        return Optional.of(exchangeModel);
    }

    private static Optional<ExchangeModel> getCalculatedExchangeRate(RequestExchangeDTO requestExchangeDTO) throws NotFoundExchangeRateException, DatabaseException {

        ExchangeModel exchangeModel = new ExchangeModel();

        RequestExchangeRateDTO baseExchangeRateDTO = new RequestExchangeRateDTO("USD", requestExchangeDTO.getBaseCurrency());
        RequestExchangeRateDTO targetExchangeRateDTO = new RequestExchangeRateDTO("USD", requestExchangeDTO.getTargetCurrency());

        Optional<ExchangeRateModel> baseModelOptional = JDBCRepsitory.readExchangeRate(baseExchangeRateDTO);
        Optional<ExchangeRateModel> targetModelOptional = JDBCRepsitory.readExchangeRate(targetExchangeRateDTO);

        if (baseModelOptional.isEmpty() || targetModelOptional.isEmpty()) {
            return Optional.empty();
        }

        ExchangeRateModel baseEexchangeRateModel = baseModelOptional.get();
        ExchangeRateModel targetEexchangeRateModel = targetModelOptional.get();

        exchangeModel.setBaseCurrency(baseEexchangeRateModel.getTargetCurrency());
        exchangeModel.setTargetCurrency(targetEexchangeRateModel.getTargetCurrency());
        exchangeModel.setRate(targetEexchangeRateModel.getRate()
                .divide(baseEexchangeRateModel.getRate(), 6, ROUND_FLOOR));
        exchangeModel.setAmount(requestExchangeDTO.getAmount());
        exchangeModel.setConvertedAmount(requestExchangeDTO.getAmount()
                .multiply(exchangeModel.getRate())
                .setScale(2, ROUND_FLOOR));

        return Optional.of(exchangeModel);
    }
}
