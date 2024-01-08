package com.currencyexchanger.servise;

import com.currencyexchanger.DTO.ReqExchangeDTO;
import com.currencyexchanger.DTO.ReqExchangeRateDTO;
import com.currencyexchanger.dao.JdbcExchangeRateDAO;
import com.currencyexchanger.exception.DatabaseException;
import com.currencyexchanger.exception.NotFoundExchangeRateException;
import com.currencyexchanger.model.ExchangeModel;
import com.currencyexchanger.model.ExchangeRateModel;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Optional;

import static java.math.BigDecimal.ROUND_FLOOR;

public class Exchange {
    private JdbcExchangeRateDAO dao = new JdbcExchangeRateDAO();

    public Optional<ExchangeModel> exchange(ReqExchangeDTO request) throws NotFoundExchangeRateException, DatabaseException, SQLException {

        Optional<ExchangeModel> exchange = getDirectExchangeRate(request);

        if (exchange.isPresent()) {
            return exchange;
        }

        exchange = getReverseExchangeRate(request);
        if (exchange.isPresent()) {
            return exchange;
        }

        exchange = getCalculatedExchangeRate(request);
        if (exchange.isPresent()) {
            return exchange;
        }

        return Optional.empty();
    }

    private Optional<ExchangeModel> getDirectExchangeRate(ReqExchangeDTO req) throws NotFoundExchangeRateException, DatabaseException, SQLException {

        ExchangeModel exchangeModel = new ExchangeModel();

        ReqExchangeRateDTO reqExchangeRateDTO = new ReqExchangeRateDTO(req.getBaseCurrency(),
                req.getTargetCurrency());

        Optional<ExchangeRateModel> exchangeRateOpt = dao.readeByCodes(reqExchangeRateDTO);

        if (exchangeRateOpt.isEmpty()) {
            return Optional.empty();
        }

        ExchangeRateModel exchangeRateModel = exchangeRateOpt.get();
        exchangeModel.setBaseCurrency(exchangeRateModel.getBaseCurrency());
        exchangeModel.setTargetCurrency(exchangeRateModel.getTargetCurrency());
        exchangeModel.setRate(exchangeRateModel.getRate());
        exchangeModel.setAmount(req.getAmount());
        exchangeModel.setConvertedAmount(req.getAmount()
                .multiply(exchangeRateModel.getRate())
                .setScale(2, ROUND_FLOOR));

        return Optional.of(exchangeModel);
    }

    private Optional<ExchangeModel> getReverseExchangeRate(ReqExchangeDTO req) throws NotFoundExchangeRateException, DatabaseException, SQLException {

        ExchangeModel exchangeModel = new ExchangeModel();

        ReqExchangeRateDTO reqExchangeRateDTO = new ReqExchangeRateDTO(req.getTargetCurrency(),
                req.getBaseCurrency());

        Optional<ExchangeRateModel> exchangeRateOptional = dao.readeByCodes(reqExchangeRateDTO);
        if (exchangeRateOptional.isEmpty()) {
            return Optional.empty();
        }

        ExchangeRateModel exchangeRateModel = exchangeRateOptional.get();
        exchangeModel.setBaseCurrency(exchangeRateModel.getTargetCurrency());
        exchangeModel.setTargetCurrency(exchangeRateModel.getBaseCurrency());
        exchangeModel.setRate(new BigDecimal(1).divide(exchangeRateModel.getRate(), 6, ROUND_FLOOR));
        exchangeModel.setAmount(req.getAmount());
        exchangeModel.setConvertedAmount(req.getAmount()
                .divide(exchangeRateModel.getRate(), 2, ROUND_FLOOR));

        return Optional.of(exchangeModel);
    }

    private Optional<ExchangeModel> getCalculatedExchangeRate(ReqExchangeDTO req) throws NotFoundExchangeRateException, DatabaseException, SQLException {

        ExchangeModel exchangeModel = new ExchangeModel();

        ReqExchangeRateDTO baseExchangeRateDTO = new ReqExchangeRateDTO("USD", req.getBaseCurrency());
        ReqExchangeRateDTO targetExchangeRateDTO = new ReqExchangeRateDTO("USD", req.getTargetCurrency());

        Optional<ExchangeRateModel> baseModelOptional = dao.readeByCodes(baseExchangeRateDTO);
        Optional<ExchangeRateModel> targetModelOptional =dao.readeByCodes(targetExchangeRateDTO);;

        if (baseModelOptional.isEmpty() || targetModelOptional.isEmpty()) {
            return Optional.empty();
        }

        ExchangeRateModel baseEexchangeRateModel = baseModelOptional.get();
        ExchangeRateModel targetEexchangeRateModel = targetModelOptional.get();

        exchangeModel.setBaseCurrency(baseEexchangeRateModel.getTargetCurrency());
        exchangeModel.setTargetCurrency(targetEexchangeRateModel.getTargetCurrency());
        exchangeModel.setRate(targetEexchangeRateModel.getRate()
                .divide(baseEexchangeRateModel.getRate(), 6, ROUND_FLOOR));
        exchangeModel.setAmount(req.getAmount());
        exchangeModel.setConvertedAmount(req.getAmount()
                .multiply(exchangeModel.getRate())
                .setScale(2, ROUND_FLOOR));

        return Optional.of(exchangeModel);
    }
}
