package com.sun.corba.se.spi.orbutil.closure;

import com.sun.corba.se.impl.orbutil.closure.Constant;
import com.sun.corba.se.impl.orbutil.closure.Future;

/* loaded from: rt.jar:com/sun/corba/se/spi/orbutil/closure/ClosureFactory.class */
public abstract class ClosureFactory {
    private ClosureFactory() {
    }

    public static Closure makeConstant(Object obj) {
        return new Constant(obj);
    }

    public static Closure makeFuture(Closure closure) {
        return new Future(closure);
    }
}
