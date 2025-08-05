package com.sun.xml.internal.bind.v2.runtime.unmarshaller;

import com.sun.xml.internal.bind.api.AccessorException;
import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;
import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/unmarshaller/Scope.class */
public final class Scope<BeanT, PropT, ItemT, PackT> {
    public final UnmarshallingContext context;
    private BeanT bean;
    private Accessor<BeanT, PropT> acc;
    private PackT pack;
    private Lister<BeanT, PropT, ItemT, PackT> lister;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !Scope.class.desiredAssertionStatus();
    }

    Scope(UnmarshallingContext context) {
        this.context = context;
    }

    public boolean hasStarted() {
        return this.bean != null;
    }

    public void reset() {
        if (this.bean == null) {
            if (!$assertionsDisabled && !clean()) {
                throw new AssertionError();
            }
        } else {
            this.bean = null;
            this.acc = null;
            this.pack = null;
            this.lister = null;
        }
    }

    public void finish() throws AccessorException {
        if (hasStarted()) {
            this.lister.endPacking(this.pack, this.bean, this.acc);
            reset();
        }
        if (!$assertionsDisabled && !clean()) {
            throw new AssertionError();
        }
    }

    private boolean clean() {
        return this.bean == null && this.acc == null && this.pack == null && this.lister == null;
    }

    public void add(Accessor<BeanT, PropT> accessor, Lister<BeanT, PropT, ItemT, PackT> lister, ItemT itemt) throws SAXException {
        try {
            if (!hasStarted()) {
                this.bean = (BeanT) this.context.getCurrentState().getTarget();
                this.acc = accessor;
                this.lister = lister;
                this.pack = lister.startPacking(this.bean, accessor);
            }
            lister.addToPack(this.pack, itemt);
        } catch (AccessorException e2) {
            Loader.handleGenericException(e2, true);
            this.lister = Lister.getErrorInstance();
            this.acc = Accessor.getErrorInstance();
        }
    }

    public void start(Accessor<BeanT, PropT> accessor, Lister<BeanT, PropT, ItemT, PackT> lister) throws SAXException {
        try {
            if (!hasStarted()) {
                this.bean = (BeanT) this.context.getCurrentState().getTarget();
                this.acc = accessor;
                this.lister = lister;
                this.pack = lister.startPacking(this.bean, accessor);
            }
        } catch (AccessorException e2) {
            Loader.handleGenericException(e2, true);
            this.lister = Lister.getErrorInstance();
            this.acc = Accessor.getErrorInstance();
        }
    }
}
