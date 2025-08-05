package com.sun.org.apache.bcel.internal.generic;

import com.sun.org.apache.bcel.internal.Constants;
import com.sun.org.apache.bcel.internal.classfile.AccessFlags;
import com.sun.org.apache.bcel.internal.classfile.Attribute;
import com.sun.org.apache.bcel.internal.classfile.ConstantPool;
import com.sun.org.apache.bcel.internal.classfile.Field;
import com.sun.org.apache.bcel.internal.classfile.JavaClass;
import com.sun.org.apache.bcel.internal.classfile.Method;
import com.sun.org.apache.bcel.internal.classfile.SourceFile;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/ClassGen.class */
public class ClassGen extends AccessFlags implements Cloneable {
    private String class_name;
    private String super_class_name;
    private String file_name;
    private int class_name_index;
    private int superclass_name_index;
    private int major;
    private int minor;
    private ConstantPoolGen cp;
    private ArrayList field_vec;
    private ArrayList method_vec;
    private ArrayList attribute_vec;
    private ArrayList interface_vec;
    private ArrayList observers;

    public ClassGen(String class_name, String super_class_name, String file_name, int access_flags, String[] interfaces, ConstantPoolGen cp) {
        this.class_name_index = -1;
        this.superclass_name_index = -1;
        this.major = 45;
        this.minor = 3;
        this.field_vec = new ArrayList();
        this.method_vec = new ArrayList();
        this.attribute_vec = new ArrayList();
        this.interface_vec = new ArrayList();
        this.class_name = class_name;
        this.super_class_name = super_class_name;
        this.file_name = file_name;
        this.access_flags = access_flags;
        this.cp = cp;
        if (file_name != null) {
            addAttribute(new SourceFile(cp.addUtf8("SourceFile"), 2, cp.addUtf8(file_name), cp.getConstantPool()));
        }
        this.class_name_index = cp.addClass(class_name);
        this.superclass_name_index = cp.addClass(super_class_name);
        if (interfaces != null) {
            for (String str : interfaces) {
                addInterface(str);
            }
        }
    }

    public ClassGen(String class_name, String super_class_name, String file_name, int access_flags, String[] interfaces) {
        this(class_name, super_class_name, file_name, access_flags, interfaces, new ConstantPoolGen());
    }

    public ClassGen(JavaClass clazz) {
        this.class_name_index = -1;
        this.superclass_name_index = -1;
        this.major = 45;
        this.minor = 3;
        this.field_vec = new ArrayList();
        this.method_vec = new ArrayList();
        this.attribute_vec = new ArrayList();
        this.interface_vec = new ArrayList();
        this.class_name_index = clazz.getClassNameIndex();
        this.superclass_name_index = clazz.getSuperclassNameIndex();
        this.class_name = clazz.getClassName();
        this.super_class_name = clazz.getSuperclassName();
        this.file_name = clazz.getSourceFileName();
        this.access_flags = clazz.getAccessFlags();
        this.cp = new ConstantPoolGen(clazz.getConstantPool());
        this.major = clazz.getMajor();
        this.minor = clazz.getMinor();
        Attribute[] attributes = clazz.getAttributes();
        Method[] methods = clazz.getMethods();
        Field[] fields = clazz.getFields();
        String[] interfaces = clazz.getInterfaceNames();
        for (String str : interfaces) {
            addInterface(str);
        }
        for (Attribute attribute : attributes) {
            addAttribute(attribute);
        }
        for (Method method : methods) {
            addMethod(method);
        }
        for (Field field : fields) {
            addField(field);
        }
    }

    public JavaClass getJavaClass() {
        int[] interfaces = getInterfaces();
        Field[] fields = getFields();
        Method[] methods = getMethods();
        Attribute[] attributes = getAttributes();
        ConstantPool cp = this.cp.getFinalConstantPool();
        return new JavaClass(this.class_name_index, this.superclass_name_index, this.file_name, this.major, this.minor, this.access_flags, cp, interfaces, fields, methods, attributes);
    }

    public void addInterface(String name) {
        this.interface_vec.add(name);
    }

    public void removeInterface(String name) {
        this.interface_vec.remove(name);
    }

    public int getMajor() {
        return this.major;
    }

    public void setMajor(int major) {
        this.major = major;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }

    public int getMinor() {
        return this.minor;
    }

    public void addAttribute(Attribute a2) {
        this.attribute_vec.add(a2);
    }

    public void addMethod(Method m2) {
        this.method_vec.add(m2);
    }

    public void addEmptyConstructor(int access_flags) {
        InstructionList il = new InstructionList();
        il.append(InstructionConstants.THIS);
        il.append(new INVOKESPECIAL(this.cp.addMethodref(this.super_class_name, Constants.CONSTRUCTOR_NAME, "()V")));
        il.append(InstructionConstants.RETURN);
        MethodGen mg = new MethodGen(access_flags, Type.VOID, Type.NO_ARGS, null, Constants.CONSTRUCTOR_NAME, this.class_name, il, this.cp);
        mg.setMaxStack(1);
        addMethod(mg.getMethod());
    }

    public void addField(Field f2) {
        this.field_vec.add(f2);
    }

    public boolean containsField(Field f2) {
        return this.field_vec.contains(f2);
    }

    public Field containsField(String name) {
        Iterator e2 = this.field_vec.iterator();
        while (e2.hasNext()) {
            Field f2 = (Field) e2.next();
            if (f2.getName().equals(name)) {
                return f2;
            }
        }
        return null;
    }

    public Method containsMethod(String name, String signature) {
        Iterator e2 = this.method_vec.iterator();
        while (e2.hasNext()) {
            Method m2 = (Method) e2.next();
            if (m2.getName().equals(name) && m2.getSignature().equals(signature)) {
                return m2;
            }
        }
        return null;
    }

    public void removeAttribute(Attribute a2) {
        this.attribute_vec.remove(a2);
    }

    public void removeMethod(Method m2) {
        this.method_vec.remove(m2);
    }

    public void replaceMethod(Method old, Method new_) {
        if (new_ == null) {
            throw new ClassGenException("Replacement method must not be null");
        }
        int i2 = this.method_vec.indexOf(old);
        if (i2 < 0) {
            this.method_vec.add(new_);
        } else {
            this.method_vec.set(i2, new_);
        }
    }

    public void replaceField(Field old, Field new_) {
        if (new_ == null) {
            throw new ClassGenException("Replacement method must not be null");
        }
        int i2 = this.field_vec.indexOf(old);
        if (i2 < 0) {
            this.field_vec.add(new_);
        } else {
            this.field_vec.set(i2, new_);
        }
    }

    public void removeField(Field f2) {
        this.field_vec.remove(f2);
    }

    public String getClassName() {
        return this.class_name;
    }

    public String getSuperclassName() {
        return this.super_class_name;
    }

    public String getFileName() {
        return this.file_name;
    }

    public void setClassName(String name) {
        this.class_name = name.replace('/', '.');
        this.class_name_index = this.cp.addClass(name);
    }

    public void setSuperclassName(String name) {
        this.super_class_name = name.replace('/', '.');
        this.superclass_name_index = this.cp.addClass(name);
    }

    public Method[] getMethods() {
        Method[] methods = new Method[this.method_vec.size()];
        this.method_vec.toArray(methods);
        return methods;
    }

    public void setMethods(Method[] methods) {
        this.method_vec.clear();
        for (Method method : methods) {
            addMethod(method);
        }
    }

    public void setMethodAt(Method method, int pos) {
        this.method_vec.set(pos, method);
    }

    public Method getMethodAt(int pos) {
        return (Method) this.method_vec.get(pos);
    }

    public String[] getInterfaceNames() {
        int size = this.interface_vec.size();
        String[] interfaces = new String[size];
        this.interface_vec.toArray(interfaces);
        return interfaces;
    }

    public int[] getInterfaces() {
        int size = this.interface_vec.size();
        int[] interfaces = new int[size];
        for (int i2 = 0; i2 < size; i2++) {
            interfaces[i2] = this.cp.addClass((String) this.interface_vec.get(i2));
        }
        return interfaces;
    }

    public Field[] getFields() {
        Field[] fields = new Field[this.field_vec.size()];
        this.field_vec.toArray(fields);
        return fields;
    }

    public Attribute[] getAttributes() {
        Attribute[] attributes = new Attribute[this.attribute_vec.size()];
        this.attribute_vec.toArray(attributes);
        return attributes;
    }

    public ConstantPoolGen getConstantPool() {
        return this.cp;
    }

    public void setConstantPool(ConstantPoolGen constant_pool) {
        this.cp = constant_pool;
    }

    public void setClassNameIndex(int class_name_index) {
        this.class_name_index = class_name_index;
        this.class_name = this.cp.getConstantPool().getConstantString(class_name_index, (byte) 7).replace('/', '.');
    }

    public void setSuperclassNameIndex(int superclass_name_index) {
        this.superclass_name_index = superclass_name_index;
        this.super_class_name = this.cp.getConstantPool().getConstantString(superclass_name_index, (byte) 7).replace('/', '.');
    }

    public int getSuperclassNameIndex() {
        return this.superclass_name_index;
    }

    public int getClassNameIndex() {
        return this.class_name_index;
    }

    public void addObserver(ClassObserver o2) {
        if (this.observers == null) {
            this.observers = new ArrayList();
        }
        this.observers.add(o2);
    }

    public void removeObserver(ClassObserver o2) {
        if (this.observers != null) {
            this.observers.remove(o2);
        }
    }

    public void update() {
        if (this.observers != null) {
            Iterator e2 = this.observers.iterator();
            while (e2.hasNext()) {
                ((ClassObserver) e2.next()).notify(this);
            }
        }
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e2) {
            System.err.println(e2);
            return null;
        }
    }
}
