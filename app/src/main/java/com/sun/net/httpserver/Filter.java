package com.sun.net.httpserver;

import java.io.IOException;
import java.util.List;
import java.util.ListIterator;
import jdk.Exported;

@Exported
/* loaded from: rt.jar:com/sun/net/httpserver/Filter.class */
public abstract class Filter {
    public abstract void doFilter(HttpExchange httpExchange, Chain chain) throws IOException;

    public abstract String description();

    protected Filter() {
    }

    @Exported
    /* loaded from: rt.jar:com/sun/net/httpserver/Filter$Chain.class */
    public static class Chain {
        private ListIterator<Filter> iter;
        private HttpHandler handler;

        public Chain(List<Filter> list, HttpHandler httpHandler) {
            this.iter = list.listIterator();
            this.handler = httpHandler;
        }

        public void doFilter(HttpExchange httpExchange) throws IOException {
            if (!this.iter.hasNext()) {
                this.handler.handle(httpExchange);
            } else {
                this.iter.next().doFilter(httpExchange, this);
            }
        }
    }
}
