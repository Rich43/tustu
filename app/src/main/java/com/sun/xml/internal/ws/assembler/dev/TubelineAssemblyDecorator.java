package com.sun.xml.internal.ws.assembler.dev;

import com.sun.xml.internal.ws.api.pipe.Tube;
import java.util.ArrayList;
import java.util.Collection;

/* loaded from: rt.jar:com/sun/xml/internal/ws/assembler/dev/TubelineAssemblyDecorator.class */
public class TubelineAssemblyDecorator {
    public static TubelineAssemblyDecorator composite(Iterable<TubelineAssemblyDecorator> decorators) {
        return new CompositeTubelineAssemblyDecorator(decorators);
    }

    public Tube decorateClient(Tube tube, ClientTubelineAssemblyContext context) {
        return tube;
    }

    public Tube decorateClientHead(Tube tube, ClientTubelineAssemblyContext context) {
        return tube;
    }

    public Tube decorateClientTail(Tube tube, ClientTubelineAssemblyContext context) {
        return tube;
    }

    public Tube decorateServer(Tube tube, ServerTubelineAssemblyContext context) {
        return tube;
    }

    public Tube decorateServerTail(Tube tube, ServerTubelineAssemblyContext context) {
        return tube;
    }

    public Tube decorateServerHead(Tube tube, ServerTubelineAssemblyContext context) {
        return tube;
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/assembler/dev/TubelineAssemblyDecorator$CompositeTubelineAssemblyDecorator.class */
    private static class CompositeTubelineAssemblyDecorator extends TubelineAssemblyDecorator {
        private Collection<TubelineAssemblyDecorator> decorators = new ArrayList();

        public CompositeTubelineAssemblyDecorator(Iterable<TubelineAssemblyDecorator> decorators) {
            for (TubelineAssemblyDecorator decorator : decorators) {
                this.decorators.add(decorator);
            }
        }

        @Override // com.sun.xml.internal.ws.assembler.dev.TubelineAssemblyDecorator
        public Tube decorateClient(Tube tube, ClientTubelineAssemblyContext context) {
            for (TubelineAssemblyDecorator decorator : this.decorators) {
                tube = decorator.decorateClient(tube, context);
            }
            return tube;
        }

        @Override // com.sun.xml.internal.ws.assembler.dev.TubelineAssemblyDecorator
        public Tube decorateClientHead(Tube tube, ClientTubelineAssemblyContext context) {
            for (TubelineAssemblyDecorator decorator : this.decorators) {
                tube = decorator.decorateClientHead(tube, context);
            }
            return tube;
        }

        @Override // com.sun.xml.internal.ws.assembler.dev.TubelineAssemblyDecorator
        public Tube decorateClientTail(Tube tube, ClientTubelineAssemblyContext context) {
            for (TubelineAssemblyDecorator decorator : this.decorators) {
                tube = decorator.decorateClientTail(tube, context);
            }
            return tube;
        }

        @Override // com.sun.xml.internal.ws.assembler.dev.TubelineAssemblyDecorator
        public Tube decorateServer(Tube tube, ServerTubelineAssemblyContext context) {
            for (TubelineAssemblyDecorator decorator : this.decorators) {
                tube = decorator.decorateServer(tube, context);
            }
            return tube;
        }

        @Override // com.sun.xml.internal.ws.assembler.dev.TubelineAssemblyDecorator
        public Tube decorateServerTail(Tube tube, ServerTubelineAssemblyContext context) {
            for (TubelineAssemblyDecorator decorator : this.decorators) {
                tube = decorator.decorateServerTail(tube, context);
            }
            return tube;
        }

        @Override // com.sun.xml.internal.ws.assembler.dev.TubelineAssemblyDecorator
        public Tube decorateServerHead(Tube tube, ServerTubelineAssemblyContext context) {
            for (TubelineAssemblyDecorator decorator : this.decorators) {
                tube = decorator.decorateServerHead(tube, context);
            }
            return tube;
        }
    }
}
