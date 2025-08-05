package com.sun.org.apache.xerces.internal.jaxp.validation;

import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/jaxp/validation/XSGrammarPoolContainer.class */
public interface XSGrammarPoolContainer {
    XMLGrammarPool getGrammarPool();

    boolean isFullyComposed();

    Boolean getFeature(String str);

    void setFeature(String str, boolean z2);

    Object getProperty(String str);

    void setProperty(String str, Object obj);
}
