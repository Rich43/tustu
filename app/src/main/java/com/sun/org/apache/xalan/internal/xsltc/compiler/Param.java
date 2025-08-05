package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.classfile.Field;
import com.sun.org.apache.bcel.internal.generic.BranchHandle;
import com.sun.org.apache.bcel.internal.generic.BranchInstruction;
import com.sun.org.apache.bcel.internal.generic.CHECKCAST;
import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.IFNONNULL;
import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.PUSH;
import com.sun.org.apache.bcel.internal.generic.PUTFIELD;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ObjectType;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ReferenceType;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
import com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/Param.class */
final class Param extends VariableBase {
    private boolean _isInSimpleNamedTemplate = false;

    Param() {
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.VariableBase
    public String toString() {
        return "param(" + ((Object) this._name) + ")";
    }

    public com.sun.org.apache.bcel.internal.generic.Instruction setLoadInstruction(com.sun.org.apache.bcel.internal.generic.Instruction instruction) {
        com.sun.org.apache.bcel.internal.generic.Instruction tmp = this._loadInstruction;
        this._loadInstruction = instruction;
        return tmp;
    }

    public com.sun.org.apache.bcel.internal.generic.Instruction setStoreInstruction(com.sun.org.apache.bcel.internal.generic.Instruction instruction) {
        com.sun.org.apache.bcel.internal.generic.Instruction tmp = this._storeInstruction;
        this._storeInstruction = instruction;
        return tmp;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.VariableBase, com.sun.org.apache.xalan.internal.xsltc.compiler.TopLevelElement, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void display(int indent) {
        indent(indent);
        System.out.println("param " + ((Object) this._name));
        if (this._select != null) {
            indent(indent + 4);
            System.out.println("select " + this._select.toString());
        }
        displayContents(indent + 4);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.VariableBase, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void parseContents(Parser parser) {
        super.parseContents(parser);
        SyntaxTreeNode parent = getParent();
        if (parent instanceof Stylesheet) {
            this._isLocal = false;
            Param param = parser.getSymbolTable().lookupParam(this._name);
            if (param != null) {
                int us = getImportPrecedence();
                int them = param.getImportPrecedence();
                if (us == them) {
                    String name = this._name.toString();
                    reportError(this, parser, ErrorMsg.VARIABLE_REDEF_ERR, name);
                } else if (them > us) {
                    this._ignore = true;
                    copyReferences(param);
                    return;
                } else {
                    param.copyReferences(this);
                    param.disable();
                }
            }
            ((Stylesheet) parent).addParam(this);
            parser.getSymbolTable().addParam(this);
            return;
        }
        if (parent instanceof Template) {
            Template template = (Template) parent;
            this._isLocal = true;
            template.addParameter(this);
            if (template.isSimpleNamedTemplate()) {
                this._isInSimpleNamedTemplate = true;
            }
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.TopLevelElement, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        if (this._select != null) {
            this._type = this._select.typeCheck(stable);
            if (!(this._type instanceof ReferenceType) && !(this._type instanceof ObjectType)) {
                this._select = new CastExpr(this._select, Type.Reference);
            }
        } else if (hasContents()) {
            typeCheckContents(stable);
        }
        this._type = Type.Reference;
        return Type.Void;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.TopLevelElement, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        if (this._ignore) {
            return;
        }
        this._ignore = true;
        String name = BasisLibrary.mapQNameToJavaName(this._name.toString());
        String signature = this._type.toSignature();
        String className = this._type.getClassName();
        if (isLocal()) {
            if (this._isInSimpleNamedTemplate) {
                il.append(loadInstruction());
                BranchHandle ifBlock = il.append((BranchInstruction) new IFNONNULL(null));
                translateValue(classGen, methodGen);
                il.append(storeInstruction());
                ifBlock.setTarget(il.append(NOP));
                return;
            }
            il.append(classGen.loadTranslet());
            il.append(new PUSH(cpg, name));
            translateValue(classGen, methodGen);
            il.append(new PUSH(cpg, true));
            il.append(new INVOKEVIRTUAL(cpg.addMethodref(Constants.TRANSLET_CLASS, Constants.ADD_PARAMETER, Constants.ADD_PARAMETER_SIG)));
            if (className != "") {
                il.append(new CHECKCAST(cpg.addClass(className)));
            }
            this._type.translateUnBox(classGen, methodGen);
            if (this._refs.isEmpty()) {
                il.append(this._type.POP());
                this._local = null;
                return;
            } else {
                this._local = methodGen.addLocalVariable2(name, this._type.toJCType(), il.getEnd());
                il.append(this._type.STORE(this._local.getIndex()));
                return;
            }
        }
        if (classGen.containsField(name) == null) {
            classGen.addField(new Field(1, cpg.addUtf8(name), cpg.addUtf8(signature), null, cpg.getConstantPool()));
            il.append(classGen.loadTranslet());
            il.append(DUP);
            il.append(new PUSH(cpg, name));
            translateValue(classGen, methodGen);
            il.append(new PUSH(cpg, true));
            il.append(new INVOKEVIRTUAL(cpg.addMethodref(Constants.TRANSLET_CLASS, Constants.ADD_PARAMETER, Constants.ADD_PARAMETER_SIG)));
            this._type.translateUnBox(classGen, methodGen);
            if (className != "") {
                il.append(new CHECKCAST(cpg.addClass(className)));
            }
            il.append(new PUTFIELD(cpg.addFieldref(classGen.getClassName(), name, signature)));
        }
    }
}
