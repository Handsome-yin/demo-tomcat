package com.ycc.demo.tomcat;

import com.ycc.demo.tomcat.http.Request;
import com.ycc.demo.tomcat.http.Response;
import com.ycc.demo.tomcat.http.Servlet;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Properties;

public class Tomcat {

    private final int port;

    private Properties webxml = new Properties();
    private HashMap<String, Servlet> servletMapping = new HashMap();

    public Tomcat(int port) {
        this.port = port;
    }

    //1、配置好启动端口，默认8080  ServerSocket  IP:localhost
    //2、配置web.xml 自己写的Servlet继承HttpServlet
    //   servlet-name
    //   servlet-class
    //   url-pattern
    //3、读取配置，url-pattern  和 Servlet建立一个映射关系
    //   Map servletMapping
    private void init() {
        try {
            //加载web.xml文件,同时初始化 ServletMapping对象
            String WEB_INF = this.getClass().getResource("/").getPath();
            FileInputStream fis = new FileInputStream(WEB_INF + "web.properties");

            webxml.load(fis);
            for (Object k : webxml.keySet()) {

                String key = k.toString();
                if (key.endsWith(".url")) {
                    String servletName = key.replaceAll("\\.url$", "");
                    String url = webxml.getProperty(key);
                    String className = webxml.getProperty(servletName + ".className");
                    //单实例，多线程
                    Servlet obj = (Servlet) Class.forName(className).newInstance();
                    servletMapping.put(url, obj);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() {
        try {
            init();
            ServerSocket serverSocket = new ServerSocket(this.port);

            System.out.println(" tomcat 已启动；端口号： " + this.port);

            while (true) {
                Socket accept = serverSocket.accept();
                procss(accept);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void procss(Socket accept) {
        try {
            InputStream is = accept.getInputStream();
            OutputStream os = accept.getOutputStream();
            Request request = new Request(is);
            Response response = new Response(os);
            String url = request.getUrl();
            Servlet servlet = servletMapping.get(url);
            if(servlet!=null){
                servlet.service(request, response);
            }else {
                response.write("404 - Not Found");
            }
            os.flush();
            os.close();

            is.close();
            accept.close();

        } catch (Exception e) {

        }
    }

    public static void main(String[] args) {
        new Tomcat(8080).start();
    }
}
