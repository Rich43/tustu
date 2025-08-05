package com.sun.org.apache.xerces.internal.xinclude;

import com.sun.org.apache.xerces.internal.xni.parser.XMLComponent;
import com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentFilter;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xinclude/XPointerSchema.class */
public interface XPointerSchema extends XMLComponent, XMLDocumentFilter {
    void setXPointerSchemaName(String str);

    String getXpointerSchemaName();

    void setParent(Object obj);

    Object getParent();

    void setXPointerSchemaPointer(String str);

    String getXPointerSchemaPointer();

    boolean isSubResourceIndentified();

    void reset();
}
