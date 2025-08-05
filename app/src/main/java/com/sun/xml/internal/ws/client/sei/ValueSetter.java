package com.sun.xml.internal.ws.client.sei;

import com.sun.xml.internal.ws.model.ParameterImpl;
import com.sun.xml.internal.ws.spi.db.PropertyAccessor;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.ws.Holder;
import javax.xml.ws.WebServiceException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/client/sei/ValueSetter.class */
public abstract class ValueSetter {
    private static final ValueSetter RETURN_VALUE = new ReturnValue();
    private static final ValueSetter[] POOL = new ValueSetter[16];
    static final ValueSetter SINGLE_VALUE;

    abstract Object put(Object obj, Object[] objArr);

    private ValueSetter() {
    }

    static {
        for (int i2 = 0; i2 < POOL.length; i2++) {
            POOL[i2] = new Param(i2);
        }
        SINGLE_VALUE = new SingleValue();
    }

    static ValueSetter getSync(ParameterImpl p2) {
        int idx = p2.getIndex();
        if (idx == -1) {
            return RETURN_VALUE;
        }
        if (idx < POOL.length) {
            return POOL[idx];
        }
        return new Param(idx);
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/client/sei/ValueSetter$ReturnValue.class */
    private static final class ReturnValue extends ValueSetter {
        private ReturnValue() {
            super();
        }

        @Override // com.sun.xml.internal.ws.client.sei.ValueSetter
        Object put(Object obj, Object[] args) {
            return obj;
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/client/sei/ValueSetter$Param.class */
    static final class Param extends ValueSetter {
        private final int idx;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !ValueSetter.class.desiredAssertionStatus();
        }

        public Param(int idx) {
            super();
            this.idx = idx;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.sun.xml.internal.ws.client.sei.ValueSetter
        Object put(Object obj, Object[] args) {
            Object arg = args[this.idx];
            if (arg != null) {
                if (!$assertionsDisabled && !(arg instanceof Holder)) {
                    throw new AssertionError();
                }
                ((Holder) arg).value = obj;
                return null;
            }
            return null;
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/client/sei/ValueSetter$SingleValue.class */
    private static final class SingleValue extends ValueSetter {
        private SingleValue() {
            super();
        }

        @Override // com.sun.xml.internal.ws.client.sei.ValueSetter
        Object put(Object obj, Object[] args) {
            args[0] = obj;
            return null;
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/client/sei/ValueSetter$AsyncBeanValueSetter.class */
    static final class AsyncBeanValueSetter extends ValueSetter {
        private final PropertyAccessor accessor;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !ValueSetter.class.desiredAssertionStatus();
        }

        AsyncBeanValueSetter(ParameterImpl p2, Class wrapper) {
            super();
            QName name = p2.getName();
            try {
                this.accessor = p2.getOwner().getBindingContext().getElementPropertyAccessor(wrapper, name.getNamespaceURI(), name.getLocalPart());
            } catch (JAXBException e2) {
                throw new WebServiceException(((Object) wrapper) + " do not have a property of the name " + ((Object) name), e2);
            }
        }

        @Override // com.sun.xml.internal.ws.client.sei.ValueSetter
        Object put(Object obj, Object[] args) {
            if (!$assertionsDisabled && args == null) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && args.length != 1) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && args[0] == null) {
                throw new AssertionError();
            }
            Object bean = args[0];
            try {
                this.accessor.set(bean, obj);
                return null;
            } catch (Exception e2) {
                throw new WebServiceException(e2);
            }
        }
    }
}
