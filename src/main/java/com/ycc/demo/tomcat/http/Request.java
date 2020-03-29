package com.ycc.demo.tomcat.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class Request {

    private String method;
    private String url;

    private ChannelHandlerContext ctx;

    private HttpRequest req;

    public Request(InputStream in) {
        try {
            //拿到HTTP协议内容
            String content = "";
            byte[] buff = new byte[1024];
            int len = 0;
            if ((len = in.read(buff)) > 0) {
                content = new String(buff, 0, len);
            }

            String line = content.split("\\n")[0];
            String[] arr = line.split("\\s");

            this.method = arr[0];
            this.url = arr[1];
            System.out.println(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Request(ChannelHandlerContext ctx, HttpRequest req) {
        this.ctx = ctx;
        this.req = req;
        url = req.uri();
        method = req.method().name();
    }

    public String getMethodName() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, List<String>> getParameters() {
        QueryStringDecoder decoder = new QueryStringDecoder(req.uri());
        return decoder.parameters();
    }

    public String getParameter(String name) {
        Map<String, List<String>> params = getParameters();
        List<String> param = params.get(name);
        if (null == param) {
            return null;
        } else {
            return param.get(0);
        }
    }
}
