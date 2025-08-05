package com.sun.org.apache.bcel.internal.util;

import com.sun.org.apache.bcel.internal.classfile.ClassFormatException;
import com.sun.org.apache.bcel.internal.classfile.ClassParser;
import com.sun.org.apache.bcel.internal.classfile.JavaClass;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/util/SyntheticRepository.class */
public class SyntheticRepository implements Repository {
    private static HashMap _instances = new HashMap();
    private HashMap _loadedClasses = new HashMap();

    private SyntheticRepository() {
    }

    public static SyntheticRepository getInstance() {
        return new SyntheticRepository();
    }

    @Override // com.sun.org.apache.bcel.internal.util.Repository
    public void storeClass(JavaClass clazz) {
        this._loadedClasses.put(clazz.getClassName(), clazz);
        clazz.setRepository(this);
    }

    @Override // com.sun.org.apache.bcel.internal.util.Repository
    public void removeClass(JavaClass clazz) {
        this._loadedClasses.remove(clazz.getClassName());
    }

    @Override // com.sun.org.apache.bcel.internal.util.Repository
    public JavaClass findClass(String className) {
        return (JavaClass) this._loadedClasses.get(className);
    }

    @Override // com.sun.org.apache.bcel.internal.util.Repository
    public JavaClass loadClass(String className) throws ClassNotFoundException {
        if (className == null || className.equals("")) {
            throw new IllegalArgumentException("Invalid class name " + className);
        }
        String className2 = className.replace('/', '.');
        IOException e2 = new IOException("Couldn't find: " + className2 + ".class");
        throw new ClassNotFoundException("Exception while looking for class " + className2 + ": " + e2.toString());
    }

    @Override // com.sun.org.apache.bcel.internal.util.Repository
    public JavaClass loadClass(Class clazz) throws ClassNotFoundException {
        String className = clazz.getName();
        String name = className;
        int i2 = name.lastIndexOf(46);
        if (i2 > 0) {
            name = name.substring(i2 + 1);
        }
        return loadClass(clazz.getResourceAsStream(name + ".class"), className);
    }

    private JavaClass loadClass(InputStream is, String className) throws ClassNotFoundException, ClassFormatException {
        JavaClass clazz = findClass(className);
        if (clazz != null) {
            return clazz;
        }
        if (is != null) {
            try {
                ClassParser parser = new ClassParser(is, className);
                JavaClass clazz2 = parser.parse();
                storeClass(clazz2);
                return clazz2;
            } catch (IOException e2) {
                throw new ClassNotFoundException("Exception while looking for class " + className + ": " + e2.toString());
            }
        }
        throw new ClassNotFoundException("SyntheticRepository could not load " + className);
    }

    @Override // com.sun.org.apache.bcel.internal.util.Repository
    public void clear() {
        this._loadedClasses.clear();
    }
}
