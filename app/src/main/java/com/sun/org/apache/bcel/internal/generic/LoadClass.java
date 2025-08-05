package com.sun.org.apache.bcel.internal.generic;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/LoadClass.class */
public interface LoadClass {
    ObjectType getLoadClassType(ConstantPoolGen constantPoolGen);

    Type getType(ConstantPoolGen constantPoolGen);
}
