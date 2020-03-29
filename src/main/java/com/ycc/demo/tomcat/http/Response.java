package com.ycc.demo.tomcat.http;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public class Response {
    private OutputStream out;

    public Response(OutputStream out) {
        this.out = out;
    }

    private ChannelHandlerContext ctx;

    private HttpRequest req;

    public Response(ChannelHandlerContext ctx, HttpRequest req) {
        this.ctx = ctx;
        this.req = req;
    }

    public void write(String s) throws UnsupportedEncodingException {
        if (s == null || s.length() == 0) {
            return;
        }
        if (req != null) {
            // 设置 http协议及请求头信息
            FullHttpResponse response = new DefaultFullHttpResponse(
                    // 设置http版本为1.1
                    HttpVersion.HTTP_1_1,
                    // 设置响应状态码
                    HttpResponseStatus.OK,
                    // 将输出值写出 编码为UTF-8
                    Unpooled.wrappedBuffer(s.getBytes("UTF-8")));

            response.headers().set("Content-Type", "text/html;");
            // 当前是否支持长连接
//            if (HttpUtil.isKeepAlive(r)) {
//                // 设置连接内容为长连接
//                response.headers().set(CONNECTION, HttpHeaderValues.KEEP_ALIVE);
//            }
            ctx.write(response);
            ctx.flush();
            ctx.close();
        } else {
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
}
