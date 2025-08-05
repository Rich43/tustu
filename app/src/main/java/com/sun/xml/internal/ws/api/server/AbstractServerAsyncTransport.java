package com.sun.xml.internal.ws.api.server;

import com.oracle.webservices.internal.api.message.PropertySet;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.pipe.Codec;
import com.sun.xml.internal.ws.api.server.WSEndpoint;
import com.sun.xml.internal.ws.util.Pool;
import java.io.IOException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/server/AbstractServerAsyncTransport.class */
public abstract class AbstractServerAsyncTransport<T> {
    private final WSEndpoint endpoint;
    private final CodecPool codecPool;

    protected abstract void encodePacket(T t2, @NotNull Packet packet, @NotNull Codec codec) throws IOException;

    @Nullable
    protected abstract String getAcceptableMimeTypes(T t2);

    @Nullable
    protected abstract TransportBackChannel getTransportBackChannel(T t2);

    @NotNull
    protected abstract PropertySet getPropertySet(T t2);

    @NotNull
    protected abstract WebServiceContextDelegate getWebServiceContextDelegate(T t2);

    public AbstractServerAsyncTransport(WSEndpoint endpoint) {
        this.endpoint = endpoint;
        this.codecPool = new CodecPool(endpoint);
    }

    protected Packet decodePacket(T connection, @NotNull Codec codec) throws IOException {
        Packet packet = new Packet();
        packet.acceptableMimeTypes = getAcceptableMimeTypes(connection);
        packet.addSatellite(getPropertySet(connection));
        packet.transportBackChannel = getTransportBackChannel(connection);
        return packet;
    }

    protected void handle(final T connection) throws IOException {
        final Codec codec = this.codecPool.take();
        Packet request = decodePacket(connection, codec);
        if (!request.getMessage().isFault()) {
            this.endpoint.schedule(request, new WSEndpoint.CompletionCallback() { // from class: com.sun.xml.internal.ws.api.server.AbstractServerAsyncTransport.1
                /* JADX WARN: Multi-variable type inference failed */
                @Override // com.sun.xml.internal.ws.api.server.WSEndpoint.CompletionCallback
                public void onCompletion(@NotNull Packet response) {
                    try {
                        AbstractServerAsyncTransport.this.encodePacket(connection, response, codec);
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                    AbstractServerAsyncTransport.this.codecPool.recycle(codec);
                }
            });
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/api/server/AbstractServerAsyncTransport$CodecPool.class */
    private static final class CodecPool extends Pool<Codec> {
        WSEndpoint endpoint;

        CodecPool(WSEndpoint endpoint) {
            this.endpoint = endpoint;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.sun.xml.internal.ws.util.Pool
        public Codec create() {
            return this.endpoint.createCodec();
        }
    }
}
