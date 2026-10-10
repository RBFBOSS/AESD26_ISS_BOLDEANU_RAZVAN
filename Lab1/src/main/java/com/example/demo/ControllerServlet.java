package com.example.demo;

import java.io.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet("/controller")
public class ControllerServlet extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        System.out.println("Method: " + request.getMethod());
        System.out.println("IP: " + request.getRemoteAddr());
        System.out.println("User-Agent: " + request.getHeader("User-Agent"));
        System.out.println("Languages: " + request.getHeader("Accept-Language"));
        System.out.println("Parameter value: " + request.getParameter("value"));
        response.setContentType("text/html");
        String selection = request.getParameter("value");
        if (selection.equals("1")){
            request.getRequestDispatcher("/page1.html")
                    .forward(request, response);
        }
        else if (selection.equals("2")){
            request.getRequestDispatcher("/page2.html")
                    .forward(request, response);
        }
        else{
            request.getRequestDispatcher("/eroare.html")
                    .forward(request, response);
        }
    }

    public void destroy() {
    }
}
