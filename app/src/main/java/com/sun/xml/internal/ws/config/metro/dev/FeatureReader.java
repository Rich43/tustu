package com.sun.xml.internal.ws.config.metro.dev;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;
import jdk.jfr.Enabled;

/* loaded from: rt.jar:com/sun/xml/internal/ws/config/metro/dev/FeatureReader.class */
public interface FeatureReader<T extends WebServiceFeature> {
    public static final QName ENABLED_ATTRIBUTE_NAME = new QName(Enabled.NAME);

    T parse(XMLEventReader xMLEventReader) throws WebServiceException;
}
