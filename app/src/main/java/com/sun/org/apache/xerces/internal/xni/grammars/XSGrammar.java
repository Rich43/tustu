package com.sun.org.apache.xerces.internal.xni.grammars;

import com.sun.org.apache.xerces.internal.xs.XSModel;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xni/grammars/XSGrammar.class */
public interface XSGrammar extends Grammar {
    XSModel toXSModel();

    XSModel toXSModel(XSGrammar[] xSGrammarArr);
}
