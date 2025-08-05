package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.PUSH;
import com.sun.org.apache.xalan.internal.utils.ConfigurationError;
import com.sun.org.apache.xalan.internal.utils.ObjectFactory;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
import com.sun.org.apache.xpath.internal.compiler.Keywords;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Vector;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/FunctionAvailableCall.class */
final class FunctionAvailableCall extends FunctionCall {
    private Expression _arg;
    private String _nameOfFunct;
    private String _namespaceOfFunct;
    private boolean _isFunctionAvailable;

    public FunctionAvailableCall(QName fname, Vector arguments) {
        super(fname, arguments);
        this._nameOfFunct = null;
        this._namespaceOfFunct = null;
        this._isFunctionAvailable = false;
        this._arg = (Expression) arguments.elementAt(0);
        this._type = null;
        if (this._arg instanceof LiteralExpr) {
            LiteralExpr arg = (LiteralExpr) this._arg;
            this._namespaceOfFunct = arg.getNamespace();
            this._nameOfFunct = arg.getValue();
            if (!isInternalNamespace()) {
                this._isFunctionAvailable = hasMethods();
            }
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.FunctionCall, com.sun.org.apache.xalan.internal.xsltc.compiler.Expression, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        if (this._type != null) {
            return this._type;
        }
        if (this._arg instanceof LiteralExpr) {
            Type type = Type.Boolean;
            this._type = type;
            return type;
        }
        ErrorMsg err = new ErrorMsg(ErrorMsg.NEED_LITERAL_ERR, (Object) Keywords.FUNC_EXT_FUNCTION_AVAILABLE_STRING, (SyntaxTreeNode) this);
        throw new TypeCheckError(err);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression
    public Object evaluateAtCompileTime() {
        return getResult() ? Boolean.TRUE : Boolean.FALSE;
    }

    private boolean hasMethods() throws SecurityException, ConfigurationError {
        String methodName;
        String className = getClassNameFromUri(this._namespaceOfFunct);
        int colonIndex = this._nameOfFunct.indexOf(58);
        if (colonIndex > 0) {
            String functionName = this._nameOfFunct.substring(colonIndex + 1);
            int lastDotIndex = functionName.lastIndexOf(46);
            if (lastDotIndex > 0) {
                methodName = functionName.substring(lastDotIndex + 1);
                className = (className == null || className.length() == 0) ? functionName.substring(0, lastDotIndex) : className + "." + functionName.substring(0, lastDotIndex);
            } else {
                methodName = functionName;
            }
        } else {
            methodName = this._nameOfFunct;
        }
        if (className == null || methodName == null) {
            return false;
        }
        if (methodName.indexOf(45) > 0) {
            methodName = replaceDash(methodName);
        }
        try {
            Class clazz = ObjectFactory.findProviderClass(className, true);
            if (clazz == null) {
                return false;
            }
            Method[] methods = clazz.getMethods();
            for (int i2 = 0; i2 < methods.length; i2++) {
                int mods = methods[i2].getModifiers();
                if (Modifier.isPublic(mods) && Modifier.isStatic(mods) && methods[i2].getName().equals(methodName)) {
                    return true;
                }
            }
            return false;
        } catch (ClassNotFoundException e2) {
            return false;
        }
    }

    public boolean getResult() {
        if (this._nameOfFunct == null) {
            return false;
        }
        if (isInternalNamespace()) {
            Parser parser = getParser();
            this._isFunctionAvailable = parser.functionSupported(Util.getLocalName(this._nameOfFunct));
        }
        return this._isFunctionAvailable;
    }

    private boolean isInternalNamespace() {
        return this._namespaceOfFunct == null || this._namespaceOfFunct.equals("") || this._namespaceOfFunct.equals(Constants.TRANSLET_URI);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.FunctionCall, com.sun.org.apache.xalan.internal.xsltc.compiler.Expression, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        methodGen.getInstructionList().append(new PUSH(cpg, getResult()));
    }
}
