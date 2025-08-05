package com.sun.org.apache.bcel.internal.util;

import com.sun.org.apache.bcel.internal.classfile.ClassFormatException;
import com.sun.org.apache.bcel.internal.classfile.ClassParser;
import com.sun.org.apache.bcel.internal.classfile.JavaClass;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/util/ClassLoaderRepository.class */
public class ClassLoaderRepository implements Repository {
    private java.lang.ClassLoader loader;
    private HashMap loadedClasses = new HashMap();

    public ClassLoaderRepository(java.lang.ClassLoader loader) {
        this.loader = loader;
    }

    @Override // com.sun.org.apache.bcel.internal.util.Repository
    public void storeClass(JavaClass clazz) {
        this.loadedClasses.put(clazz.getClassName(), clazz);
        clazz.setRepository(this);
    }

    @Override // com.sun.org.apache.bcel.internal.util.Repository
    public void removeClass(JavaClass clazz) {
        this.loadedClasses.remove(clazz.getClassName());
    }

    @Override // com.sun.org.apache.bcel.internal.util.Repository
    public JavaClass findClass(String className) {
        if (this.loadedClasses.containsKey(className)) {
            return (JavaClass) this.loadedClasses.get(className);
        }
        return null;
    }

    @Override // com.sun.org.apache.bcel.internal.util.Repository
    public JavaClass loadClass(String className) throws ClassNotFoundException, ClassFormatException {
        String classFile = className.replace('.', '/');
        JavaClass RC = findClass(className);
        if (RC != null) {
            return RC;
        }
        try {
            InputStream is = this.loader.getResourceAsStream(classFile + ".class");
            if (is == null) {
                throw new ClassNotFoundException(className + " not found.");
            }
            ClassParser parser = new ClassParser(is, className);
            JavaClass RC2 = parser.parse();
            storeClass(RC2);
            return RC2;
        } catch (IOException e2) {
            throw new ClassNotFoundException(e2.toString());
        }
    }

    @Override // com.sun.org.apache.bcel.internal.util.Repository
    public JavaClass loadClass(Class clazz) throws ClassNotFoundException {
        return loadClass(clazz.getName());
    }

    @Override // com.sun.org.apache.bcel.internal.util.Repository
    public void clear() {
        this.loadedClasses.clear();
    }
}
