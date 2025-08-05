package com.sun.xml.internal.ws.assembler;

import com.sun.istack.internal.logging.Logger;
import com.sun.xml.internal.ws.api.pipe.Pipe;
import com.sun.xml.internal.ws.api.pipe.Tube;
import com.sun.xml.internal.ws.api.pipe.helper.PipeAdapter;
import com.sun.xml.internal.ws.assembler.dev.TubelineAssemblyContext;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

/* loaded from: rt.jar:com/sun/xml/internal/ws/assembler/TubelineAssemblyContextImpl.class */
class TubelineAssemblyContextImpl implements TubelineAssemblyContext {
    private static final Logger LOGGER = Logger.getLogger(TubelineAssemblyContextImpl.class);
    private Tube head;
    private Pipe adaptedHead;
    private List<Tube> tubes = new LinkedList();

    TubelineAssemblyContextImpl() {
    }

    @Override // com.sun.xml.internal.ws.assembler.dev.TubelineAssemblyContext
    public Tube getTubelineHead() {
        return this.head;
    }

    @Override // com.sun.xml.internal.ws.assembler.dev.TubelineAssemblyContext
    public Pipe getAdaptedTubelineHead() {
        if (this.adaptedHead == null) {
            this.adaptedHead = PipeAdapter.adapt(this.head);
        }
        return this.adaptedHead;
    }

    boolean setTubelineHead(Tube newHead) {
        if (newHead == this.head || newHead == this.adaptedHead) {
            return false;
        }
        this.head = newHead;
        this.tubes.add(this.head);
        this.adaptedHead = null;
        if (LOGGER.isLoggable(Level.FINER)) {
            Logger logger = LOGGER;
            Object[] objArr = new Object[1];
            objArr[0] = newHead == null ? null : newHead.getClass().getName();
            logger.finer(MessageFormat.format("Added '{0}' tube instance to the tubeline.", objArr));
            return true;
        }
        return true;
    }

    @Override // com.sun.xml.internal.ws.assembler.dev.TubelineAssemblyContext
    public <T> T getImplementation(Class<T> type) {
        for (Tube tube : this.tubes) {
            if (type.isInstance(tube)) {
                return type.cast(tube);
            }
        }
        return null;
    }
}
