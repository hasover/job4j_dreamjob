package ru.job4j.dream.servlet;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.store.MemStore;
import ru.job4j.dream.store.PsqlStore;
import ru.job4j.dream.store.Store;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PsqlStore.class)
public class RemoveServletTest {
    @Test
    public void test() throws ServletException, IOException {
        Store store = MemStore.instOf();
        Candidate candidate = new Candidate(0, "remove", 1);
        store.save(candidate);

        PowerMockito.mockStatic(PsqlStore.class);
        Mockito.when(PsqlStore.instOf()).thenReturn(store);

        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getParameter("id")).thenReturn(String.valueOf(candidate.getId()));
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

        new RemoveServlet().doGet(request, response);
        assertThat(store.findCandidateById(candidate.getId()), CoreMatchers.nullValue());
    }
}