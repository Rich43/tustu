package com.sun.xml.internal.bind.v2.model.core;

import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/core/MaybeElement.class */
public interface MaybeElement<T, C> extends NonElement<T, C> {
    boolean isElement();

    QName getElementName();

    Element<T, C> asElement();
}
