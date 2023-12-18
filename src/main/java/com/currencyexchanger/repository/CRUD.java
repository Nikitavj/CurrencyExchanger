package com.currencyexchanger.repository;

import com.currencyexchanger.utils.DBUtils;

import java.sql.Connection;
class CRUD {

    protected static Connection connection = DBUtils.getConnect().get();
}
