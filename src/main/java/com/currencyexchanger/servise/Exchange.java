package com.currencyexchanger.servise;

import com.currencyexchanger.DTO.ReqExchange;
import com.currencyexchanger.dao.JdbcExchangeRateDAO;
import com.currencyexchanger.model.ExchangeModel;
import com.currencyexchanger.model.ExchangeRateModel;

import java.math.BigDecimal;
import java.util.Optional;

import static java.math.BigDecimal.ROUND_FLOOR;

public class Exchange {
    private static final int SCALE_RATE = 6;
    private static final int SCALE_CONVERT_AMOUNT = 2;
    private JdbcExchangeRateDAO dao = new JdbcExchangeRateDAO();

    public Optional<ExchangeModel> exchange(ReqExchange req) {

        Optional<ExchangeModel> exchange = getDirectExchangeRate(req);

        if (exchange.isPresent()) {
            return exchange;
        }

        exchange = getReverseExchangeRate(req);
        if (exchange.isPresent()) {
            return exchange;
        }

        exchange = getConvertExchangeRate(req);
        if (exchange.isPresent()) {
            return exchange;
        }

        return exchange;
    }

    private Optional<ExchangeModel> getDirectExchangeRate(ReqExchange req) {

        Optional<ExchangeRateModel> rateOpt = dao.readeByCodes(
                req.getBaseCurrency(),
                req.getTargetCurrency()
        );

        if (rateOpt.isPresent()) {
            ExchangeRateModel rateModel = rateOpt.get();
            BigDecimal convAmount = req.getAmount().
                    multiply(rateModel.getRate()).
                    setScale(SCALE_CONVERT_AMOUNT, ROUND_FLOOR);

            ExchangeModel model = new ExchangeModel(
                    rateModel.getBaseCurrency(),
                    rateModel.getTargetCurrency(),
                    rateModel.getRate(),
                    req.getAmount(),
                    convAmount
            );

            return Optional.of(model);
        }

        return Optional.empty();
    }

    private Optional<ExchangeModel> getReverseExchangeRate(ReqExchange req) {

        Optional<ExchangeRateModel> rateOpt = dao.readeByCodes(
                req.getTargetCurrency(),
                req.getBaseCurrency()
        );

        if (rateOpt.isPresent()) {
            ExchangeRateModel rateModel = rateOpt.get();

            BigDecimal convertedAmount = req.getAmount().divide(
                    rateModel.getRate(),
                    SCALE_CONVERT_AMOUNT,
                    ROUND_FLOOR
            );
            BigDecimal rate = new BigDecimal(1).
                    divide(rateModel.getRate(), SCALE_RATE, ROUND_FLOOR);

            ExchangeModel exchange = new ExchangeModel(
                    rateModel.getTargetCurrency(),
                    rateModel.getBaseCurrency(),
                    rate,
                    req.getAmount(),
                    convertedAmount
            );

            return Optional.of(exchange);
        }

        return Optional.empty();
    }

    private Optional<ExchangeModel> getConvertExchangeRate(ReqExchange req) {

        Optional<ExchangeRateModel> baseModelOpt = dao.readeByCodes(
                "USD",
                req.getBaseCurrency()
        );
        Optional<ExchangeRateModel> targetModelOpt = dao.readeByCodes(
                "USD",
                req.getTargetCurrency()
        );

        if (baseModelOpt.isPresent() && targetModelOpt.isPresent()) {
            ExchangeRateModel baseRate = baseModelOpt.get();
            ExchangeRateModel targetRate = targetModelOpt.get();

            BigDecimal rate = targetRate.getRate()
                    .divide(baseRate.getRate(), SCALE_RATE, ROUND_FLOOR);
            BigDecimal convAmount = req.getAmount()
                    .multiply(rate)
                    .setScale(SCALE_CONVERT_AMOUNT, ROUND_FLOOR);

            ExchangeModel model = new ExchangeModel(
                    baseRate.getTargetCurrency(),
                    targetRate.getTargetCurrency(),
                    rate,
                    req.getAmount(),
                    convAmount
            );

            return Optional.of(model);
        }

        return Optional.empty();
    }
}
