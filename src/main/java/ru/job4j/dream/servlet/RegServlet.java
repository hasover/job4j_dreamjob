package ru.job4j.dream.servlet;
import ru.job4j.dream.model.User;
import ru.job4j.dream.store.PsqlStore;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RegServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        if (name.trim().equals("") || email.trim().equals("") || password.trim().equals("")) {
            req.setAttribute("error", "Нужно заполнить все поля");
            req.getRequestDispatcher("reg.jsp").forward(req, resp);
        } else if (PsqlStore.instOf().findUserByMail(email) != null) {
            req.setAttribute("error", "Пользователь уже существует");
            req.getRequestDispatcher("reg.jsp").forward(req, resp);
        } else {
            PsqlStore.instOf().save(new User(0, name, email, password));
            req.getRequestDispatcher("auth.do").forward(req, resp);
        }
    }
}
