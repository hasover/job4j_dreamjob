package ru.job4j.dream.servlet;
import ru.job4j.dream.model.User;
import ru.job4j.dream.store.PsqlStore;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import javax.validation.ConstraintViolationException;

public class RegServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        try {
            if (PsqlStore.instOf().findUserByMail(email) != null) {
                throw new ConstraintViolationException("Пользователь уже существует", null);
            }
            PsqlStore.instOf().save(new User(0, name, email, password));
            req.getRequestDispatcher("auth.do").forward(req, resp);
        } catch (ConstraintViolationException ex) {
            req.setAttribute("error", ex.getMessage());
            req.getRequestDispatcher("reg.jsp").forward(req, resp);
        }
    }
}
