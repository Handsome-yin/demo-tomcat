package com.ycc.demo.tomcat.servlet;

import com.ycc.demo.tomcat.http.Request;
import com.ycc.demo.tomcat.http.Servlet;
import com.ycc.demo.tomcat.http.Response;

public class secondServlet extends Servlet {
    public void doGet(Request request, Response response) throws Exception {
        doPost(request, response);
    }

    public void doPost(Request request, Response response) throws Exception {
        response.write("secondServlet//secondServlet");
    }
}
