package com.sun.xml.internal.bind.v2.runtime.unmarshaller;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/unmarshaller/ChildLoader.class */
public final class ChildLoader {
    public final Loader loader;
    public final Receiver receiver;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ChildLoader.class.desiredAssertionStatus();
    }

    public ChildLoader(Loader loader, Receiver receiver) {
        if (!$assertionsDisabled && loader == null) {
            throw new AssertionError();
        }
        this.loader = loader;
        this.receiver = receiver;
    }
}
