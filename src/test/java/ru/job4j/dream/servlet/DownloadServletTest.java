package ru.job4j.dream.servlet;

import org.junit.Test;
import org.mockito.Mockito;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

public class DownloadServletTest {

    @Test
    public void test() throws IOException, ServletException {
        File directory = new File("C:/images");
        if (!directory.exists()) {
            directory.mkdir();
            directory.deleteOnExit();
        }
        File temp = File.createTempFile("tmp", null, new File("C:/images/"));
        temp.deleteOnExit();

        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

        Mockito.when(request.getParameter("id")).thenReturn(temp.getName());
        Mockito.when(response.getOutputStream()).thenReturn(Mockito.mock(ServletOutputStream.class));

        new DownloadServlet().doGet(request, response);
        Mockito.verify(response).getOutputStream();
    }
}