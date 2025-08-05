package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.classfile.Field;
import com.sun.org.apache.bcel.internal.generic.ACONST_NULL;
import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.DCONST;
import com.sun.org.apache.bcel.internal.generic.ICONST;
import com.sun.org.apache.bcel.internal.generic.InstructionHandle;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.PUTFIELD;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.BooleanType;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.IntType;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.NodeType;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.RealType;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/Variable.class */
final class Variable extends VariableBase {
    Variable() {
    }

    public int getIndex() {
        if (this._local != null) {
            return this._local.getIndex();
        }
        return -1;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.VariableBase, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void parseContents(Parser parser) {
        super.parseContents(parser);
        SyntaxTreeNode parent = getParent();
        if (parent instanceof Stylesheet) {
            this._isLocal = false;
            Variable var = parser.getSymbolTable().lookupVariable(this._name);
            if (var != null) {
                int us = getImportPrecedence();
                int them = var.getImportPrecedence();
                if (us == them) {
                    String name = this._name.toString();
                    reportError(this, parser, ErrorMsg.VARIABLE_REDEF_ERR, name);
                } else if (them > us) {
                    this._ignore = true;
                    copyReferences(var);
                    return;
                } else {
                    var.copyReferences(this);
                    var.disable();
                }
            }
            ((Stylesheet) parent).addVariable(this);
            parser.getSymbolTable().addVariable(this);
            return;
        }
        this._isLocal = true;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.TopLevelElement, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        if (this._select != null) {
            this._type = this._select.typeCheck(stable);
        } else if (hasContents()) {
            typeCheckContents(stable);
            this._type = Type.ResultTree;
        } else {
            this._type = Type.Reference;
        }
        return Type.Void;
    }

    public void initialize(ClassGenerator classGen, MethodGenerator methodGen) {
        classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        if (isLocal() && !this._refs.isEmpty()) {
            if (this._local == null) {
                this._local = methodGen.addLocalVariable2(getEscapedName(), this._type.toJCType(), null);
            }
            if ((this._type instanceof IntType) || (this._type instanceof NodeType) || (this._type instanceof BooleanType)) {
                il.append(new ICONST(0));
            } else if (this._type instanceof RealType) {
                il.append(new DCONST(0.0d));
            } else {
                il.append(new ACONST_NULL());
            }
            this._local.setStart(il.append(this._type.STORE(this._local.getIndex())));
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.TopLevelElement, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        if (this._refs.isEmpty()) {
            this._ignore = true;
        }
        if (this._ignore) {
            return;
        }
        this._ignore = true;
        String name = getEscapedName();
        if (isLocal()) {
            translateValue(classGen, methodGen);
            boolean createLocal = this._local == null;
            if (createLocal) {
                mapRegister(methodGen);
            }
            InstructionHandle storeInst = il.append(this._type.STORE(this._local.getIndex()));
            if (createLocal) {
                this._local.setStart(storeInst);
                return;
            }
            return;
        }
        String signature = this._type.toSignature();
        if (classGen.containsField(name) == null) {
            classGen.addField(new Field(1, cpg.addUtf8(name), cpg.addUtf8(signature), null, cpg.getConstantPool()));
            il.append(classGen.loadTranslet());
            translateValue(classGen, methodGen);
            il.append(new PUTFIELD(cpg.addFieldref(classGen.getClassName(), name, signature)));
        }
    }
}
