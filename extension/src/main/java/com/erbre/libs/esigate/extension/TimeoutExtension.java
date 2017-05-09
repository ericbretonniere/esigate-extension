package com.erbre.libs.esigate.extension;

import java.util.Map.Entry;
import java.util.Properties;

import org.apache.http.client.config.RequestConfig;
import org.esigate.Driver;
import org.esigate.events.Event;
import org.esigate.events.EventDefinition;
import org.esigate.events.EventManager;
import org.esigate.events.IEventListener;
import org.esigate.events.impl.FetchEvent;
import org.esigate.extension.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeoutExtension implements Extension, IEventListener {
    private static final Logger LOG = LoggerFactory.getLogger(TimeoutExtension.class);
    private static final String URL_PATTERN_PROPERTY_NAME = "specialSocketTimeoutPattern";
    private static final String TIMEOUT_PROPERTY_NAME = "specialSocketTimeout";
    private static final int DEFAULT_TIMEOUT = 5000;
    private int timeout;
    private String urlPattern;

    @Override
    public void init(Driver driver, Properties properties) {
        driver.getEventManager().register(EventManager.EVENT_FETCH_PRE, this);
        urlPattern = properties.getProperty(URL_PATTERN_PROPERTY_NAME);
        final String timeoutStr = properties.getProperty(TIMEOUT_PROPERTY_NAME);
        if (!(timeoutStr == null || timeoutStr.isEmpty())) {
            try {
                timeout = Integer.parseInt(timeoutStr);
            } catch (NumberFormatException nfex) {
                timeout = DEFAULT_TIMEOUT;
            }
        } else {
            timeout = DEFAULT_TIMEOUT;
        }

        for (Entry<?, ?> entry : properties.entrySet()) {
            LOG.info("Property [" + entry.getKey() + "][" + entry.getValue() + "]");
        }

        LOG.info("Timeout set to [" + timeout + "] for url pattern [" + urlPattern + "]");
    }

    @Override
    public boolean event(EventDefinition id, Event event) {

        FetchEvent e = (FetchEvent) event;
        if (EventManager.EVENT_FETCH_PRE.equals(id)) {
            String uri = e.getHttpRequest().getURI().toString();
            if (uri.contains(urlPattern)) {
                final RequestConfig reqCfg = RequestConfig.custom().setConnectTimeout(timeout).setSocketTimeout(timeout)
                        .build();
                LOG.info("Timeout set to [" + timeout + "] for url [" + uri + "]");
                e.getHttpContext().setRequestConfig(reqCfg);
            } else {
                LOG.info("No Timeout set for url [" + uri + "]");
            }
        }
        return true;
    }

}
