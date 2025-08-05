package com.sun.org.apache.bcel.internal.classfile;

import java.io.DataInputStream;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/classfile/AttributeReader.class */
public interface AttributeReader {
    Attribute createAttribute(int i2, int i3, DataInputStream dataInputStream, ConstantPool constantPool);
}
