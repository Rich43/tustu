package com.sun.corba.se.impl.orbutil.closure;

import com.sun.corba.se.spi.orbutil.closure.Closure;

/* loaded from: rt.jar:com/sun/corba/se/impl/orbutil/closure/Future.class */
public class Future implements Closure {
    private Closure closure;
    private boolean evaluated = false;
    private Object value = null;

    public Future(Closure closure) {
        this.closure = closure;
    }

    @Override // com.sun.corba.se.spi.orbutil.closure.Closure
    public synchronized Object evaluate() {
        if (!this.evaluated) {
            this.evaluated = true;
            this.value = this.closure.evaluate();
        }
        return this.value;
    }
}
