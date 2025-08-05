package com.sun.xml.internal.ws.api.pipe;

import com.sun.xml.internal.ws.api.pipe.helper.AbstractTubeImpl;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/pipe/PipeClonerImpl.class */
public class PipeClonerImpl extends PipeCloner {
    private static final Logger LOGGER;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !PipeClonerImpl.class.desiredAssertionStatus();
        LOGGER = Logger.getLogger(PipeClonerImpl.class.getName());
    }

    public PipeClonerImpl() {
        super(new HashMap());
    }

    protected PipeClonerImpl(Map<Object, Object> master2copy) {
        super(master2copy);
    }

    @Override // com.sun.xml.internal.ws.api.pipe.PipeCloner
    public <T extends Pipe> T copy(T t2) {
        Pipe pipeCopy = (Pipe) this.master2copy.get(t2);
        if (pipeCopy == null) {
            pipeCopy = t2.copy(this);
            if (!$assertionsDisabled && this.master2copy.get(t2) != pipeCopy) {
                throw new AssertionError((Object) ("the pipe must call the add(...) method to register itself before start copying other pipes, but " + ((Object) t2) + " hasn't done so"));
            }
        }
        return (T) pipeCopy;
    }

    @Override // com.sun.xml.internal.ws.api.pipe.PipeCloner
    public void add(Pipe original, Pipe copy) {
        if (!$assertionsDisabled && this.master2copy.containsKey(original)) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && (original == null || copy == null)) {
            throw new AssertionError();
        }
        this.master2copy.put(original, copy);
    }

    public void add(AbstractTubeImpl original, AbstractTubeImpl copy) {
        add((Tube) original, (Tube) copy);
    }

    @Override // com.sun.xml.internal.ws.api.pipe.TubeCloner
    public void add(Tube original, Tube copy) {
        if (!$assertionsDisabled && this.master2copy.containsKey(original)) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && (original == null || copy == null)) {
            throw new AssertionError();
        }
        this.master2copy.put(original, copy);
    }

    @Override // com.sun.xml.internal.ws.api.pipe.TubeCloner
    public <T extends Tube> T copy(T t2) {
        Tube tubeCopy = (Tube) this.master2copy.get(t2);
        if (tubeCopy == null) {
            if (t2 != null) {
                tubeCopy = t2.copy(this);
            } else if (LOGGER.isLoggable(Level.FINER)) {
                LOGGER.fine("WARNING, tube passed to 'copy' in " + ((Object) this) + " was null, so no copy was made");
            }
        }
        return (T) tubeCopy;
    }
}
