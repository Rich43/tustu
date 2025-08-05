package com.sun.org.apache.xpath.internal.axes;

import com.sun.org.apache.xml.internal.dtm.DTMIterator;
import com.sun.org.apache.xml.internal.utils.WrappedRuntimeException;
import java.io.Serializable;
import java.util.ArrayList;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/axes/IteratorPool.class */
public final class IteratorPool implements Serializable {
    static final long serialVersionUID = -460927331149566998L;
    private final DTMIterator m_orig;
    private final ArrayList m_freeStack = new ArrayList();

    public IteratorPool(DTMIterator original) {
        this.m_orig = original;
    }

    public synchronized DTMIterator getInstanceOrThrow() throws CloneNotSupportedException {
        if (this.m_freeStack.isEmpty()) {
            return (DTMIterator) this.m_orig.clone();
        }
        DTMIterator result = (DTMIterator) this.m_freeStack.remove(this.m_freeStack.size() - 1);
        return result;
    }

    public synchronized DTMIterator getInstance() {
        if (this.m_freeStack.isEmpty()) {
            try {
                return (DTMIterator) this.m_orig.clone();
            } catch (Exception ex) {
                throw new WrappedRuntimeException(ex);
            }
        }
        DTMIterator result = (DTMIterator) this.m_freeStack.remove(this.m_freeStack.size() - 1);
        return result;
    }

    public synchronized void freeInstance(DTMIterator obj) {
        this.m_freeStack.add(obj);
    }
}
