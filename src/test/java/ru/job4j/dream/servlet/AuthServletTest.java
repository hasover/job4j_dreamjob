package ru.job4j.dream.servlet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ru.job4j.dream.model.User;
import ru.job4j.dream.store.MemStore;
import ru.job4j.dream.store.PsqlStore;
import ru.job4j.dream.store.Store;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PsqlStore.class)
public class AuthServletTest {

    @Test
    public void whenCanLogin() throws ServletException, IOException {
        Store store = MemStore.instOf();
        User user = new User(0, "login", "login", "password");
        store.save(user);

        PowerMockito.mockStatic(PsqlStore.class);
        PowerMockito.when(PsqlStore.instOf()).thenReturn(store);

        HttpServletRequest req = PowerMockito.mock(HttpServletRequest.class);
        Mockito.when(req.getParameter("email")).thenReturn("login");
        Mockito.when(req.getParameter("password")).thenReturn("password");

        HttpServletResponse resp = Mockito.mock(HttpServletResponse.class);

        HttpSession session = Mockito.mock(HttpSession.class);
        Mockito.when(req.getSession()).thenReturn(session);

        new AuthServlet().doPost(req, resp);
        Mockito.verify(session).setAttribute("user", store.findUserByMail("login"));
    }

    @Test
    public void whenCannotLogin() throws ServletException, IOException {
        Store store = MemStore.instOf();
        User user = new User(0, "login1", "login1", "password");
        store.save(user);

        PowerMockito.mockStatic(PsqlStore.class);
        PowerMockito.when(PsqlStore.instOf()).thenReturn(store);

        HttpServletRequest req = PowerMockito.mock(HttpServletRequest.class);
        Mockito.when(req.getParameter("email")).thenReturn("login1");
        Mockito.when(req.getParameter("password")).thenReturn("password1");

        HttpServletResponse resp = Mockito.mock(HttpServletResponse.class);
        RequestDispatcher dispatcher = Mockito.mock(RequestDispatcher.class);
        Mockito.when(req.getRequestDispatcher(Mockito.any())).thenReturn(dispatcher);

        new AuthServlet().doPost(req, resp);
        Mockito.verify(req).getRequestDispatcher(Mockito.any());
    }
}