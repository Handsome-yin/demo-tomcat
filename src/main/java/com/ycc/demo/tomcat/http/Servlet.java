package com.ycc.demo.tomcat.http;

public abstract class Servlet {

    public void service(Request request, Response response) throws Exception {

        if ("GET".equalsIgnoreCase(request.getMethodName())) {
            doGet(request, response);
        } else {
            doPost(request, response);
        }
    }

    public abstract void doGet(Request request, Response response) throws Exception;

    public abstract void doPost(Request request, Response response) throws Exception;
}
