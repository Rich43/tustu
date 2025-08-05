package com.sun.org.apache.xpath.internal.objects;

import com.sun.org.apache.xml.internal.utils.WrappedRuntimeException;
import javax.xml.transform.TransformerException;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/objects/XBooleanStatic.class */
public class XBooleanStatic extends XBoolean {
    static final long serialVersionUID = -8064147275772687409L;
    private final boolean m_val;

    public XBooleanStatic(boolean b2) {
        super(b2);
        this.m_val = b2;
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XBoolean, com.sun.org.apache.xpath.internal.objects.XObject
    public boolean equals(XObject obj2) {
        try {
            return this.m_val == obj2.bool();
        } catch (TransformerException te) {
            throw new WrappedRuntimeException(te);
        }
    }
}
