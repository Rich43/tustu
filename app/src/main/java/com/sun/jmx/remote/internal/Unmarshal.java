package com.sun.jmx.remote.internal;

import java.io.IOException;
import java.rmi.MarshalledObject;

/* loaded from: rt.jar:com/sun/jmx/remote/internal/Unmarshal.class */
public interface Unmarshal {
    Object get(MarshalledObject<?> marshalledObject) throws IOException, ClassNotFoundException;
}
