package com.sun.xml.internal.ws.transport.http;

import com.oracle.webservices.internal.api.message.BasePropertySet;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.api.server.WebServiceContextDelegate;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* loaded from: rt.jar:com/sun/xml/internal/ws/transport/http/WSHTTPConnection.class */
public abstract class WSHTTPConnection extends BasePropertySet {
    public static final int OK = 200;
    public static final int ONEWAY = 202;
    public static final int UNSUPPORTED_MEDIA = 415;
    public static final int MALFORMED_XML = 400;
    public static final int INTERNAL_ERR = 500;
    private volatile boolean closed;

    public abstract void setResponseHeaders(@NotNull Map<String, List<String>> map);

    public abstract void setResponseHeader(String str, List<String> list);

    public abstract void setContentTypeResponseHeader(@NotNull String str);

    public abstract void setStatus(int i2);

    public abstract int getStatus();

    @NotNull
    public abstract InputStream getInput() throws IOException;

    @NotNull
    public abstract OutputStream getOutput() throws IOException;

    @NotNull
    public abstract WebServiceContextDelegate getWebServiceContextDelegate();

    @NotNull
    public abstract String getRequestMethod();

    @NotNull
    public abstract Map<String, List<String>> getRequestHeaders();

    @NotNull
    public abstract Set<String> getRequestHeaderNames();

    public abstract Map<String, List<String>> getResponseHeaders();

    @Nullable
    public abstract String getRequestHeader(@NotNull String str);

    @Nullable
    public abstract List<String> getRequestHeaderValues(@NotNull String str);

    @Nullable
    public abstract String getQueryString();

    @Nullable
    public abstract String getPathInfo();

    @NotNull
    public abstract String getRequestURI();

    @NotNull
    public abstract String getRequestScheme();

    @NotNull
    public abstract String getServerName();

    public abstract int getServerPort();

    public abstract boolean isSecure();

    public void setResponseHeader(String key, String value) {
        setResponseHeader(key, Collections.singletonList(value));
    }

    @NotNull
    public String getContextPath() {
        return "";
    }

    public Object getContext() {
        return null;
    }

    @NotNull
    public String getBaseAddress() {
        throw new UnsupportedOperationException();
    }

    public Principal getUserPrincipal() {
        return null;
    }

    public boolean isUserInRole(String role) {
        return false;
    }

    public Object getRequestAttribute(String key) {
        return null;
    }

    public void close() {
        this.closed = true;
    }

    public boolean isClosed() {
        return this.closed;
    }

    public String getProtocol() {
        return "HTTP/1.1";
    }

    public String getCookie(String name) {
        return null;
    }

    public void setCookie(String name, String value) {
    }

    public void setContentLengthResponseHeader(int value) {
    }
}
