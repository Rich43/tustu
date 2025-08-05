package com.sun.org.apache.xerces.internal.impl.dtd;

import com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentFilter;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/dtd/XMLDTDValidatorFilter.class */
public interface XMLDTDValidatorFilter extends XMLDocumentFilter {
    boolean hasGrammar();

    boolean validate();
}
