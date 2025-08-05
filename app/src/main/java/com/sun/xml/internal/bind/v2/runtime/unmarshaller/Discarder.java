package com.sun.xml.internal.bind.v2.runtime.unmarshaller;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallingContext;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/unmarshaller/Discarder.class */
public final class Discarder extends Loader {
    public static final Loader INSTANCE = new Discarder();

    private Discarder() {
        super(false);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader
    public void childElement(UnmarshallingContext.State state, TagName ea) {
        state.setTarget(null);
        state.setLoader(this);
    }
}
