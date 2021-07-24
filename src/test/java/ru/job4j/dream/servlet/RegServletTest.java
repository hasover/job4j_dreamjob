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
import java.io.IOException;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PsqlStore.class)
public class RegServletTest {

    @Test
    public void whenCanRegister() throws ServletException, IOException {
        Store store = MemStore.instOf();
        PowerMockito.mockStatic(PsqlStore.class);
        Mockito.when(PsqlStore.instOf()).thenReturn(store);

        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getParameter("name")).thenReturn("new");
        Mockito.when(request.getParameter("email")).thenReturn("new");
        Mockito.when(request.getParameter("password")).thenReturn("new");
        Mockito.when(request.getRequestDispatcher(Mockito.any())).thenReturn(Mockito.mock(RequestDispatcher.class));

        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

        new RegServlet().doPost(request, response);
        Mockito.verify(request).getRequestDispatcher(Mockito.any());
    }

    @Test
    public void whenCannotRegister() throws ServletException, IOException {
        Store store = MemStore.instOf();
        store.save(new User(0, "new2", "new2", "new2"));
        PowerMockito.mockStatic(PsqlStore.class);
        Mockito.when(PsqlStore.instOf()).thenReturn(store);

        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getParameter("name")).thenReturn("new2");
        Mockito.when(request.getParameter("email")).thenReturn("new2");
        Mockito.when(request.getParameter("password")).thenReturn("new2");
        Mockito.when(request.getRequestDispatcher(Mockito.any())).thenReturn(Mockito.mock(RequestDispatcher.class));

        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

        new RegServlet().doPost(request, response);
        Mockito.verify(request).setAttribute(Mockito.any(), Mockito.any());
    }
}