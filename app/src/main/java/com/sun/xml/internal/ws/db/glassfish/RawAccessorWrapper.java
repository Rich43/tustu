package com.sun.xml.internal.ws.db.glassfish;

import com.sun.xml.internal.bind.api.AccessorException;
import com.sun.xml.internal.bind.api.RawAccessor;
import com.sun.xml.internal.ws.spi.db.DatabindingException;
import com.sun.xml.internal.ws.spi.db.PropertyAccessor;

/* loaded from: rt.jar:com/sun/xml/internal/ws/db/glassfish/RawAccessorWrapper.class */
public class RawAccessorWrapper implements PropertyAccessor {
    private RawAccessor accessor;

    public RawAccessorWrapper(RawAccessor a2) {
        this.accessor = a2;
    }

    public boolean equals(Object obj) {
        return this.accessor.equals(obj);
    }

    @Override // com.sun.xml.internal.ws.spi.db.PropertyAccessor
    public Object get(Object bean) throws DatabindingException {
        try {
            return this.accessor.get(bean);
        } catch (AccessorException e2) {
            throw new DatabindingException(e2);
        }
    }

    public int hashCode() {
        return this.accessor.hashCode();
    }

    @Override // com.sun.xml.internal.ws.spi.db.PropertyAccessor
    public void set(Object bean, Object value) throws DatabindingException {
        try {
            this.accessor.set(bean, value);
        } catch (AccessorException e2) {
            throw new DatabindingException(e2);
        }
    }

    public String toString() {
        return this.accessor.toString();
    }
}
