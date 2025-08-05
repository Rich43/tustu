package com.sun.org.apache.xalan.internal.xsltc.dom;

import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/dom/CurrentNodeListFilter.class */
public interface CurrentNodeListFilter {
    boolean test(int i2, int i3, int i4, int i5, AbstractTranslet abstractTranslet, DTMAxisIterator dTMAxisIterator);
}
