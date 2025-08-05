package com.sun.xml.internal.ws.transport.http.server;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.api.server.BoundEndpoint;
import com.sun.xml.internal.ws.api.server.Module;
import com.sun.xml.internal.ws.api.server.WSEndpoint;
import com.sun.xml.internal.ws.api.server.WebModule;
import com.sun.xml.internal.ws.transport.http.HttpAdapter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.ws.WebServiceException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/transport/http/server/ServerAdapter.class */
public final class ServerAdapter extends HttpAdapter implements BoundEndpoint {
    final String name;
    private static final Logger LOGGER = Logger.getLogger(ServerAdapter.class.getName());

    protected ServerAdapter(String name, String urlPattern, WSEndpoint endpoint, ServerAdapterList owner) {
        super(endpoint, owner, urlPattern);
        this.name = name;
        Module module = (Module) endpoint.getContainer().getSPI(Module.class);
        if (module == null) {
            LOGGER.log(Level.WARNING, "Container {0} doesn''t support {1}", new Object[]{endpoint.getContainer(), Module.class});
        } else {
            module.getBoundEndpoints().add(this);
        }
    }

    public String getName() {
        return this.name;
    }

    @Override // com.sun.xml.internal.ws.api.server.BoundEndpoint
    @NotNull
    public URI getAddress() {
        WebModule webModule = (WebModule) this.endpoint.getContainer().getSPI(WebModule.class);
        if (webModule == null) {
            throw new WebServiceException("Container " + ((Object) this.endpoint.getContainer()) + " doesn't support " + ((Object) WebModule.class));
        }
        return getAddress(webModule.getContextPath());
    }

    @Override // com.sun.xml.internal.ws.api.server.BoundEndpoint
    @NotNull
    public URI getAddress(String baseAddress) {
        String adrs = baseAddress + getValidPath();
        try {
            return new URI(adrs);
        } catch (URISyntaxException e2) {
            throw new WebServiceException("Unable to compute address for " + ((Object) this.endpoint), e2);
        }
    }

    public void dispose() {
        this.endpoint.dispose();
    }

    public String getUrlPattern() {
        return this.urlPattern;
    }

    public String toString() {
        return super.toString() + "[name=" + this.name + ']';
    }
}
