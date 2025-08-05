package com.sun.xml.internal.bind.v2.model.core;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/core/WildcardMode.class */
public enum WildcardMode {
    STRICT(false, true),
    SKIP(true, false),
    LAX(true, true);

    public final boolean allowDom;
    public final boolean allowTypedObject;

    WildcardMode(boolean allowDom, boolean allowTypedObject) {
        this.allowDom = allowDom;
        this.allowTypedObject = allowTypedObject;
    }
}
