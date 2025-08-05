package com.sun.xml.internal.ws.api;

import com.sun.istack.internal.Nullable;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Iterator;
import javax.xml.ws.WebServiceException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/EndpointAddress.class */
public final class EndpointAddress {

    @Nullable
    private URL url;
    private final URI uri;
    private final String stringForm;
    private volatile boolean dontUseProxyMethod;
    private Proxy proxy;

    public EndpointAddress(URI uri) {
        this.uri = uri;
        this.stringForm = uri.toString();
        try {
            initURL();
            this.proxy = chooseProxy();
        } catch (MalformedURLException e2) {
        }
    }

    public EndpointAddress(String url) throws URISyntaxException {
        this.uri = new URI(url);
        this.stringForm = url;
        try {
            initURL();
            this.proxy = chooseProxy();
        } catch (MalformedURLException e2) {
        }
    }

    private void initURL() throws MalformedURLException {
        String scheme = this.uri.getScheme();
        if (scheme == null) {
            this.url = new URL(this.uri.toString());
            return;
        }
        String scheme2 = scheme.toLowerCase();
        if ("http".equals(scheme2) || "https".equals(scheme2)) {
            this.url = new URL(this.uri.toASCIIString());
        } else {
            this.url = this.uri.toURL();
        }
    }

    public static EndpointAddress create(String url) {
        try {
            return new EndpointAddress(url);
        } catch (URISyntaxException e2) {
            throw new WebServiceException("Illegal endpoint address: " + url, e2);
        }
    }

    private Proxy chooseProxy() {
        ProxySelector sel = (ProxySelector) AccessController.doPrivileged(new PrivilegedAction<ProxySelector>() { // from class: com.sun.xml.internal.ws.api.EndpointAddress.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public ProxySelector run() {
                return ProxySelector.getDefault();
            }
        });
        if (sel == null) {
            return Proxy.NO_PROXY;
        }
        if (!sel.getClass().getName().equals("sun.net.spi.DefaultProxySelector")) {
            return null;
        }
        Iterator<Proxy> it = sel.select(this.uri).iterator();
        if (it.hasNext()) {
            return it.next();
        }
        return Proxy.NO_PROXY;
    }

    public URL getURL() {
        return this.url;
    }

    public URI getURI() {
        return this.uri;
    }

    public URLConnection openConnection() throws IOException {
        if (this.url == null) {
            throw new WebServiceException("URI=" + ((Object) this.uri) + " doesn't have the corresponding URL");
        }
        if (this.proxy != null && !this.dontUseProxyMethod) {
            try {
                return this.url.openConnection(this.proxy);
            } catch (UnsupportedOperationException e2) {
                this.dontUseProxyMethod = true;
            }
        }
        return this.url.openConnection();
    }

    public String toString() {
        return this.stringForm;
    }
}
