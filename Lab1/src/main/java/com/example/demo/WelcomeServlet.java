package com.example.demo;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/welcome")
public class WelcomeServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<form action=\"controller\" method=\"post\">");
        out.println("<label>Select a value:</label><br>");
        out.println("<input type=\"radio\" name=\"value\" value=\"1\"> 1<br>");
        out.println("<input type=\"radio\" name=\"value\" value=\"2\"> 2<br>");
        out.println("<br>");
        out.println("<button type=\"submit\">Submit</button>");
        out.println("</form>");
        out.println("</body></html>");
    }

    public void destroy() {
    }
}
