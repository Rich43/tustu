package com.sun.xml.internal.ws.api.pipe;

import java.util.Map;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/pipe/PipeCloner.class */
public abstract class PipeCloner extends TubeCloner {
    public abstract <T extends Pipe> T copy(T t2);

    public abstract void add(Pipe pipe, Pipe pipe2);

    public static Pipe clone(Pipe p2) {
        return new PipeClonerImpl().copy((PipeClonerImpl) p2);
    }

    PipeCloner(Map<Object, Object> master2copy) {
        super(master2copy);
    }
}
