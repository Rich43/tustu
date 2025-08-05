package com.sun.xml.internal.bind.v2.runtime.reflect;

import com.sun.xml.internal.bind.api.AccessorException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/reflect/NullSafeAccessor.class */
public class NullSafeAccessor<B, V, P> extends Accessor<B, V> {
    private final Accessor<B, V> core;
    private final Lister<B, V, ?, P> lister;

    public NullSafeAccessor(Accessor<B, V> core, Lister<B, V, ?, P> lister) {
        super(core.getValueType());
        this.core = core;
        this.lister = lister;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Accessor
    public V get(B bean) throws AccessorException {
        V v2 = this.core.get(bean);
        if (v2 == null) {
            P pack = this.lister.startPacking(bean, this.core);
            this.lister.endPacking(pack, bean, this.core);
            v2 = this.core.get(bean);
        }
        return v2;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Accessor
    public void set(B bean, V value) throws AccessorException {
        this.core.set(bean, value);
    }
}
