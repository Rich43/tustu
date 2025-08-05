package com.sun.xml.internal.ws;

import javax.xml.ws.WebServiceException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/Closeable.class */
public interface Closeable extends java.io.Closeable {
    @Override // java.io.Closeable, java.lang.AutoCloseable
    void close() throws WebServiceException;
}
