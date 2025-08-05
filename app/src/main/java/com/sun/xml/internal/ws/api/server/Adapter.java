package com.sun.xml.internal.ws.api.server;

import com.sun.xml.internal.ws.api.Component;
import com.sun.xml.internal.ws.api.config.management.Reconfigurable;
import com.sun.xml.internal.ws.api.pipe.Codec;
import com.sun.xml.internal.ws.api.server.Adapter.Toolkit;
import com.sun.xml.internal.ws.api.server.WSEndpoint;
import com.sun.xml.internal.ws.util.Pool;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/server/Adapter.class */
public abstract class Adapter<TK extends Toolkit> implements Reconfigurable, Component {
    protected final WSEndpoint<?> endpoint;
    protected volatile Pool<TK> pool = (Pool<TK>) new Pool<TK>() { // from class: com.sun.xml.internal.ws.api.server.Adapter.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.sun.xml.internal.ws.util.Pool
        public TK create() {
            return (TK) Adapter.this.createToolkit();
        }
    };
    static final /* synthetic */ boolean $assertionsDisabled;

    protected abstract TK createToolkit();

    static {
        $assertionsDisabled = !Adapter.class.desiredAssertionStatus();
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/api/server/Adapter$Toolkit.class */
    public class Toolkit {
        public final Codec codec;
        public final WSEndpoint.PipeHead head;

        public Toolkit() {
            this.codec = Adapter.this.endpoint.createCodec();
            this.head = Adapter.this.endpoint.createPipeHead();
        }
    }

    protected Adapter(WSEndpoint wSEndpoint) {
        if (!$assertionsDisabled && wSEndpoint == null) {
            throw new AssertionError();
        }
        this.endpoint = wSEndpoint;
        wSEndpoint.getComponents().add(getEndpointComponent());
    }

    protected Component getEndpointComponent() {
        return new Component() { // from class: com.sun.xml.internal.ws.api.server.Adapter.2
            @Override // com.sun.xml.internal.ws.api.Component
            public <S> S getSPI(Class<S> spiType) {
                if (spiType.isAssignableFrom(Reconfigurable.class)) {
                    return spiType.cast(Adapter.this);
                }
                return null;
            }
        };
    }

    @Override // com.sun.xml.internal.ws.api.config.management.Reconfigurable
    public void reconfigure() {
        this.pool = (Pool<TK>) new Pool<TK>() { // from class: com.sun.xml.internal.ws.api.server.Adapter.3
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.sun.xml.internal.ws.util.Pool
            public TK create() {
                return (TK) Adapter.this.createToolkit();
            }
        };
    }

    @Override // com.sun.xml.internal.ws.api.Component
    public <S> S getSPI(Class<S> cls) {
        if (cls.isAssignableFrom(Reconfigurable.class)) {
            return cls.cast(this);
        }
        if (this.endpoint != null) {
            return (S) this.endpoint.getSPI(cls);
        }
        return null;
    }

    public WSEndpoint<?> getEndpoint() {
        return this.endpoint;
    }

    protected Pool<TK> getPool() {
        return this.pool;
    }
}
