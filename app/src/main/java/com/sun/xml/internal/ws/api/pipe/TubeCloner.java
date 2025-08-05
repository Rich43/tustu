package com.sun.xml.internal.ws.api.pipe;

import java.util.Map;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/pipe/TubeCloner.class */
public abstract class TubeCloner {
    public final Map<Object, Object> master2copy;

    public abstract <T extends Tube> T copy(T t2);

    public abstract void add(Tube tube, Tube tube2);

    public static Tube clone(Tube p2) {
        return new PipeClonerImpl().copy((PipeClonerImpl) p2);
    }

    TubeCloner(Map<Object, Object> master2copy) {
        this.master2copy = master2copy;
    }
}
