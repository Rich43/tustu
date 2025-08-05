package com.sun.xml.internal.ws.api;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.ws.Service;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/WSDLLocator.class */
public abstract class WSDLLocator {
    public abstract URL locateWSDL(Class<Service> cls, String str) throws MalformedURLException;
}
