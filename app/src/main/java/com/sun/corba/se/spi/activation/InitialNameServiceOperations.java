package com.sun.corba.se.spi.activation;

import com.sun.corba.se.spi.activation.InitialNameServicePackage.NameAlreadyBound;
import org.omg.CORBA.Object;

/* loaded from: rt.jar:com/sun/corba/se/spi/activation/InitialNameServiceOperations.class */
public interface InitialNameServiceOperations {
    void bind(String str, Object object, boolean z2) throws NameAlreadyBound;
}
