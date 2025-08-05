package com.sun.xml.internal.bind.v2.runtime.reflect;

import javax.xml.bind.JAXBException;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/reflect/ListIterator.class */
public interface ListIterator<E> {
    boolean hasNext();

    E next() throws SAXException, JAXBException;
}
