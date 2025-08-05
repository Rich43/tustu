package com.sun.xml.internal.bind;

import javax.xml.bind.Marshaller;

/* loaded from: rt.jar:com/sun/xml/internal/bind/CycleRecoverable.class */
public interface CycleRecoverable {

    /* loaded from: rt.jar:com/sun/xml/internal/bind/CycleRecoverable$Context.class */
    public interface Context {
        Marshaller getMarshaller();
    }

    Object onCycleDetected(Context context);
}
