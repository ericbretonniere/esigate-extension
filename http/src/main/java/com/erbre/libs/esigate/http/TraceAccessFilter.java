package com.erbre.libs.esigate.http;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TraceAccessFilter implements Filter {

    private static final Logger LOGGER = Logger.getLogger(TraceAccessFilter.class.getName());

    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        StringBuilder builder = new StringBuilder();
        long nano = System.nanoTime();
        // builder.append("\nRemote\t
        // [").append(httpRequest.getRemoteAddr()).append("] [")
        // .append(httpRequest.getRemoteHost()).append(':').append(httpRequest.getRemotePort()).append(']');
        // builder.append("\nLocal\t
        // [").append(httpRequest.getLocalAddr()).append("] [")
        // .append(httpRequest.getLocalName()).append(':').append(httpRequest.getLocalPort()).append(']');
        builder.append("\nRequest\t [").append(httpRequest.getMethod()).append("][").append(httpRequest.getProtocol())
                .append("] URL [").append(httpRequest.getRequestURL()).append("] URI [")
                .append(httpRequest.getRequestURI()).append("] ContentType [").append(httpRequest.getContentType())
                .append(']');

        LOGGER.info(builder.toString());

        chain.doFilter(request, response);

        StringBuilder builder2 = new StringBuilder();
        builder2.append("\nResponse [").append(httpResponse.getStatus()).append("] ContentType [")
                .append(httpResponse.getContentType()).append("] time [").append((System.nanoTime() - nano))
                .append("] ns from ").append(builder.toString());

        LOGGER.info(builder2.toString());

    }

    public void destroy() {
        // TODO Auto-generated method stub

    }

}
