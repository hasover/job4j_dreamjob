package ru.job4j.dream.servlet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.store.MemStore;
import ru.job4j.dream.store.PsqlStore;
import ru.job4j.dream.store.Store;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PsqlStore.class)
public class PostServletTest {

    @Test
    public void whenCreateNewPost() throws ServletException, IOException {
        Store store = MemStore.instOf();

        PowerMockito.mockStatic(PsqlStore.class);
        PowerMockito.when(PsqlStore.instOf()).thenReturn(store);

        HttpServletRequest req = PowerMockito.mock(HttpServletRequest.class);
        HttpServletResponse resp = PowerMockito.mock(HttpServletResponse.class);

        PowerMockito.when(req.getParameter("id")).thenReturn("0");
        PowerMockito.when(req.getParameter("name")).thenReturn("n");
        PowerMockito.when(req.getParameter("description")).thenReturn("d");

        new PostServlet().doPost(req, resp);
        Post result = store.findPostById(4);

        assertThat(result.getName(), is("n"));
        assertThat(result.getDescription(), is("d"));
    }

    @Test
    public void whenEditExistingPost() throws ServletException, IOException {
        Store store = MemStore.instOf();

        PowerMockito.mockStatic(PsqlStore.class);
        PowerMockito.when(PsqlStore.instOf()).thenReturn(store);

        HttpServletRequest req = PowerMockito.mock(HttpServletRequest.class);
        HttpServletResponse resp = PowerMockito.mock(HttpServletResponse.class);

        PowerMockito.when(req.getParameter("id")).thenReturn("1");
        PowerMockito.when(req.getParameter("name")).thenReturn("n");
        PowerMockito.when(req.getParameter("description")).thenReturn("d");

        new PostServlet().doPost(req, resp);
        Post result = store.findPostById(1);

        assertThat(result.getName(), is("n"));
        assertThat(result.getDescription(), is("d"));
    }
}
