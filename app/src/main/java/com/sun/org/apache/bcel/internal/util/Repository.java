package com.sun.org.apache.bcel.internal.util;

import com.sun.org.apache.bcel.internal.classfile.JavaClass;
import java.io.Serializable;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/util/Repository.class */
public interface Repository extends Serializable {
    void storeClass(JavaClass javaClass);

    void removeClass(JavaClass javaClass);

    JavaClass findClass(String str);

    JavaClass loadClass(String str) throws ClassNotFoundException;

    JavaClass loadClass(Class cls) throws ClassNotFoundException;

    void clear();
}
