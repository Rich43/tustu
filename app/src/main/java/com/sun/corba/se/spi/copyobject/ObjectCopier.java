package com.sun.corba.se.spi.copyobject;

/* loaded from: rt.jar:com/sun/corba/se/spi/copyobject/ObjectCopier.class */
public interface ObjectCopier {
    Object copy(Object obj) throws ReflectiveCopyException;
}
