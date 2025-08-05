package com.sun.corba.se.impl.orbutil.closure;

import com.sun.corba.se.spi.orbutil.closure.Closure;

/* loaded from: rt.jar:com/sun/corba/se/impl/orbutil/closure/Constant.class */
public class Constant implements Closure {
    private Object value;

    public Constant(Object obj) {
        this.value = obj;
    }

    @Override // com.sun.corba.se.spi.orbutil.closure.Closure
    public Object evaluate() {
        return this.value;
    }
}
