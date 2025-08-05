package com.sun.org.apache.xerces.internal.xni.parser;

import com.sun.org.apache.xerces.internal.util.FeatureState;
import com.sun.org.apache.xerces.internal.util.PropertyState;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xni/parser/XMLComponentManager.class */
public interface XMLComponentManager {
    boolean getFeature(String str) throws XMLConfigurationException;

    boolean getFeature(String str, boolean z2);

    Object getProperty(String str) throws XMLConfigurationException;

    Object getProperty(String str, Object obj);

    FeatureState getFeatureState(String str);

    PropertyState getPropertyState(String str);
}
