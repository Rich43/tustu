package com.sun.xml.internal.ws.api.pipe;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.api.pipe.helper.PipeAdapter;
import com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe;
import com.sun.xml.internal.ws.util.ServiceFinder;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.ws.WebServiceException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/pipe/TransportTubeFactory.class */
public abstract class TransportTubeFactory {
    private static final TransportTubeFactory DEFAULT = new DefaultTransportTubeFactory();
    private static final Logger logger = Logger.getLogger(TransportTubeFactory.class.getName());

    public abstract Tube doCreate(@NotNull ClientTubeAssemblerContext clientTubeAssemblerContext);

    /* loaded from: rt.jar:com/sun/xml/internal/ws/api/pipe/TransportTubeFactory$DefaultTransportTubeFactory.class */
    private static class DefaultTransportTubeFactory extends TransportTubeFactory {
        private DefaultTransportTubeFactory() {
        }

        @Override // com.sun.xml.internal.ws.api.pipe.TransportTubeFactory
        public Tube doCreate(ClientTubeAssemblerContext context) {
            return createDefault(context);
        }
    }

    public static Tube create(@Nullable ClassLoader classLoader, @NotNull ClientTubeAssemblerContext context) {
        Iterator it = ServiceFinder.find(TransportTubeFactory.class, classLoader, context.getContainer()).iterator();
        while (it.hasNext()) {
            TransportTubeFactory factory = (TransportTubeFactory) it.next();
            Tube tube = factory.doCreate(context);
            if (tube != null) {
                if (logger.isLoggable(Level.FINE)) {
                    logger.log(Level.FINE, "{0} successfully created {1}", new Object[]{factory.getClass(), tube});
                }
                return tube;
            }
        }
        ClientPipeAssemblerContext ctxt = new ClientPipeAssemblerContext(context.getAddress(), context.getWsdlModel(), context.getService(), context.getBinding(), context.getContainer());
        ctxt.setCodec(context.getCodec());
        Iterator it2 = ServiceFinder.find(TransportPipeFactory.class, classLoader).iterator();
        while (it2.hasNext()) {
            TransportPipeFactory factory2 = (TransportPipeFactory) it2.next();
            Pipe pipe = factory2.doCreate(ctxt);
            if (pipe != null) {
                if (logger.isLoggable(Level.FINE)) {
                    logger.log(Level.FINE, "{0} successfully created {1}", new Object[]{factory2.getClass(), pipe});
                }
                return PipeAdapter.adapt(pipe);
            }
        }
        return DEFAULT.createDefault(ctxt);
    }

    protected Tube createDefault(ClientTubeAssemblerContext context) {
        String scheme = context.getAddress().getURI().getScheme();
        if (scheme != null && (scheme.equalsIgnoreCase("http") || scheme.equalsIgnoreCase("https"))) {
            return createHttpTransport(context);
        }
        throw new WebServiceException("Unsupported endpoint address: " + ((Object) context.getAddress()));
    }

    protected Tube createHttpTransport(ClientTubeAssemblerContext context) {
        return new HttpTransportPipe(context.getCodec(), context.getBinding());
    }
}
