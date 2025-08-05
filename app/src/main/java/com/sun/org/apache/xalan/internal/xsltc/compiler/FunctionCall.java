package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.generic.BranchInstruction;
import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.IFEQ;
import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
import com.sun.org.apache.bcel.internal.generic.INVOKESPECIAL;
import com.sun.org.apache.bcel.internal.generic.INVOKESTATIC;
import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
import com.sun.org.apache.bcel.internal.generic.InstructionConstants;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.LocalVariableGen;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.sun.org.apache.bcel.internal.generic.PUSH;
import com.sun.org.apache.xalan.internal.utils.ObjectFactory;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.BooleanType;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.IntType;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodType;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MultiHashtable;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ObjectType;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ReferenceType;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.xml.internal.JdkXmlFeatures;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/FunctionCall.class */
class FunctionCall extends Expression {
    private QName _fname;
    private final Vector _arguments;
    protected static final String EXT_XSLTC = "http://xml.apache.org/xalan/xsltc";
    protected static final String JAVA_EXT_XSLTC = "http://xml.apache.org/xalan/xsltc/java";
    protected static final String EXT_XALAN = "http://xml.apache.org/xalan";
    protected static final String JAVA_EXT_XALAN = "http://xml.apache.org/xalan/java";
    protected static final String JAVA_EXT_XALAN_OLD = "http://xml.apache.org/xslt/java";
    protected static final String EXSLT_COMMON = "http://exslt.org/common";
    protected static final String EXSLT_MATH = "http://exslt.org/math";
    protected static final String EXSLT_SETS = "http://exslt.org/sets";
    protected static final String EXSLT_DATETIME = "http://exslt.org/dates-and-times";
    protected static final String EXSLT_STRINGS = "http://exslt.org/strings";
    protected static final String XALAN_CLASSPACKAGE_NAMESPACE = "xalan://";
    protected static final int NAMESPACE_FORMAT_JAVA = 0;
    protected static final int NAMESPACE_FORMAT_CLASS = 1;
    protected static final int NAMESPACE_FORMAT_PACKAGE = 2;
    protected static final int NAMESPACE_FORMAT_CLASS_OR_PACKAGE = 3;
    private int _namespace_format;
    Expression _thisArgument;
    private String _className;
    private Class _clazz;
    private Method _chosenMethod;
    private Constructor _chosenConstructor;
    private MethodType _chosenMethodType;
    private boolean unresolvedExternal;
    private boolean _isExtConstructor;
    private boolean _isStatic;
    private static final Map<Class<?>, Type> JAVA2INTERNAL;
    private static final Map<String, String> EXTENSIONNAMESPACE;
    private static final Map<String, String> EXTENSIONFUNCTION;
    private static final Vector EMPTY_ARG_LIST = new Vector(0);
    private static final MultiHashtable<Type, JavaType> _internal2Java = new MultiHashtable<>();

    static {
        try {
            Class<?> nodeClass = Class.forName("org.w3c.dom.Node");
            Class<?> nodeListClass = Class.forName("org.w3c.dom.NodeList");
            _internal2Java.put(Type.Boolean, new JavaType(Boolean.TYPE, 0));
            _internal2Java.put(Type.Boolean, new JavaType(Boolean.class, 1));
            _internal2Java.put(Type.Boolean, new JavaType(Object.class, 2));
            _internal2Java.put(Type.Real, new JavaType(Double.TYPE, 0));
            _internal2Java.put(Type.Real, new JavaType(Double.class, 1));
            _internal2Java.put(Type.Real, new JavaType(Float.TYPE, 2));
            _internal2Java.put(Type.Real, new JavaType(Long.TYPE, 3));
            _internal2Java.put(Type.Real, new JavaType(Integer.TYPE, 4));
            _internal2Java.put(Type.Real, new JavaType(Short.TYPE, 5));
            _internal2Java.put(Type.Real, new JavaType(Byte.TYPE, 6));
            _internal2Java.put(Type.Real, new JavaType(Character.TYPE, 7));
            _internal2Java.put(Type.Real, new JavaType(Object.class, 8));
            _internal2Java.put(Type.Int, new JavaType(Double.TYPE, 0));
            _internal2Java.put(Type.Int, new JavaType(Double.class, 1));
            _internal2Java.put(Type.Int, new JavaType(Float.TYPE, 2));
            _internal2Java.put(Type.Int, new JavaType(Long.TYPE, 3));
            _internal2Java.put(Type.Int, new JavaType(Integer.TYPE, 4));
            _internal2Java.put(Type.Int, new JavaType(Short.TYPE, 5));
            _internal2Java.put(Type.Int, new JavaType(Byte.TYPE, 6));
            _internal2Java.put(Type.Int, new JavaType(Character.TYPE, 7));
            _internal2Java.put(Type.Int, new JavaType(Object.class, 8));
            _internal2Java.put(Type.String, new JavaType(String.class, 0));
            _internal2Java.put(Type.String, new JavaType(Object.class, 1));
            _internal2Java.put(Type.NodeSet, new JavaType(nodeListClass, 0));
            _internal2Java.put(Type.NodeSet, new JavaType(nodeClass, 1));
            _internal2Java.put(Type.NodeSet, new JavaType(Object.class, 2));
            _internal2Java.put(Type.NodeSet, new JavaType(String.class, 3));
            _internal2Java.put(Type.Node, new JavaType(nodeListClass, 0));
            _internal2Java.put(Type.Node, new JavaType(nodeClass, 1));
            _internal2Java.put(Type.Node, new JavaType(Object.class, 2));
            _internal2Java.put(Type.Node, new JavaType(String.class, 3));
            _internal2Java.put(Type.ResultTree, new JavaType(nodeListClass, 0));
            _internal2Java.put(Type.ResultTree, new JavaType(nodeClass, 1));
            _internal2Java.put(Type.ResultTree, new JavaType(Object.class, 2));
            _internal2Java.put(Type.ResultTree, new JavaType(String.class, 3));
            _internal2Java.put(Type.Reference, new JavaType(Object.class, 0));
            _internal2Java.makeUnmodifiable();
            Map<Class<?>, Type> java2Internal = new HashMap<>();
            Map<String, String> extensionNamespaceTable = new HashMap<>();
            Map<String, String> extensionFunctionTable = new HashMap<>();
            java2Internal.put(Boolean.TYPE, Type.Boolean);
            java2Internal.put(Void.TYPE, Type.Void);
            java2Internal.put(Character.TYPE, Type.Real);
            java2Internal.put(Byte.TYPE, Type.Real);
            java2Internal.put(Short.TYPE, Type.Real);
            java2Internal.put(Integer.TYPE, Type.Real);
            java2Internal.put(Long.TYPE, Type.Real);
            java2Internal.put(Float.TYPE, Type.Real);
            java2Internal.put(Double.TYPE, Type.Real);
            java2Internal.put(String.class, Type.String);
            java2Internal.put(Object.class, Type.Reference);
            java2Internal.put(nodeListClass, Type.NodeSet);
            java2Internal.put(nodeClass, Type.NodeSet);
            extensionNamespaceTable.put("http://xml.apache.org/xalan", "com.sun.org.apache.xalan.internal.lib.Extensions");
            extensionNamespaceTable.put("http://exslt.org/common", "com.sun.org.apache.xalan.internal.lib.ExsltCommon");
            extensionNamespaceTable.put("http://exslt.org/math", "com.sun.org.apache.xalan.internal.lib.ExsltMath");
            extensionNamespaceTable.put("http://exslt.org/sets", "com.sun.org.apache.xalan.internal.lib.ExsltSets");
            extensionNamespaceTable.put("http://exslt.org/dates-and-times", "com.sun.org.apache.xalan.internal.lib.ExsltDatetime");
            extensionNamespaceTable.put("http://exslt.org/strings", "com.sun.org.apache.xalan.internal.lib.ExsltStrings");
            extensionFunctionTable.put("http://exslt.org/common:nodeSet", "nodeset");
            extensionFunctionTable.put("http://exslt.org/common:objectType", "objectType");
            extensionFunctionTable.put("http://xml.apache.org/xalan:nodeset", "nodeset");
            JAVA2INTERNAL = Collections.unmodifiableMap(java2Internal);
            EXTENSIONNAMESPACE = Collections.unmodifiableMap(extensionNamespaceTable);
            EXTENSIONFUNCTION = Collections.unmodifiableMap(extensionFunctionTable);
        } catch (ClassNotFoundException e2) {
            ErrorMsg err = new ErrorMsg(ErrorMsg.CLASS_NOT_FOUND_ERR, "org.w3c.dom.Node or NodeList");
            throw new ExceptionInInitializerError(err.toString());
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/FunctionCall$JavaType.class */
    static class JavaType {
        public Class<?> type;
        public int distance;

        public JavaType(Class type, int distance) {
            this.type = type;
            this.distance = distance;
        }

        public int hashCode() {
            return Objects.hashCode(this.type);
        }

        public boolean equals(Object query) {
            if (query == null) {
                return false;
            }
            if (query.getClass().isAssignableFrom(JavaType.class)) {
                return ((JavaType) query).type.equals(this.type);
            }
            return query.equals(this.type);
        }
    }

    public FunctionCall(QName fname, Vector arguments) {
        this._namespace_format = 0;
        this._thisArgument = null;
        this._isExtConstructor = false;
        this._isStatic = false;
        this._fname = fname;
        this._arguments = arguments;
        this._type = null;
    }

    public FunctionCall(QName fname) {
        this(fname, EMPTY_ARG_LIST);
    }

    public String getName() {
        return this._fname.toString();
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void setParser(Parser parser) {
        super.setParser(parser);
        if (this._arguments != null) {
            int n2 = this._arguments.size();
            for (int i2 = 0; i2 < n2; i2++) {
                Expression exp = (Expression) this._arguments.elementAt(i2);
                exp.setParser(parser);
                exp.setParent(this);
            }
        }
    }

    public String getClassNameFromUri(String uri) {
        String className = EXTENSIONNAMESPACE.get(uri);
        if (className != null) {
            return className;
        }
        if (uri.startsWith(JAVA_EXT_XSLTC)) {
            int length = JAVA_EXT_XSLTC.length() + 1;
            return uri.length() > length ? uri.substring(length) : "";
        }
        if (uri.startsWith("http://xml.apache.org/xalan/java")) {
            int length2 = "http://xml.apache.org/xalan/java".length() + 1;
            return uri.length() > length2 ? uri.substring(length2) : "";
        }
        if (uri.startsWith("http://xml.apache.org/xslt/java")) {
            int length3 = "http://xml.apache.org/xslt/java".length() + 1;
            return uri.length() > length3 ? uri.substring(length3) : "";
        }
        int index = uri.lastIndexOf(47);
        return index > 0 ? uri.substring(index + 1) : uri;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        if (this._type != null) {
            return this._type;
        }
        String namespace = this._fname.getNamespace();
        String local = this._fname.getLocalPart();
        if (isExtension()) {
            this._fname = new QName(null, null, local);
            return typeCheckStandard(stable);
        }
        if (isStandard()) {
            return typeCheckStandard(stable);
        }
        try {
            this._className = getClassNameFromUri(namespace);
            int pos = local.lastIndexOf(46);
            if (pos > 0) {
                this._isStatic = true;
                if (this._className != null && this._className.length() > 0) {
                    this._namespace_format = 2;
                    this._className += "." + local.substring(0, pos);
                } else {
                    this._namespace_format = 0;
                    this._className = local.substring(0, pos);
                }
                this._fname = new QName(namespace, null, local.substring(pos + 1));
            } else {
                if (this._className != null && this._className.length() > 0) {
                    try {
                        this._clazz = ObjectFactory.findProviderClass(this._className, true);
                        this._namespace_format = 1;
                    } catch (ClassNotFoundException e2) {
                        this._namespace_format = 2;
                    }
                } else {
                    this._namespace_format = 0;
                }
                if (local.indexOf(45) > 0) {
                    local = replaceDash(local);
                }
                String extFunction = EXTENSIONFUNCTION.get(namespace + CallSiteDescriptor.TOKEN_DELIMITER + local);
                if (extFunction != null) {
                    this._fname = new QName(null, null, extFunction);
                    return typeCheckStandard(stable);
                }
                this._fname = new QName(namespace, null, local);
            }
            return typeCheckExternal(stable);
        } catch (TypeCheckError e3) {
            ErrorMsg errorMsg = e3.getErrorMsg();
            if (errorMsg == null) {
                String name = this._fname.getLocalPart();
                errorMsg = new ErrorMsg(ErrorMsg.METHOD_NOT_FOUND_ERR, name);
            }
            getParser().reportError(3, errorMsg);
            Type type = Type.Void;
            this._type = type;
            return type;
        }
    }

    public Type typeCheckStandard(SymbolTable stable) throws TypeCheckError {
        this._fname.clearNamespace();
        int n2 = this._arguments.size();
        Vector argsType = typeCheckArgs(stable);
        MethodType args = new MethodType(Type.Void, argsType);
        MethodType ptype = lookupPrimop(stable, this._fname.getLocalPart(), args);
        if (ptype != null) {
            for (int i2 = 0; i2 < n2; i2++) {
                Type argType = (Type) ptype.argsType().elementAt(i2);
                Expression exp = (Expression) this._arguments.elementAt(i2);
                if (!argType.identicalTo(exp.getType())) {
                    try {
                        this._arguments.setElementAt(new CastExpr(exp, argType), i2);
                    } catch (TypeCheckError e2) {
                        throw new TypeCheckError(this);
                    }
                }
            }
            this._chosenMethodType = ptype;
            Type typeResultType = ptype.resultType();
            this._type = typeResultType;
            return typeResultType;
        }
        throw new TypeCheckError(this);
    }

    public Type typeCheckConstructor(SymbolTable stable) throws TypeCheckError, SecurityException {
        Vector constructors = findConstructors();
        if (constructors == null) {
            throw new TypeCheckError(ErrorMsg.CONSTRUCTOR_NOT_FOUND, this._className);
        }
        int nConstructors = constructors.size();
        int nArgs = this._arguments.size();
        Vector argsType = typeCheckArgs(stable);
        int bestConstrDistance = Integer.MAX_VALUE;
        this._type = null;
        for (int i2 = 0; i2 < nConstructors; i2++) {
            Constructor constructor = (Constructor) constructors.elementAt(i2);
            Class[] paramTypes = constructor.getParameterTypes();
            int currConstrDistance = 0;
            int j2 = 0;
            while (true) {
                if (j2 >= nArgs) {
                    break;
                }
                Class extType = paramTypes[j2];
                Type intType = (Type) argsType.elementAt(j2);
                JavaType match = _internal2Java.maps(intType, new JavaType(extType, 0));
                if (match != null) {
                    currConstrDistance += match.distance;
                } else if (intType instanceof ObjectType) {
                    ObjectType objectType = (ObjectType) intType;
                    if (objectType.getJavaClass() != extType) {
                        if (extType.isAssignableFrom(objectType.getJavaClass())) {
                            currConstrDistance++;
                        } else {
                            currConstrDistance = Integer.MAX_VALUE;
                            break;
                        }
                    } else {
                        continue;
                    }
                } else {
                    currConstrDistance = Integer.MAX_VALUE;
                    break;
                }
                j2++;
            }
            if (j2 == nArgs && currConstrDistance < bestConstrDistance) {
                this._chosenConstructor = constructor;
                this._isExtConstructor = true;
                bestConstrDistance = currConstrDistance;
                this._type = this._clazz != null ? Type.newObjectType(this._clazz) : Type.newObjectType(this._className);
            }
        }
        if (this._type != null) {
            return this._type;
        }
        throw new TypeCheckError(ErrorMsg.ARGUMENT_CONVERSION_ERR, getMethodSignature(argsType));
    }

    public Type typeCheckExternal(SymbolTable stable) throws TypeCheckError, SecurityException {
        int nArgs = this._arguments.size();
        String name = this._fname.getLocalPart();
        if (this._fname.getLocalPart().equals("new")) {
            return typeCheckConstructor(stable);
        }
        boolean hasThisArgument = false;
        if (nArgs == 0) {
            this._isStatic = true;
        }
        if (!this._isStatic) {
            if (this._namespace_format == 0 || this._namespace_format == 2) {
                hasThisArgument = true;
            }
            Expression firstArg = (Expression) this._arguments.elementAt(0);
            Type firstArgType = firstArg.typeCheck(stable);
            if (this._namespace_format == 1 && (firstArgType instanceof ObjectType) && this._clazz != null && this._clazz.isAssignableFrom(((ObjectType) firstArgType).getJavaClass())) {
                hasThisArgument = true;
            }
            if (hasThisArgument) {
                this._thisArgument = (Expression) this._arguments.elementAt(0);
                this._arguments.remove(0);
                nArgs--;
                if (firstArgType instanceof ObjectType) {
                    this._className = ((ObjectType) firstArgType).getJavaClassName();
                } else {
                    throw new TypeCheckError(ErrorMsg.NO_JAVA_FUNCT_THIS_REF, name);
                }
            }
        } else if (this._className.length() == 0) {
            Parser parser = getParser();
            if (parser != null) {
                reportWarning(this, parser, ErrorMsg.FUNCTION_RESOLVE_ERR, this._fname.toString());
            }
            this.unresolvedExternal = true;
            Type type = Type.Int;
            this._type = type;
            return type;
        }
        Vector methods = findMethods();
        if (methods == null) {
            throw new TypeCheckError(ErrorMsg.METHOD_NOT_FOUND_ERR, this._className + "." + name);
        }
        int nMethods = methods.size();
        Vector argsType = typeCheckArgs(stable);
        int bestMethodDistance = Integer.MAX_VALUE;
        this._type = null;
        for (int i2 = 0; i2 < nMethods; i2++) {
            Method method = (Method) methods.elementAt(i2);
            Class[] paramTypes = method.getParameterTypes();
            int currMethodDistance = 0;
            int j2 = 0;
            while (true) {
                if (j2 >= nArgs) {
                    break;
                }
                Class extType = paramTypes[j2];
                Type intType = (Type) argsType.elementAt(j2);
                JavaType match = _internal2Java.maps(intType, new JavaType(extType, 0));
                if (match != null) {
                    currMethodDistance += match.distance;
                } else if (intType instanceof ReferenceType) {
                    currMethodDistance++;
                } else if (intType instanceof ObjectType) {
                    ObjectType object = (ObjectType) intType;
                    if (extType.getName().equals(object.getJavaClassName())) {
                        currMethodDistance += 0;
                    } else if (extType.isAssignableFrom(object.getJavaClass())) {
                        currMethodDistance++;
                    } else {
                        currMethodDistance = Integer.MAX_VALUE;
                        break;
                    }
                } else {
                    currMethodDistance = Integer.MAX_VALUE;
                    break;
                }
                j2++;
            }
            if (j2 == nArgs) {
                Class extType2 = method.getReturnType();
                this._type = JAVA2INTERNAL.get(extType2);
                if (this._type == null) {
                    this._type = Type.newObjectType(extType2);
                }
                if (this._type != null && currMethodDistance < bestMethodDistance) {
                    this._chosenMethod = method;
                    bestMethodDistance = currMethodDistance;
                }
            }
        }
        if (this._chosenMethod != null && this._thisArgument == null && !Modifier.isStatic(this._chosenMethod.getModifiers())) {
            throw new TypeCheckError(ErrorMsg.NO_JAVA_FUNCT_THIS_REF, getMethodSignature(argsType));
        }
        if (this._type != null) {
            if (this._type == Type.NodeSet) {
                getXSLTC().setMultiDocument(true);
            }
            return this._type;
        }
        throw new TypeCheckError(ErrorMsg.ARGUMENT_CONVERSION_ERR, getMethodSignature(argsType));
    }

    public Vector typeCheckArgs(SymbolTable stable) throws TypeCheckError {
        Vector result = new Vector();
        Enumeration e2 = this._arguments.elements();
        while (e2.hasMoreElements()) {
            Expression exp = (Expression) e2.nextElement2();
            result.addElement(exp.typeCheck(stable));
        }
        return result;
    }

    protected final Expression argument(int i2) {
        return (Expression) this._arguments.elementAt(i2);
    }

    protected final Expression argument() {
        return argument(0);
    }

    protected final int argumentCount() {
        return this._arguments.size();
    }

    protected final void setArgument(int i2, Expression exp) {
        this._arguments.setElementAt(exp, i2);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression
    public void translateDesynthesized(ClassGenerator classGen, MethodGenerator methodGen) {
        Type type = Type.Boolean;
        if (this._chosenMethodType != null) {
            type = this._chosenMethodType.resultType();
        }
        InstructionList il = methodGen.getInstructionList();
        translate(classGen, methodGen);
        if ((type instanceof BooleanType) || (type instanceof IntType)) {
            this._falseList.add(il.append((BranchInstruction) new IFEQ(null)));
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        int n2 = argumentCount();
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        boolean isSecureProcessing = classGen.getParser().getXSLTC().isSecureProcessing();
        boolean isExtensionFunctionEnabled = classGen.getParser().getXSLTC().getFeature(JdkXmlFeatures.XmlFeature.ENABLE_EXTENSION_FUNCTION);
        if (isStandard() || isExtension()) {
            for (int i2 = 0; i2 < n2; i2++) {
                Expression exp = argument(i2);
                exp.translate(classGen, methodGen);
                exp.startIterator(classGen, methodGen);
            }
            String name = this._fname.toString().replace('-', '_') + PdfOps.F_TOKEN;
            String args = "";
            if (name.equals("sumF")) {
                args = Constants.DOM_INTF_SIG;
                il.append(methodGen.loadDOM());
            } else if (name.equals("normalize_spaceF") && this._chosenMethodType.toSignature(args).equals("()Ljava/lang/String;")) {
                args = "ILcom/sun/org/apache/xalan/internal/xsltc/DOM;";
                il.append(methodGen.loadContextNode());
                il.append(methodGen.loadDOM());
            }
            il.append(new INVOKESTATIC(cpg.addMethodref(Constants.BASIS_LIBRARY_CLASS, name, this._chosenMethodType.toSignature(args))));
            return;
        }
        if (this.unresolvedExternal) {
            int index = cpg.addMethodref(Constants.BASIS_LIBRARY_CLASS, "unresolved_externalF", "(Ljava/lang/String;)V");
            il.append(new PUSH(cpg, this._fname.toString()));
            il.append(new INVOKESTATIC(index));
            return;
        }
        if (this._isExtConstructor) {
            if (isSecureProcessing && !isExtensionFunctionEnabled) {
                translateUnallowedExtension(cpg, il);
            }
            String clazz = this._chosenConstructor.getDeclaringClass().getName();
            Class[] paramTypes = this._chosenConstructor.getParameterTypes();
            LocalVariableGen[] paramTemp = new LocalVariableGen[n2];
            for (int i3 = 0; i3 < n2; i3++) {
                Expression exp2 = argument(i3);
                Type expType = exp2.getType();
                exp2.translate(classGen, methodGen);
                exp2.startIterator(classGen, methodGen);
                expType.translateTo(classGen, methodGen, paramTypes[i3]);
                paramTemp[i3] = methodGen.addLocalVariable("function_call_tmp" + i3, expType.toJCType(), null, null);
                paramTemp[i3].setStart(il.append(expType.STORE(paramTemp[i3].getIndex())));
            }
            il.append(new NEW(cpg.addClass(this._className)));
            il.append(InstructionConstants.DUP);
            for (int i4 = 0; i4 < n2; i4++) {
                Expression arg = argument(i4);
                paramTemp[i4].setEnd(il.append(arg.getType().LOAD(paramTemp[i4].getIndex())));
            }
            StringBuffer buffer = new StringBuffer();
            buffer.append('(');
            for (Class cls : paramTypes) {
                buffer.append(getSignature(cls));
            }
            buffer.append(')');
            buffer.append("V");
            il.append(new INVOKESPECIAL(cpg.addMethodref(clazz, com.sun.org.apache.bcel.internal.Constants.CONSTRUCTOR_NAME, buffer.toString())));
            Type.Object.translateFrom(classGen, methodGen, this._chosenConstructor.getDeclaringClass());
            return;
        }
        if (isSecureProcessing && !isExtensionFunctionEnabled) {
            translateUnallowedExtension(cpg, il);
        }
        String clazz2 = this._chosenMethod.getDeclaringClass().getName();
        Class[] paramTypes2 = this._chosenMethod.getParameterTypes();
        if (this._thisArgument != null) {
            this._thisArgument.translate(classGen, methodGen);
        }
        for (int i5 = 0; i5 < n2; i5++) {
            Expression exp3 = argument(i5);
            exp3.translate(classGen, methodGen);
            exp3.startIterator(classGen, methodGen);
            exp3.getType().translateTo(classGen, methodGen, paramTypes2[i5]);
        }
        StringBuffer buffer2 = new StringBuffer();
        buffer2.append('(');
        for (Class cls2 : paramTypes2) {
            buffer2.append(getSignature(cls2));
        }
        buffer2.append(')');
        buffer2.append(getSignature(this._chosenMethod.getReturnType()));
        if (this._thisArgument != null && this._clazz.isInterface()) {
            il.append(new INVOKEINTERFACE(cpg.addInterfaceMethodref(clazz2, this._fname.getLocalPart(), buffer2.toString()), n2 + 1));
        } else {
            int index2 = cpg.addMethodref(clazz2, this._fname.getLocalPart(), buffer2.toString());
            il.append(this._thisArgument != null ? new INVOKEVIRTUAL(index2) : new INVOKESTATIC(index2));
        }
        this._type.translateFrom(classGen, methodGen, this._chosenMethod.getReturnType());
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression
    public String toString() {
        return "funcall(" + ((Object) this._fname) + ", " + ((Object) this._arguments) + ')';
    }

    public boolean isStandard() {
        String namespace = this._fname.getNamespace();
        return namespace == null || namespace.equals("");
    }

    public boolean isExtension() {
        String namespace = this._fname.getNamespace();
        return namespace != null && namespace.equals("http://xml.apache.org/xalan/xsltc");
    }

    private Vector findMethods() throws SecurityException {
        Vector result = null;
        String namespace = this._fname.getNamespace();
        if (this._className != null && this._className.length() > 0) {
            int nArgs = this._arguments.size();
            try {
                if (this._clazz == null) {
                    boolean isSecureProcessing = getXSLTC().isSecureProcessing();
                    boolean isExtensionFunctionEnabled = getXSLTC().getFeature(JdkXmlFeatures.XmlFeature.ENABLE_EXTENSION_FUNCTION);
                    if (namespace != null && isSecureProcessing && isExtensionFunctionEnabled && (namespace.startsWith("http://xml.apache.org/xalan/java") || namespace.startsWith(JAVA_EXT_XSLTC) || namespace.startsWith("http://xml.apache.org/xslt/java") || namespace.startsWith(XALAN_CLASSPACKAGE_NAMESPACE))) {
                        this._clazz = getXSLTC().loadExternalFunction(this._className);
                    } else {
                        this._clazz = ObjectFactory.findProviderClass(this._className, true);
                    }
                    if (this._clazz == null) {
                        ErrorMsg msg = new ErrorMsg(ErrorMsg.CLASS_NOT_FOUND_ERR, this._className);
                        getParser().reportError(3, msg);
                    }
                }
                String methodName = this._fname.getLocalPart();
                Method[] methods = this._clazz.getMethods();
                for (int i2 = 0; i2 < methods.length; i2++) {
                    int mods = methods[i2].getModifiers();
                    if (Modifier.isPublic(mods) && methods[i2].getName().equals(methodName) && methods[i2].getParameterTypes().length == nArgs) {
                        if (result == null) {
                            result = new Vector();
                        }
                        result.addElement(methods[i2]);
                    }
                }
            } catch (ClassNotFoundException e2) {
                ErrorMsg msg2 = new ErrorMsg(ErrorMsg.CLASS_NOT_FOUND_ERR, this._className);
                getParser().reportError(3, msg2);
            }
        }
        return result;
    }

    private Vector findConstructors() throws SecurityException {
        Vector result = null;
        this._fname.getNamespace();
        int nArgs = this._arguments.size();
        try {
            if (this._clazz == null) {
                this._clazz = ObjectFactory.findProviderClass(this._className, true);
                if (this._clazz == null) {
                    ErrorMsg msg = new ErrorMsg(ErrorMsg.CLASS_NOT_FOUND_ERR, this._className);
                    getParser().reportError(3, msg);
                }
            }
            Constructor[] constructors = this._clazz.getConstructors();
            for (int i2 = 0; i2 < constructors.length; i2++) {
                int mods = constructors[i2].getModifiers();
                if (Modifier.isPublic(mods) && constructors[i2].getParameterTypes().length == nArgs) {
                    if (result == null) {
                        result = new Vector();
                    }
                    result.addElement(constructors[i2]);
                }
            }
        } catch (ClassNotFoundException e2) {
            ErrorMsg msg2 = new ErrorMsg(ErrorMsg.CLASS_NOT_FOUND_ERR, this._className);
            getParser().reportError(3, msg2);
        }
        return result;
    }

    static final String getSignature(Class clazz) {
        if (clazz.isArray()) {
            StringBuffer sb = new StringBuffer();
            Class componentType = clazz;
            while (true) {
                Class cl = componentType;
                if (cl.isArray()) {
                    sb.append("[");
                    componentType = cl.getComponentType();
                } else {
                    sb.append(getSignature(cl));
                    return sb.toString();
                }
            }
        } else {
            if (clazz.isPrimitive()) {
                if (clazz == Integer.TYPE) {
                    return "I";
                }
                if (clazz == Byte.TYPE) {
                    return PdfOps.B_TOKEN;
                }
                if (clazz == Long.TYPE) {
                    return "J";
                }
                if (clazz == Float.TYPE) {
                    return PdfOps.F_TOKEN;
                }
                if (clazz == Double.TYPE) {
                    return PdfOps.D_TOKEN;
                }
                if (clazz == Short.TYPE) {
                    return PdfOps.S_TOKEN;
                }
                if (clazz == Character.TYPE) {
                    return "C";
                }
                if (clazz == Boolean.TYPE) {
                    return Constants.HASIDCALL_INDEX_SIG;
                }
                if (clazz == Void.TYPE) {
                    return "V";
                }
                String name = clazz.toString();
                ErrorMsg err = new ErrorMsg(ErrorMsg.UNKNOWN_SIG_TYPE_ERR, name);
                throw new Error(err.toString());
            }
            return "L" + clazz.getName().replace('.', '/') + ';';
        }
    }

    static final String getSignature(Method meth) {
        StringBuffer sb = new StringBuffer();
        sb.append('(');
        Class[] params = meth.getParameterTypes();
        for (Class cls : params) {
            sb.append(getSignature(cls));
        }
        return sb.append(')').append(getSignature(meth.getReturnType())).toString();
    }

    static final String getSignature(Constructor cons) {
        StringBuffer sb = new StringBuffer();
        sb.append('(');
        Class[] params = cons.getParameterTypes();
        for (Class cls : params) {
            sb.append(getSignature(cls));
        }
        return sb.append(")V").toString();
    }

    private String getMethodSignature(Vector argsType) {
        StringBuffer buf = new StringBuffer(this._className);
        buf.append('.').append(this._fname.getLocalPart()).append('(');
        int nArgs = argsType.size();
        for (int i2 = 0; i2 < nArgs; i2++) {
            Type intType = (Type) argsType.elementAt(i2);
            buf.append(intType.toString());
            if (i2 < nArgs - 1) {
                buf.append(", ");
            }
        }
        buf.append(')');
        return buf.toString();
    }

    protected static String replaceDash(String name) {
        StringBuilder buff = new StringBuilder("");
        for (int i2 = 0; i2 < name.length(); i2++) {
            if (i2 <= 0 || name.charAt(i2 - 1) != '-') {
                if (name.charAt(i2) != '-') {
                    buff.append(name.charAt(i2));
                }
            } else {
                buff.append(Character.toUpperCase(name.charAt(i2)));
            }
        }
        return buff.toString();
    }

    private void translateUnallowedExtension(ConstantPoolGen cpg, InstructionList il) {
        int index = cpg.addMethodref(Constants.BASIS_LIBRARY_CLASS, "unallowed_extension_functionF", "(Ljava/lang/String;)V");
        il.append(new PUSH(cpg, this._fname.toString()));
        il.append(new INVOKESTATIC(index));
    }
}
