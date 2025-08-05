package com.sun.org.apache.xpath.internal.objects;

import com.sun.org.apache.xml.internal.utils.WrappedRuntimeException;
import javax.xml.transform.TransformerException;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/objects/XBoolean.class */
public class XBoolean extends XObject {
    static final long serialVersionUID = -2964933058866100881L;
    public static final XBoolean S_TRUE = new XBooleanStatic(true);
    public static final XBoolean S_FALSE = new XBooleanStatic(false);
    private final boolean m_val;

    public XBoolean(boolean b2) {
        this.m_val = b2;
    }

    public XBoolean(Boolean b2) {
        this.m_val = b2.booleanValue();
        setObject(b2);
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject
    public int getType() {
        return 1;
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject
    public String getTypeString() {
        return "#BOOLEAN";
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject
    public double num() {
        return this.m_val ? 1.0d : 0.0d;
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject
    public boolean bool() {
        return this.m_val;
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject
    public String str() {
        return this.m_val ? "true" : "false";
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject
    public Object object() {
        if (null == this.m_obj) {
            setObject(new Boolean(this.m_val));
        }
        return this.m_obj;
    }

    @Override // com.sun.org.apache.xpath.internal.objects.XObject
    public boolean equals(XObject obj2) {
        if (obj2.getType() == 4) {
            return obj2.equals((XObject) this);
        }
        try {
            return this.m_val == obj2.bool();
        } catch (TransformerException te) {
            throw new WrappedRuntimeException(te);
        }
    }
}
