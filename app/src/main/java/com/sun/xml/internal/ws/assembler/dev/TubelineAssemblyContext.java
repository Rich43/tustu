package com.sun.xml.internal.ws.assembler.dev;

import com.sun.xml.internal.ws.api.pipe.Pipe;
import com.sun.xml.internal.ws.api.pipe.Tube;

/* loaded from: rt.jar:com/sun/xml/internal/ws/assembler/dev/TubelineAssemblyContext.class */
public interface TubelineAssemblyContext {
    Pipe getAdaptedTubelineHead();

    <T> T getImplementation(Class<T> cls);

    Tube getTubelineHead();
}
