package ru.job4j.dream.servlet;

import ru.job4j.dream.store.MemStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

public class RemoveServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        MemStore.instOf().removeCandidate(Integer.parseInt(req.getParameter("id")));
        File file = new File("C:/images/" + req.getParameter("id"));
        file.delete();
        resp.sendRedirect(req.getContextPath() + "/candidates.do");
    }
}
