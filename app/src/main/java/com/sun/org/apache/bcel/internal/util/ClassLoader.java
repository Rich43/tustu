package com.sun.org.apache.bcel.internal.util;

import com.sun.org.apache.bcel.internal.classfile.ClassParser;
import com.sun.org.apache.bcel.internal.classfile.ConstantClass;
import com.sun.org.apache.bcel.internal.classfile.ConstantPool;
import com.sun.org.apache.bcel.internal.classfile.ConstantUtf8;
import com.sun.org.apache.bcel.internal.classfile.JavaClass;
import com.sun.org.apache.bcel.internal.classfile.Utility;
import java.io.ByteArrayInputStream;
import java.util.Hashtable;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/util/ClassLoader.class */
public class ClassLoader extends java.lang.ClassLoader {
    private Hashtable classes;
    private String[] ignored_packages;
    private Repository repository;
    private java.lang.ClassLoader deferTo;

    public ClassLoader() {
        this.classes = new Hashtable();
        this.ignored_packages = new String[]{"java.", "javax.", "sun."};
        this.repository = SyntheticRepository.getInstance();
        this.deferTo = getSystemClassLoader();
    }

    public ClassLoader(java.lang.ClassLoader deferTo) {
        this.classes = new Hashtable();
        this.ignored_packages = new String[]{"java.", "javax.", "sun."};
        this.repository = SyntheticRepository.getInstance();
        this.deferTo = getSystemClassLoader();
        this.deferTo = deferTo;
        this.repository = new ClassLoaderRepository(deferTo);
    }

    public ClassLoader(String[] ignored_packages) {
        this.classes = new Hashtable();
        this.ignored_packages = new String[]{"java.", "javax.", "sun."};
        this.repository = SyntheticRepository.getInstance();
        this.deferTo = getSystemClassLoader();
        addIgnoredPkgs(ignored_packages);
    }

    public ClassLoader(java.lang.ClassLoader deferTo, String[] ignored_packages) {
        this.classes = new Hashtable();
        this.ignored_packages = new String[]{"java.", "javax.", "sun."};
        this.repository = SyntheticRepository.getInstance();
        this.deferTo = getSystemClassLoader();
        this.deferTo = deferTo;
        this.repository = new ClassLoaderRepository(deferTo);
        addIgnoredPkgs(ignored_packages);
    }

    private void addIgnoredPkgs(String[] ignored_packages) {
        String[] new_p = new String[ignored_packages.length + this.ignored_packages.length];
        System.arraycopy(this.ignored_packages, 0, new_p, 0, this.ignored_packages.length);
        System.arraycopy(ignored_packages, 0, new_p, this.ignored_packages.length, ignored_packages.length);
        this.ignored_packages = new_p;
    }

    @Override // java.lang.ClassLoader
    protected Class loadClass(String class_name, boolean resolve) throws ClassNotFoundException {
        JavaClass clazz;
        Class cls = (Class) this.classes.get(class_name);
        Class cl = cls;
        if (cls == null) {
            int i2 = 0;
            while (true) {
                if (i2 >= this.ignored_packages.length) {
                    break;
                }
                if (!class_name.startsWith(this.ignored_packages[i2])) {
                    i2++;
                } else {
                    cl = this.deferTo.loadClass(class_name);
                    break;
                }
            }
            if (cl == null) {
                if (class_name.indexOf("$$BCEL$$") >= 0) {
                    clazz = createClass(class_name);
                } else {
                    JavaClass clazz2 = this.repository.loadClass(class_name);
                    if (clazz2 != null) {
                        clazz = modifyClass(clazz2);
                    } else {
                        throw new ClassNotFoundException(class_name);
                    }
                }
                if (clazz != null) {
                    byte[] bytes = clazz.getBytes();
                    cl = defineClass(class_name, bytes, 0, bytes.length);
                } else {
                    cl = Class.forName(class_name);
                }
            }
            if (resolve) {
                resolveClass(cl);
            }
        }
        this.classes.put(class_name, cl);
        return cl;
    }

    protected JavaClass modifyClass(JavaClass clazz) {
        return clazz;
    }

    protected JavaClass createClass(String class_name) {
        int index = class_name.indexOf("$$BCEL$$");
        String real_name = class_name.substring(index + 8);
        try {
            byte[] bytes = Utility.decode(real_name, true);
            ClassParser parser = new ClassParser(new ByteArrayInputStream(bytes), "foo");
            JavaClass clazz = parser.parse();
            ConstantPool cp = clazz.getConstantPool();
            ConstantClass cl = (ConstantClass) cp.getConstant(clazz.getClassNameIndex(), (byte) 7);
            ConstantUtf8 name = (ConstantUtf8) cp.getConstant(cl.getNameIndex(), (byte) 1);
            name.setBytes(class_name.replace('.', '/'));
            return clazz;
        } catch (Throwable e2) {
            e2.printStackTrace();
            return null;
        }
    }
}
