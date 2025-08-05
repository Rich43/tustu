package com.sun.org.apache.xerces.internal.xni.parser;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xni/parser/XMLComponent.class */
public interface XMLComponent {
    void reset(XMLComponentManager xMLComponentManager) throws XMLConfigurationException;

    String[] getRecognizedFeatures();

    void setFeature(String str, boolean z2) throws XMLConfigurationException;

    String[] getRecognizedProperties();

    void setProperty(String str, Object obj) throws XMLConfigurationException;

    Boolean getFeatureDefault(String str);

    Object getPropertyDefault(String str);
}
