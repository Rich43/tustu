package com.sun.org.apache.xalan.internal.xsltc.compiler.util;

import com.sun.org.apache.bcel.internal.generic.ALOAD;
import com.sun.org.apache.bcel.internal.generic.ASTORE;
import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.ILOAD;
import com.sun.org.apache.bcel.internal.generic.ISTORE;
import com.sun.org.apache.bcel.internal.generic.Instruction;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.xpath.internal.compiler.Keywords;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/util/TestGenerator.class */
public final class TestGenerator extends MethodGenerator {
    private static int CONTEXT_NODE_INDEX = 1;
    private static int CURRENT_NODE_INDEX = 4;
    private static int ITERATOR_INDEX = 6;
    private Instruction _aloadDom;
    private final Instruction _iloadCurrent;
    private final Instruction _iloadContext;
    private final Instruction _istoreCurrent;
    private final Instruction _istoreContext;
    private final Instruction _astoreIterator;
    private final Instruction _aloadIterator;

    public TestGenerator(int access_flags, com.sun.org.apache.bcel.internal.generic.Type return_type, com.sun.org.apache.bcel.internal.generic.Type[] arg_types, String[] arg_names, String method_name, String class_name, InstructionList il, ConstantPoolGen cp) {
        super(access_flags, return_type, arg_types, arg_names, method_name, class_name, il, cp);
        this._iloadCurrent = new ILOAD(CURRENT_NODE_INDEX);
        this._istoreCurrent = new ISTORE(CURRENT_NODE_INDEX);
        this._iloadContext = new ILOAD(CONTEXT_NODE_INDEX);
        this._istoreContext = new ILOAD(CONTEXT_NODE_INDEX);
        this._astoreIterator = new ASTORE(ITERATOR_INDEX);
        this._aloadIterator = new ALOAD(ITERATOR_INDEX);
    }

    public int getHandlerIndex() {
        return -1;
    }

    public int getIteratorIndex() {
        return ITERATOR_INDEX;
    }

    public void setDomIndex(int domIndex) {
        this._aloadDom = new ALOAD(domIndex);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator
    public Instruction loadDOM() {
        return this._aloadDom;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator
    public Instruction loadCurrentNode() {
        return this._iloadCurrent;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator
    public Instruction loadContextNode() {
        return this._iloadContext;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator
    public Instruction storeContextNode() {
        return this._istoreContext;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator
    public Instruction storeCurrentNode() {
        return this._istoreCurrent;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator
    public Instruction storeIterator() {
        return this._astoreIterator;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator
    public Instruction loadIterator() {
        return this._aloadIterator;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator
    public int getLocalIndex(String name) {
        if (name.equals(Keywords.FUNC_CURRENT_STRING)) {
            return CURRENT_NODE_INDEX;
        }
        return super.getLocalIndex(name);
    }
}
