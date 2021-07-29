package ru.job4j.dream.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.job4j.dream.store.PsqlStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class CityServlet extends HttpServlet {
    private final static Gson GSON = new GsonBuilder().create();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=utf-8");
        OutputStream out = resp.getOutputStream();
        String json = GSON.toJson(PsqlStore.instOf().findAllCities());
        out.write(json.getBytes(StandardCharsets.UTF_8));
        out.flush();
        out.close();
    }
}
