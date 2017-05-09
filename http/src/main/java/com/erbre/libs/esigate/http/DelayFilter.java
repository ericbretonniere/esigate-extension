package com.erbre.libs.esigate.http;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class DelayFilter implements Filter {

    private static final Logger LOGGER = Logger.getLogger(DelayFilter.class.getName());

    private static final long DEFAULT_DELAY = 5000l;
    private long delay;

    public void init(FilterConfig filterConfig) throws ServletException {
        String delayValue = filterConfig.getInitParameter("delay");
        if (delayValue == null || delayValue.isEmpty()) {
            delay = DEFAULT_DELAY;
        } else {
            try {
                delay = Long.parseLong(delayValue);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Illegal delay value[" + delayValue + "] for DelayFilter delay init param",
                        e);
            }
        }
        LOGGER.config("Configuration delay is [" + delay + "]ms");

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            LOGGER.info("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz sleeping [" + delay + "] ms");
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            // do nothing
        }
        LOGGER.info("Wake Up!!!!!");
        chain.doFilter(request, response);

    }

    public void destroy() {
        // TODO Auto-generated method stub

    }

}
