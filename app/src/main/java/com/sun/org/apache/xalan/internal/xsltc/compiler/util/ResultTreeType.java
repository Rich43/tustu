package com.sun.org.apache.xalan.internal.xsltc.compiler.util;

import com.sun.org.apache.bcel.internal.generic.ALOAD;
import com.sun.org.apache.bcel.internal.generic.ASTORE;
import com.sun.org.apache.bcel.internal.generic.BranchInstruction;
import com.sun.org.apache.bcel.internal.generic.CHECKCAST;
import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.GETFIELD;
import com.sun.org.apache.bcel.internal.generic.IFEQ;
import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
import com.sun.org.apache.bcel.internal.generic.INVOKESPECIAL;
import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
import com.sun.org.apache.bcel.internal.generic.Instruction;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.LocalVariableGen;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.sun.org.apache.bcel.internal.generic.PUSH;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import com.sun.org.apache.xalan.internal.xsltc.compiler.FlowList;
import com.sun.org.apache.xpath.internal.compiler.Keywords;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/util/ResultTreeType.class */
public final class ResultTreeType extends Type {
    private final String _methodName;

    protected ResultTreeType() {
        this._methodName = null;
    }

    public ResultTreeType(String methodName) {
        this._methodName = methodName;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public String toString() {
        return "result-tree";
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public boolean identicalTo(Type other) {
        return other instanceof ResultTreeType;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public String toSignature() {
        return Constants.DOM_INTF_SIG;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public com.sun.org.apache.bcel.internal.generic.Type toJCType() {
        return Util.getJCRefType(toSignature());
    }

    public String getMethodName() {
        return this._methodName;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public boolean implementedAsMethod() {
        return this._methodName != null;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, Type type) {
        if (type == Type.String) {
            translateTo(classGen, methodGen, (StringType) type);
            return;
        }
        if (type == Type.Boolean) {
            translateTo(classGen, methodGen, (BooleanType) type);
            return;
        }
        if (type == Type.Real) {
            translateTo(classGen, methodGen, (RealType) type);
            return;
        }
        if (type == Type.NodeSet) {
            translateTo(classGen, methodGen, (NodeSetType) type);
            return;
        }
        if (type == Type.Reference) {
            translateTo(classGen, methodGen, (ReferenceType) type);
        } else if (type == Type.Object) {
            translateTo(classGen, methodGen, (ObjectType) type);
        } else {
            ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", toString(), type.toString());
            classGen.getParser().reportError(2, err);
        }
    }

    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, BooleanType type) {
        classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        il.append(POP);
        il.append(ICONST_1);
    }

    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, StringType type) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        if (this._methodName == null) {
            int index = cpg.addInterfaceMethodref(Constants.DOM_INTF, "getStringValue", "()Ljava/lang/String;");
            il.append(new INVOKEINTERFACE(index, 1));
            return;
        }
        String className = classGen.getClassName();
        methodGen.getLocalIndex(Keywords.FUNC_CURRENT_STRING);
        il.append(classGen.loadTranslet());
        if (classGen.isExternal()) {
            il.append(new CHECKCAST(cpg.addClass(className)));
        }
        il.append(DUP);
        il.append(new GETFIELD(cpg.addFieldref(className, Constants.DOM_FIELD, Constants.DOM_INTF_SIG)));
        int index2 = cpg.addMethodref(Constants.STRING_VALUE_HANDLER, com.sun.org.apache.bcel.internal.Constants.CONSTRUCTOR_NAME, "()V");
        il.append(new NEW(cpg.addClass(Constants.STRING_VALUE_HANDLER)));
        il.append(DUP);
        il.append(DUP);
        il.append(new INVOKESPECIAL(index2));
        LocalVariableGen handler = methodGen.addLocalVariable("rt_to_string_handler", Util.getJCRefType(Constants.STRING_VALUE_HANDLER_SIG), null, null);
        handler.setStart(il.append(new ASTORE(handler.getIndex())));
        int index3 = cpg.addMethodref(className, this._methodName, "(Lcom/sun/org/apache/xalan/internal/xsltc/DOM;Lcom/sun/org/apache/xml/internal/serializer/SerializationHandler;)V");
        il.append(new INVOKEVIRTUAL(index3));
        handler.setEnd(il.append(new ALOAD(handler.getIndex())));
        int index4 = cpg.addMethodref(Constants.STRING_VALUE_HANDLER, "getValue", "()Ljava/lang/String;");
        il.append(new INVOKEVIRTUAL(index4));
    }

    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, RealType type) {
        translateTo(classGen, methodGen, Type.String);
        Type.String.translateTo(classGen, methodGen, Type.Real);
    }

    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, ReferenceType type) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        if (this._methodName == null) {
            il.append(NOP);
            return;
        }
        String className = classGen.getClassName();
        methodGen.getLocalIndex(Keywords.FUNC_CURRENT_STRING);
        il.append(classGen.loadTranslet());
        if (classGen.isExternal()) {
            il.append(new CHECKCAST(cpg.addClass(className)));
        }
        il.append(methodGen.loadDOM());
        il.append(methodGen.loadDOM());
        int index = cpg.addInterfaceMethodref(Constants.DOM_INTF, "getResultTreeFrag", "(IZ)Lcom/sun/org/apache/xalan/internal/xsltc/DOM;");
        il.append(new PUSH(cpg, 32));
        il.append(new PUSH(cpg, false));
        il.append(new INVOKEINTERFACE(index, 3));
        il.append(DUP);
        LocalVariableGen newDom = methodGen.addLocalVariable("rt_to_reference_dom", Util.getJCRefType(Constants.DOM_INTF_SIG), null, null);
        il.append(new CHECKCAST(cpg.addClass(Constants.DOM_INTF_SIG)));
        newDom.setStart(il.append(new ASTORE(newDom.getIndex())));
        int index2 = cpg.addInterfaceMethodref(Constants.DOM_INTF, "getOutputDomBuilder", "()Lcom/sun/org/apache/xml/internal/serializer/SerializationHandler;");
        il.append(new INVOKEINTERFACE(index2, 1));
        il.append(DUP);
        il.append(DUP);
        LocalVariableGen domBuilder = methodGen.addLocalVariable("rt_to_reference_handler", Util.getJCRefType("Lcom/sun/org/apache/xml/internal/serializer/SerializationHandler;"), null, null);
        domBuilder.setStart(il.append(new ASTORE(domBuilder.getIndex())));
        int index3 = cpg.addInterfaceMethodref(Constants.TRANSLET_OUTPUT_INTERFACE, "startDocument", "()V");
        il.append(new INVOKEINTERFACE(index3, 1));
        int index4 = cpg.addMethodref(className, this._methodName, "(Lcom/sun/org/apache/xalan/internal/xsltc/DOM;Lcom/sun/org/apache/xml/internal/serializer/SerializationHandler;)V");
        il.append(new INVOKEVIRTUAL(index4));
        domBuilder.setEnd(il.append(new ALOAD(domBuilder.getIndex())));
        int index5 = cpg.addInterfaceMethodref(Constants.TRANSLET_OUTPUT_INTERFACE, "endDocument", "()V");
        il.append(new INVOKEINTERFACE(index5, 1));
        newDom.setEnd(il.append(new ALOAD(newDom.getIndex())));
    }

    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, NodeSetType type) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        il.append(DUP);
        il.append(classGen.loadTranslet());
        il.append(new GETFIELD(cpg.addFieldref(Constants.TRANSLET_CLASS, Constants.NAMES_INDEX, "[Ljava/lang/String;")));
        il.append(classGen.loadTranslet());
        il.append(new GETFIELD(cpg.addFieldref(Constants.TRANSLET_CLASS, Constants.URIS_INDEX, "[Ljava/lang/String;")));
        il.append(classGen.loadTranslet());
        il.append(new GETFIELD(cpg.addFieldref(Constants.TRANSLET_CLASS, Constants.TYPES_INDEX, Constants.TYPES_INDEX_SIG)));
        il.append(classGen.loadTranslet());
        il.append(new GETFIELD(cpg.addFieldref(Constants.TRANSLET_CLASS, Constants.NAMESPACE_INDEX, "[Ljava/lang/String;")));
        int mapping = cpg.addInterfaceMethodref(Constants.DOM_INTF, "setupMapping", "([Ljava/lang/String;[Ljava/lang/String;[I[Ljava/lang/String;)V");
        il.append(new INVOKEINTERFACE(mapping, 5));
        il.append(DUP);
        int iter = cpg.addInterfaceMethodref(Constants.DOM_INTF, "getIterator", "()Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;");
        il.append(new INVOKEINTERFACE(iter, 1));
    }

    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, ObjectType type) {
        methodGen.getInstructionList().append(NOP);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public FlowList translateToDesynthesized(ClassGenerator classGen, MethodGenerator methodGen, BooleanType type) {
        InstructionList il = methodGen.getInstructionList();
        translateTo(classGen, methodGen, Type.Boolean);
        return new FlowList(il.append((BranchInstruction) new IFEQ(null)));
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, Class clazz) {
        String className = clazz.getName();
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        if (className.equals("org.w3c.dom.Node")) {
            translateTo(classGen, methodGen, Type.NodeSet);
            int index = cpg.addInterfaceMethodref(Constants.DOM_INTF, Constants.MAKE_NODE, Constants.MAKE_NODE_SIG2);
            il.append(new INVOKEINTERFACE(index, 2));
        } else if (className.equals("org.w3c.dom.NodeList")) {
            translateTo(classGen, methodGen, Type.NodeSet);
            int index2 = cpg.addInterfaceMethodref(Constants.DOM_INTF, Constants.MAKE_NODE_LIST, Constants.MAKE_NODE_LIST_SIG2);
            il.append(new INVOKEINTERFACE(index2, 2));
        } else if (className.equals(Constants.OBJECT_CLASS)) {
            il.append(NOP);
        } else if (className.equals("java.lang.String")) {
            translateTo(classGen, methodGen, Type.String);
        } else {
            ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", toString(), className);
            classGen.getParser().reportError(2, err);
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public void translateBox(ClassGenerator classGen, MethodGenerator methodGen) {
        translateTo(classGen, methodGen, Type.Reference);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public void translateUnBox(ClassGenerator classGen, MethodGenerator methodGen) {
        methodGen.getInstructionList().append(NOP);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public String getClassName() {
        return Constants.DOM_INTF;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public Instruction LOAD(int slot) {
        return new ALOAD(slot);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public Instruction STORE(int slot) {
        return new ASTORE(slot);
    }
}
