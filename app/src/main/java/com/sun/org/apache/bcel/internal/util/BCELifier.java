package com.sun.org.apache.bcel.internal.util;

import com.sun.org.apache.bcel.internal.Constants;
import com.sun.org.apache.bcel.internal.classfile.ClassFormatException;
import com.sun.org.apache.bcel.internal.classfile.ClassParser;
import com.sun.org.apache.bcel.internal.classfile.ConstantValue;
import com.sun.org.apache.bcel.internal.classfile.EmptyVisitor;
import com.sun.org.apache.bcel.internal.classfile.Field;
import com.sun.org.apache.bcel.internal.classfile.JavaClass;
import com.sun.org.apache.bcel.internal.classfile.Method;
import com.sun.org.apache.bcel.internal.classfile.Utility;
import com.sun.org.apache.bcel.internal.generic.ArrayType;
import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.MethodGen;
import com.sun.org.apache.bcel.internal.generic.Type;
import java.io.OutputStream;
import java.io.PrintWriter;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/util/BCELifier.class */
public class BCELifier extends EmptyVisitor {
    private JavaClass _clazz;
    private PrintWriter _out;
    private ConstantPoolGen _cp;

    public BCELifier(JavaClass clazz, OutputStream out) {
        this._clazz = clazz;
        this._out = new PrintWriter(out);
        this._cp = new ConstantPoolGen(this._clazz.getConstantPool());
    }

    public void start() {
        visitJavaClass(this._clazz);
        this._out.flush();
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.EmptyVisitor, com.sun.org.apache.bcel.internal.classfile.Visitor
    public void visitJavaClass(JavaClass clazz) {
        String class_name = clazz.getClassName();
        String super_name = clazz.getSuperclassName();
        String package_name = clazz.getPackageName();
        String inter = Utility.printArray(clazz.getInterfaceNames(), false, true);
        if (!"".equals(package_name)) {
            class_name = class_name.substring(package_name.length() + 1);
            this._out.println("package " + package_name + ";\n");
        }
        this._out.println("import com.sun.org.apache.bcel.internal.generic.*;");
        this._out.println("import com.sun.org.apache.bcel.internal.classfile.*;");
        this._out.println("import com.sun.org.apache.bcel.internal.*;");
        this._out.println("import java.io.*;\n");
        this._out.println("public class " + class_name + "Creator implements Constants {");
        this._out.println("  private InstructionFactory _factory;");
        this._out.println("  private ConstantPoolGen    _cp;");
        this._out.println("  private ClassGen           _cg;\n");
        this._out.println("  public " + class_name + "Creator() {");
        this._out.println("    _cg = new ClassGen(\"" + ("".equals(package_name) ? class_name : package_name + "." + class_name) + "\", \"" + super_name + "\", \"" + clazz.getSourceFileName() + "\", " + printFlags(clazz.getAccessFlags(), true) + ", new String[] { " + inter + " });\n");
        this._out.println("    _cp = _cg.getConstantPool();");
        this._out.println("    _factory = new InstructionFactory(_cg, _cp);");
        this._out.println("  }\n");
        printCreate();
        Field[] fields = clazz.getFields();
        if (fields.length > 0) {
            this._out.println("  private void createFields() {");
            this._out.println("    FieldGen field;");
            for (Field field : fields) {
                field.accept(this);
            }
            this._out.println("  }\n");
        }
        Method[] methods = clazz.getMethods();
        for (int i2 = 0; i2 < methods.length; i2++) {
            this._out.println("  private void createMethod_" + i2 + "() {");
            methods[i2].accept(this);
            this._out.println("  }\n");
        }
        printMain();
        this._out.println("}");
    }

    private void printCreate() {
        this._out.println("  public void create(OutputStream out) throws IOException {");
        Field[] fields = this._clazz.getFields();
        if (fields.length > 0) {
            this._out.println("    createFields();");
        }
        Method[] methods = this._clazz.getMethods();
        for (int i2 = 0; i2 < methods.length; i2++) {
            this._out.println("    createMethod_" + i2 + "();");
        }
        this._out.println("    _cg.getJavaClass().dump(out);");
        this._out.println("  }\n");
    }

    private void printMain() {
        String class_name = this._clazz.getClassName();
        this._out.println("  public static void _main(String[] args) throws Exception {");
        this._out.println("    " + class_name + "Creator creator = new " + class_name + "Creator();");
        this._out.println("    creator.create(new FileOutputStream(\"" + class_name + ".class\"));");
        this._out.println("  }");
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.EmptyVisitor, com.sun.org.apache.bcel.internal.classfile.Visitor
    public void visitField(Field field) throws ClassFormatException {
        this._out.println("\n    field = new FieldGen(" + printFlags(field.getAccessFlags()) + ", " + printType(field.getSignature()) + ", \"" + field.getName() + "\", _cp);");
        ConstantValue cv = field.getConstantValue();
        if (cv != null) {
            String value = cv.toString();
            this._out.println("    field.setInitValue(" + value + ")");
        }
        this._out.println("    _cg.addField(field.getField());");
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.EmptyVisitor, com.sun.org.apache.bcel.internal.classfile.Visitor
    public void visitMethod(Method method) {
        MethodGen mg = new MethodGen(method, this._clazz.getClassName(), this._cp);
        Type result_type = mg.getReturnType();
        Type[] arg_types = mg.getArgumentTypes();
        this._out.println("    InstructionList il = new InstructionList();");
        this._out.println("    MethodGen method = new MethodGen(" + printFlags(method.getAccessFlags()) + ", " + printType(result_type) + ", " + printArgumentTypes(arg_types) + ", new String[] { " + Utility.printArray(mg.getArgumentNames(), false, true) + " }, \"" + method.getName() + "\", \"" + this._clazz.getClassName() + "\", il, _cp);\n");
        BCELFactory factory = new BCELFactory(mg, this._out);
        factory.start();
        this._out.println("    method.setMaxStack();");
        this._out.println("    method.setMaxLocals();");
        this._out.println("    _cg.addMethod(method.getMethod());");
        this._out.println("    il.dispose();");
    }

    static String printFlags(int flags) {
        return printFlags(flags, false);
    }

    static String printFlags(int flags, boolean for_class) {
        if (flags == 0) {
            return "0";
        }
        StringBuffer buf = new StringBuffer();
        int pow = 1;
        for (int i2 = 0; i2 <= 2048; i2++) {
            if ((flags & pow) != 0) {
                if (pow == 32 && for_class) {
                    buf.append("ACC_SUPER | ");
                } else {
                    buf.append("ACC_" + Constants.ACCESS_NAMES[i2].toUpperCase() + " | ");
                }
            }
            pow <<= 1;
        }
        String str = buf.toString();
        return str.substring(0, str.length() - 3);
    }

    static String printArgumentTypes(Type[] arg_types) {
        if (arg_types.length == 0) {
            return "Type.NO_ARGS";
        }
        StringBuffer args = new StringBuffer();
        for (int i2 = 0; i2 < arg_types.length; i2++) {
            args.append(printType(arg_types[i2]));
            if (i2 < arg_types.length - 1) {
                args.append(", ");
            }
        }
        return "new Type[] { " + args.toString() + " }";
    }

    static String printType(Type type) {
        return printType(type.getSignature());
    }

    static String printType(String signature) throws StringIndexOutOfBoundsException, ClassFormatException {
        Type type = Type.getType(signature);
        byte t2 = type.getType();
        if (t2 <= 12) {
            return "Type." + Constants.TYPE_NAMES[t2].toUpperCase();
        }
        if (type.toString().equals("java.lang.String")) {
            return "Type.STRING";
        }
        if (type.toString().equals(com.sun.org.apache.xalan.internal.xsltc.compiler.Constants.OBJECT_CLASS)) {
            return "Type.OBJECT";
        }
        if (type.toString().equals(com.sun.org.apache.xalan.internal.xsltc.compiler.Constants.STRING_BUFFER_CLASS)) {
            return "Type.STRINGBUFFER";
        }
        if (type instanceof ArrayType) {
            ArrayType at2 = (ArrayType) type;
            return "new ArrayType(" + printType(at2.getBasicType()) + ", " + at2.getDimensions() + ")";
        }
        return "new ObjectType(\"" + Utility.signatureToString(signature, false) + "\")";
    }

    public static void _main(String[] argv) throws Exception {
        String name = argv[0];
        JavaClass javaClassLookupClass = com.sun.org.apache.bcel.internal.Repository.lookupClass(name);
        JavaClass java_class = javaClassLookupClass;
        if (javaClassLookupClass == null) {
            java_class = new ClassParser(name).parse();
        }
        BCELifier bcelifier = new BCELifier(java_class, System.out);
        bcelifier.start();
    }
}
