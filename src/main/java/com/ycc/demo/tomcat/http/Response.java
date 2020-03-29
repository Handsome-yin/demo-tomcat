package com.ycc.demo.tomcat.http;

import java.io.IOException;
import java.io.OutputStream;

public class Response {
    private OutputStream out;

    public Response(OutputStream out) {
        this.out = out;
    }

    public void write(String s) {
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.1 200 OK\n")
                .append("Content-Type: text/html;\n")
                .append("\r\n")
                .append(s);
        try {
            out.write(sb.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
