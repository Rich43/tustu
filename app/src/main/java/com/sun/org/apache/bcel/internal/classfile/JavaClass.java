package com.sun.org.apache.bcel.internal.classfile;

import com.sun.org.apache.bcel.internal.generic.Type;
import com.sun.org.apache.bcel.internal.util.ClassQueue;
import com.sun.org.apache.bcel.internal.util.ClassVector;
import com.sun.org.apache.bcel.internal.util.Repository;
import com.sun.org.apache.bcel.internal.util.SyntheticRepository;
import com.sun.org.apache.xalan.internal.utils.SecuritySupport;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.StringTokenizer;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/classfile/JavaClass.class */
public class JavaClass extends AccessFlags implements Cloneable, Node {
    private String file_name;
    private String package_name;
    private String source_file_name;
    private int class_name_index;
    private int superclass_name_index;
    private String class_name;
    private String superclass_name;
    private int major;
    private int minor;
    private ConstantPool constant_pool;
    private int[] interfaces;
    private String[] interface_names;
    private Field[] fields;
    private Method[] methods;
    private Attribute[] attributes;
    private byte source;
    public static final byte HEAP = 1;
    public static final byte FILE = 2;
    public static final byte ZIP = 3;
    static boolean debug;
    static char sep;
    private transient Repository repository;

    static {
        debug = false;
        sep = '/';
        String debug2 = null;
        String sep2 = null;
        try {
            debug2 = SecuritySupport.getSystemProperty("JavaClass.debug");
            sep2 = SecuritySupport.getSystemProperty("file.separator");
        } catch (SecurityException e2) {
        }
        if (debug2 != null) {
            debug = new Boolean(debug2).booleanValue();
        }
        if (sep2 != null) {
            try {
                sep = sep2.charAt(0);
            } catch (StringIndexOutOfBoundsException e3) {
            }
        }
    }

    public JavaClass(int class_name_index, int superclass_name_index, String file_name, int major, int minor, int access_flags, ConstantPool constant_pool, int[] interfaces, Field[] fields, Method[] methods, Attribute[] attributes, byte source) throws ClassFormatException {
        this.source_file_name = "<Unknown>";
        this.source = (byte) 1;
        this.repository = SyntheticRepository.getInstance();
        interfaces = interfaces == null ? new int[0] : interfaces;
        if (attributes == null) {
            this.attributes = new Attribute[0];
        }
        fields = fields == null ? new Field[0] : fields;
        methods = methods == null ? new Method[0] : methods;
        this.class_name_index = class_name_index;
        this.superclass_name_index = superclass_name_index;
        this.file_name = file_name;
        this.major = major;
        this.minor = minor;
        this.access_flags = access_flags;
        this.constant_pool = constant_pool;
        this.interfaces = interfaces;
        this.fields = fields;
        this.methods = methods;
        this.attributes = attributes;
        this.source = source;
        int i2 = 0;
        while (true) {
            if (i2 >= attributes.length) {
                break;
            }
            if (!(attributes[i2] instanceof SourceFile)) {
                i2++;
            } else {
                this.source_file_name = ((SourceFile) attributes[i2]).getSourceFileName();
                break;
            }
        }
        this.class_name = constant_pool.getConstantString(class_name_index, (byte) 7);
        this.class_name = Utility.compactClassName(this.class_name, false);
        int index = this.class_name.lastIndexOf(46);
        if (index < 0) {
            this.package_name = "";
        } else {
            this.package_name = this.class_name.substring(0, index);
        }
        if (superclass_name_index > 0) {
            this.superclass_name = constant_pool.getConstantString(superclass_name_index, (byte) 7);
            this.superclass_name = Utility.compactClassName(this.superclass_name, false);
        } else {
            this.superclass_name = Constants.OBJECT_CLASS;
        }
        this.interface_names = new String[interfaces.length];
        for (int i3 = 0; i3 < interfaces.length; i3++) {
            String str = constant_pool.getConstantString(interfaces[i3], (byte) 7);
            this.interface_names[i3] = Utility.compactClassName(str, false);
        }
    }

    public JavaClass(int class_name_index, int superclass_name_index, String file_name, int major, int minor, int access_flags, ConstantPool constant_pool, int[] interfaces, Field[] fields, Method[] methods, Attribute[] attributes) {
        this(class_name_index, superclass_name_index, file_name, major, minor, access_flags, constant_pool, interfaces, fields, methods, attributes, (byte) 1);
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Node
    public void accept(Visitor v2) {
        v2.visitJavaClass(this);
    }

    static final void Debug(String str) {
        if (debug) {
            System.out.println(str);
        }
    }

    public void dump(File file) throws IOException {
        File dir;
        String parent = file.getParent();
        if (parent != null && (dir = new File(parent)) != null) {
            dir.mkdirs();
        }
        dump(new DataOutputStream(new FileOutputStream(file)));
    }

    public void dump(String file_name) throws IOException {
        dump(new File(file_name));
    }

    public byte[] getBytes() {
        ByteArrayOutputStream s2 = new ByteArrayOutputStream();
        DataOutputStream ds = new DataOutputStream(s2);
        try {
            try {
                dump(ds);
                try {
                    ds.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            } catch (Throwable th) {
                try {
                    ds.close();
                } catch (IOException e22) {
                    e22.printStackTrace();
                }
                throw th;
            }
        } catch (IOException e3) {
            e3.printStackTrace();
            try {
                ds.close();
            } catch (IOException e23) {
                e23.printStackTrace();
            }
        }
        return s2.toByteArray();
    }

    public void dump(OutputStream file) throws IOException {
        dump(new DataOutputStream(file));
    }

    public void dump(DataOutputStream file) throws IOException {
        file.writeInt(com.sun.java.util.jar.pack.Constants.JAVA_MAGIC);
        file.writeShort(this.minor);
        file.writeShort(this.major);
        this.constant_pool.dump(file);
        file.writeShort(this.access_flags);
        file.writeShort(this.class_name_index);
        file.writeShort(this.superclass_name_index);
        file.writeShort(this.interfaces.length);
        for (int i2 = 0; i2 < this.interfaces.length; i2++) {
            file.writeShort(this.interfaces[i2]);
        }
        file.writeShort(this.fields.length);
        for (int i3 = 0; i3 < this.fields.length; i3++) {
            this.fields[i3].dump(file);
        }
        file.writeShort(this.methods.length);
        for (int i4 = 0; i4 < this.methods.length; i4++) {
            this.methods[i4].dump(file);
        }
        if (this.attributes != null) {
            file.writeShort(this.attributes.length);
            for (int i5 = 0; i5 < this.attributes.length; i5++) {
                this.attributes[i5].dump(file);
            }
        } else {
            file.writeShort(0);
        }
        file.close();
    }

    public Attribute[] getAttributes() {
        return this.attributes;
    }

    public String getClassName() {
        return this.class_name;
    }

    public String getPackageName() {
        return this.package_name;
    }

    public int getClassNameIndex() {
        return this.class_name_index;
    }

    public ConstantPool getConstantPool() {
        return this.constant_pool;
    }

    public Field[] getFields() {
        return this.fields;
    }

    public String getFileName() {
        return this.file_name;
    }

    public String[] getInterfaceNames() {
        return this.interface_names;
    }

    public int[] getInterfaceIndices() {
        return this.interfaces;
    }

    public int getMajor() {
        return this.major;
    }

    public Method[] getMethods() {
        return this.methods;
    }

    public Method getMethod(java.lang.reflect.Method m2) {
        for (int i2 = 0; i2 < this.methods.length; i2++) {
            Method method = this.methods[i2];
            if (m2.getName().equals(method.getName()) && m2.getModifiers() == method.getModifiers() && Type.getSignature(m2).equals(method.getSignature())) {
                return method;
            }
        }
        return null;
    }

    public int getMinor() {
        return this.minor;
    }

    public String getSourceFileName() {
        return this.source_file_name;
    }

    public String getSuperclassName() {
        return this.superclass_name;
    }

    public int getSuperclassNameIndex() {
        return this.superclass_name_index;
    }

    public void setAttributes(Attribute[] attributes) {
        this.attributes = attributes;
    }

    public void setClassName(String class_name) {
        this.class_name = class_name;
    }

    public void setClassNameIndex(int class_name_index) {
        this.class_name_index = class_name_index;
    }

    public void setConstantPool(ConstantPool constant_pool) {
        this.constant_pool = constant_pool;
    }

    public void setFields(Field[] fields) {
        this.fields = fields;
    }

    public void setFileName(String file_name) {
        this.file_name = file_name;
    }

    public void setInterfaceNames(String[] interface_names) {
        this.interface_names = interface_names;
    }

    public void setInterfaces(int[] interfaces) {
        this.interfaces = interfaces;
    }

    public void setMajor(int major) {
        this.major = major;
    }

    public void setMethods(Method[] methods) {
        this.methods = methods;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }

    public void setSourceFileName(String source_file_name) {
        this.source_file_name = source_file_name;
    }

    public void setSuperclassName(String superclass_name) {
        this.superclass_name = superclass_name;
    }

    public void setSuperclassNameIndex(int superclass_name_index) {
        this.superclass_name_index = superclass_name_index;
    }

    public String toString() {
        String access = Utility.accessToString(this.access_flags, true);
        StringBuffer buf = new StringBuffer((access.equals("") ? "" : access + " ") + Utility.classOrInterface(this.access_flags) + " " + this.class_name + " extends " + Utility.compactClassName(this.superclass_name, false) + '\n');
        int size = this.interfaces.length;
        if (size > 0) {
            buf.append("implements\t\t");
            for (int i2 = 0; i2 < size; i2++) {
                buf.append(this.interface_names[i2]);
                if (i2 < size - 1) {
                    buf.append(", ");
                }
            }
            buf.append('\n');
        }
        buf.append("filename\t\t" + this.file_name + '\n');
        buf.append("compiled from\t\t" + this.source_file_name + '\n');
        buf.append("compiler version\t" + this.major + "." + this.minor + '\n');
        buf.append("access flags\t\t" + this.access_flags + '\n');
        buf.append("constant pool\t\t" + this.constant_pool.getLength() + " entries\n");
        buf.append("ACC_SUPER flag\t\t" + isSuper() + "\n");
        if (this.attributes.length > 0) {
            buf.append("\nAttribute(s):\n");
            for (int i3 = 0; i3 < this.attributes.length; i3++) {
                buf.append(indent(this.attributes[i3]));
            }
        }
        if (this.fields.length > 0) {
            buf.append("\n" + this.fields.length + " fields:\n");
            for (int i4 = 0; i4 < this.fields.length; i4++) {
                buf.append("\t" + ((Object) this.fields[i4]) + '\n');
            }
        }
        if (this.methods.length > 0) {
            buf.append("\n" + this.methods.length + " methods:\n");
            for (int i5 = 0; i5 < this.methods.length; i5++) {
                buf.append("\t" + ((Object) this.methods[i5]) + '\n');
            }
        }
        return buf.toString();
    }

    private static final String indent(Object obj) {
        StringTokenizer tok = new StringTokenizer(obj.toString(), "\n");
        StringBuffer buf = new StringBuffer();
        while (tok.hasMoreTokens()) {
            buf.append("\t" + tok.nextToken() + "\n");
        }
        return buf.toString();
    }

    public JavaClass copy() {
        JavaClass c2 = null;
        try {
            c2 = (JavaClass) clone();
        } catch (CloneNotSupportedException e2) {
        }
        c2.constant_pool = this.constant_pool.copy();
        c2.interfaces = (int[]) this.interfaces.clone();
        c2.interface_names = (String[]) this.interface_names.clone();
        c2.fields = new Field[this.fields.length];
        for (int i2 = 0; i2 < this.fields.length; i2++) {
            c2.fields[i2] = this.fields[i2].copy(c2.constant_pool);
        }
        c2.methods = new Method[this.methods.length];
        for (int i3 = 0; i3 < this.methods.length; i3++) {
            c2.methods[i3] = this.methods[i3].copy(c2.constant_pool);
        }
        c2.attributes = new Attribute[this.attributes.length];
        for (int i4 = 0; i4 < this.attributes.length; i4++) {
            c2.attributes[i4] = this.attributes[i4].copy(c2.constant_pool);
        }
        return c2;
    }

    public final boolean isSuper() {
        return (this.access_flags & 32) != 0;
    }

    public final boolean isClass() {
        return (this.access_flags & 512) == 0;
    }

    public final byte getSource() {
        return this.source;
    }

    public Repository getRepository() {
        return this.repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    public final boolean instanceOf(JavaClass super_class) {
        if (equals(super_class)) {
            return true;
        }
        JavaClass[] super_classes = getSuperClasses();
        for (JavaClass javaClass : super_classes) {
            if (javaClass.equals(super_class)) {
                return true;
            }
        }
        if (super_class.isInterface()) {
            return implementationOf(super_class);
        }
        return false;
    }

    public boolean implementationOf(JavaClass inter) {
        if (!inter.isInterface()) {
            throw new IllegalArgumentException(inter.getClassName() + " is no interface");
        }
        if (equals(inter)) {
            return true;
        }
        JavaClass[] super_interfaces = getAllInterfaces();
        for (JavaClass javaClass : super_interfaces) {
            if (javaClass.equals(inter)) {
                return true;
            }
        }
        return false;
    }

    public JavaClass getSuperClass() {
        if (Constants.OBJECT_CLASS.equals(getClassName())) {
            return null;
        }
        try {
            return this.repository.loadClass(getSuperclassName());
        } catch (ClassNotFoundException e2) {
            System.err.println(e2);
            return null;
        }
    }

    public JavaClass[] getSuperClasses() {
        ClassVector vec = new ClassVector();
        JavaClass superClass = getSuperClass();
        while (true) {
            JavaClass clazz = superClass;
            if (clazz != null) {
                vec.addElement(clazz);
                superClass = clazz.getSuperClass();
            } else {
                return vec.toArray();
            }
        }
    }

    public JavaClass[] getInterfaces() {
        String[] interfaces = getInterfaceNames();
        JavaClass[] classes = new JavaClass[interfaces.length];
        for (int i2 = 0; i2 < interfaces.length; i2++) {
            try {
                classes[i2] = this.repository.loadClass(interfaces[i2]);
            } catch (ClassNotFoundException e2) {
                System.err.println(e2);
                return null;
            }
        }
        return classes;
    }

    public JavaClass[] getAllInterfaces() {
        ClassQueue queue = new ClassQueue();
        ClassVector vec = new ClassVector();
        queue.enqueue(this);
        while (!queue.empty()) {
            JavaClass clazz = queue.dequeue();
            JavaClass souper = clazz.getSuperClass();
            JavaClass[] interfaces = clazz.getInterfaces();
            if (clazz.isInterface()) {
                vec.addElement(clazz);
            } else if (souper != null) {
                queue.enqueue(souper);
            }
            for (JavaClass javaClass : interfaces) {
                queue.enqueue(javaClass);
            }
        }
        return vec.toArray();
    }
}
