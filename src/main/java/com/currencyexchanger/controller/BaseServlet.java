package com.currencyexchanger.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;

import java.io.PrintWriter;

public class BaseServlet extends HttpServlet {

    protected ObjectMapper objMapper = new ObjectMapper();
    protected PrintWriter pWriter;
}
