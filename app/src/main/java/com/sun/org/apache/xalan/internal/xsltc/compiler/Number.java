package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.classfile.Field;
import com.sun.org.apache.bcel.internal.generic.ALOAD;
import com.sun.org.apache.bcel.internal.generic.ASTORE;
import com.sun.org.apache.bcel.internal.generic.BranchHandle;
import com.sun.org.apache.bcel.internal.generic.BranchInstruction;
import com.sun.org.apache.bcel.internal.generic.CHECKCAST;
import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.GETFIELD;
import com.sun.org.apache.bcel.internal.generic.GOTO;
import com.sun.org.apache.bcel.internal.generic.IFNONNULL;
import com.sun.org.apache.bcel.internal.generic.ILOAD;
import com.sun.org.apache.bcel.internal.generic.INVOKESPECIAL;
import com.sun.org.apache.bcel.internal.generic.INVOKESTATIC;
import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.LocalVariableGen;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.sun.org.apache.bcel.internal.generic.PUSH;
import com.sun.org.apache.bcel.internal.generic.PUTFIELD;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MatchGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.NodeCounterGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.RealType;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
import com.sun.org.apache.xpath.internal.compiler.Keywords;
import java.util.ArrayList;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/Number.class */
final class Number extends Instruction implements Closure {
    private static final int LEVEL_SINGLE = 0;
    private static final int LEVEL_MULTIPLE = 1;
    private static final int LEVEL_ANY = 2;
    private static final String[] ClassNames = {"com.sun.org.apache.xalan.internal.xsltc.dom.SingleNodeCounter", "com.sun.org.apache.xalan.internal.xsltc.dom.MultipleNodeCounter", "com.sun.org.apache.xalan.internal.xsltc.dom.AnyNodeCounter"};
    private static final String[] FieldNames = {"___single_node_counter", "___multiple_node_counter", "___any_node_counter"};
    private Pattern _from = null;
    private Pattern _count = null;
    private Expression _value = null;
    private AttributeValueTemplate _lang = null;
    private AttributeValueTemplate _format = null;
    private AttributeValueTemplate _letterValue = null;
    private AttributeValueTemplate _groupingSeparator = null;
    private AttributeValueTemplate _groupingSize = null;
    private int _level = 0;
    private boolean _formatNeeded = false;
    private String _className = null;
    private ArrayList _closureVars = null;

    Number() {
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Closure
    public boolean inInnerClass() {
        return this._className != null;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Closure
    public Closure getParentClosure() {
        return null;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Closure
    public String getInnerClassName() {
        return this._className;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Closure
    public void addVariable(VariableRefBase variableRef) {
        if (this._closureVars == null) {
            this._closureVars = new ArrayList();
        }
        if (!this._closureVars.contains(variableRef)) {
            this._closureVars.add(variableRef);
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void parseContents(Parser parser) {
        int count = this._attributes.getLength();
        for (int i2 = 0; i2 < count; i2++) {
            String name = this._attributes.getQName(i2);
            String value = this._attributes.getValue(i2);
            if (name.equals("value")) {
                this._value = parser.parseExpression(this, name, null);
            } else if (name.equals("count")) {
                this._count = parser.parsePattern(this, name, null);
            } else if (name.equals(com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_FROM)) {
                this._from = parser.parsePattern(this, name, null);
            } else if (name.equals(com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_LEVEL)) {
                if (value.equals(com.sun.org.apache.xalan.internal.templates.Constants.ATTRVAL_SINGLE)) {
                    this._level = 0;
                } else if (value.equals(com.sun.org.apache.xalan.internal.templates.Constants.ATTRVAL_MULTI)) {
                    this._level = 1;
                } else if (value.equals("any")) {
                    this._level = 2;
                }
            } else if (name.equals(com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_FORMAT)) {
                this._format = new AttributeValueTemplate(value, parser, this);
                this._formatNeeded = true;
            } else if (name.equals("lang")) {
                this._lang = new AttributeValueTemplate(value, parser, this);
                this._formatNeeded = true;
            } else if (name.equals(com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_LETTERVALUE)) {
                this._letterValue = new AttributeValueTemplate(value, parser, this);
                this._formatNeeded = true;
            } else if (name.equals(com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_GROUPINGSEPARATOR)) {
                this._groupingSeparator = new AttributeValueTemplate(value, parser, this);
                this._formatNeeded = true;
            } else if (name.equals(com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_GROUPINGSIZE)) {
                this._groupingSize = new AttributeValueTemplate(value, parser, this);
                this._formatNeeded = true;
            }
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Instruction, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        if (this._value != null) {
            Type tvalue = this._value.typeCheck(stable);
            if (!(tvalue instanceof RealType)) {
                this._value = new CastExpr(this._value, Type.Real);
            }
        }
        if (this._count != null) {
            this._count.typeCheck(stable);
        }
        if (this._from != null) {
            this._from.typeCheck(stable);
        }
        if (this._format != null) {
            this._format.typeCheck(stable);
        }
        if (this._lang != null) {
            this._lang.typeCheck(stable);
        }
        if (this._letterValue != null) {
            this._letterValue.typeCheck(stable);
        }
        if (this._groupingSeparator != null) {
            this._groupingSeparator.typeCheck(stable);
        }
        if (this._groupingSize != null) {
            this._groupingSize.typeCheck(stable);
        }
        return Type.Void;
    }

    public boolean hasValue() {
        return this._value != null;
    }

    public boolean isDefault() {
        return this._from == null && this._count == null;
    }

    private void compileDefault(ClassGenerator classGen, MethodGenerator methodGen) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        int[] fieldIndexes = getXSLTC().getNumberFieldIndexes();
        if (fieldIndexes[this._level] == -1) {
            Field defaultNode = new Field(2, cpg.addUtf8(FieldNames[this._level]), cpg.addUtf8(Constants.NODE_COUNTER_SIG), null, cpg.getConstantPool());
            classGen.addField(defaultNode);
            fieldIndexes[this._level] = cpg.addFieldref(classGen.getClassName(), FieldNames[this._level], Constants.NODE_COUNTER_SIG);
        }
        il.append(classGen.loadTranslet());
        il.append(new GETFIELD(fieldIndexes[this._level]));
        BranchHandle ifBlock1 = il.append((BranchInstruction) new IFNONNULL(null));
        int index = cpg.addMethodref(ClassNames[this._level], "getDefaultNodeCounter", "(Lcom/sun/org/apache/xalan/internal/xsltc/Translet;Lcom/sun/org/apache/xalan/internal/xsltc/DOM;Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;)Lcom/sun/org/apache/xalan/internal/xsltc/dom/NodeCounter;");
        il.append(classGen.loadTranslet());
        il.append(methodGen.loadDOM());
        il.append(methodGen.loadIterator());
        il.append(new INVOKESTATIC(index));
        il.append(DUP);
        il.append(classGen.loadTranslet());
        il.append(SWAP);
        il.append(new PUTFIELD(fieldIndexes[this._level]));
        BranchHandle ifBlock2 = il.append((BranchInstruction) new GOTO(null));
        ifBlock1.setTarget(il.append(classGen.loadTranslet()));
        il.append(new GETFIELD(fieldIndexes[this._level]));
        ifBlock2.setTarget(il.append(NOP));
    }

    private void compileConstructor(ClassGenerator classGen) {
        InstructionList il = new InstructionList();
        ConstantPoolGen cpg = classGen.getConstantPool();
        MethodGenerator cons = new MethodGenerator(1, com.sun.org.apache.bcel.internal.generic.Type.VOID, new com.sun.org.apache.bcel.internal.generic.Type[]{Util.getJCRefType(Constants.TRANSLET_INTF_SIG), Util.getJCRefType(Constants.DOM_INTF_SIG), Util.getJCRefType("Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;"), com.sun.org.apache.bcel.internal.generic.Type.BOOLEAN}, new String[]{Constants.DOM_PNAME, "translet", Constants.ITERATOR_PNAME, "hasFrom"}, com.sun.org.apache.bcel.internal.Constants.CONSTRUCTOR_NAME, this._className, il, cpg);
        il.append(ALOAD_0);
        il.append(ALOAD_1);
        il.append(ALOAD_2);
        il.append(new ALOAD(3));
        il.append(new ILOAD(4));
        int index = cpg.addMethodref(ClassNames[this._level], com.sun.org.apache.bcel.internal.Constants.CONSTRUCTOR_NAME, "(Lcom/sun/org/apache/xalan/internal/xsltc/Translet;Lcom/sun/org/apache/xalan/internal/xsltc/DOM;Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;Z)V");
        il.append(new INVOKESPECIAL(index));
        il.append(RETURN);
        classGen.addMethod(cons);
    }

    private void compileLocals(NodeCounterGenerator nodeCounterGen, MatchGenerator matchGen, InstructionList il) {
        ConstantPoolGen cpg = nodeCounterGen.getConstantPool();
        LocalVariableGen local = matchGen.addLocalVariable(Constants.ITERATOR_PNAME, Util.getJCRefType("Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;"), null, null);
        int field = cpg.addFieldref(Constants.NODE_COUNTER, "_iterator", "Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;");
        il.append(ALOAD_0);
        il.append(new GETFIELD(field));
        local.setStart(il.append(new ASTORE(local.getIndex())));
        matchGen.setIteratorIndex(local.getIndex());
        LocalVariableGen local2 = matchGen.addLocalVariable("translet", Util.getJCRefType("Lcom/sun/org/apache/xalan/internal/xsltc/runtime/AbstractTranslet;"), null, null);
        int field2 = cpg.addFieldref(Constants.NODE_COUNTER, "_translet", Constants.TRANSLET_INTF_SIG);
        il.append(ALOAD_0);
        il.append(new GETFIELD(field2));
        il.append(new CHECKCAST(cpg.addClass(Constants.TRANSLET_CLASS)));
        local2.setStart(il.append(new ASTORE(local2.getIndex())));
        nodeCounterGen.setTransletIndex(local2.getIndex());
        LocalVariableGen local3 = matchGen.addLocalVariable(Constants.DOCUMENT_PNAME, Util.getJCRefType(Constants.DOM_INTF_SIG), null, null);
        int field3 = cpg.addFieldref(this._className, "_document", Constants.DOM_INTF_SIG);
        il.append(ALOAD_0);
        il.append(new GETFIELD(field3));
        local3.setStart(il.append(new ASTORE(local3.getIndex())));
        matchGen.setDomIndex(local3.getIndex());
    }

    private void compilePatterns(ClassGenerator classGen, MethodGenerator methodGen) {
        this._className = getXSLTC().getHelperClassName();
        NodeCounterGenerator nodeCounterGen = new NodeCounterGenerator(this._className, ClassNames[this._level], toString(), 33, null, classGen.getStylesheet());
        ConstantPoolGen cpg = nodeCounterGen.getConstantPool();
        int closureLen = this._closureVars == null ? 0 : this._closureVars.size();
        for (int i2 = 0; i2 < closureLen; i2++) {
            VariableBase var = ((VariableRefBase) this._closureVars.get(i2)).getVariable();
            nodeCounterGen.addField(new Field(1, cpg.addUtf8(var.getEscapedName()), cpg.addUtf8(var.getType().toSignature()), null, cpg.getConstantPool()));
        }
        compileConstructor(nodeCounterGen);
        if (this._from != null) {
            InstructionList il = new InstructionList();
            MatchGenerator matchGen = new MatchGenerator(17, com.sun.org.apache.bcel.internal.generic.Type.BOOLEAN, new com.sun.org.apache.bcel.internal.generic.Type[]{com.sun.org.apache.bcel.internal.generic.Type.INT}, new String[]{"node"}, "matchesFrom", this._className, il, cpg);
            compileLocals(nodeCounterGen, matchGen, il);
            il.append(matchGen.loadContextNode());
            this._from.translate(nodeCounterGen, matchGen);
            this._from.synthesize(nodeCounterGen, matchGen);
            il.append(IRETURN);
            nodeCounterGen.addMethod(matchGen);
        }
        if (this._count != null) {
            InstructionList il2 = new InstructionList();
            MatchGenerator matchGen2 = new MatchGenerator(17, com.sun.org.apache.bcel.internal.generic.Type.BOOLEAN, new com.sun.org.apache.bcel.internal.generic.Type[]{com.sun.org.apache.bcel.internal.generic.Type.INT}, new String[]{"node"}, "matchesCount", this._className, il2, cpg);
            compileLocals(nodeCounterGen, matchGen2, il2);
            il2.append(matchGen2.loadContextNode());
            this._count.translate(nodeCounterGen, matchGen2);
            this._count.synthesize(nodeCounterGen, matchGen2);
            il2.append(IRETURN);
            nodeCounterGen.addMethod(matchGen2);
        }
        getXSLTC().dumpClass(nodeCounterGen.getJavaClass());
        ConstantPoolGen cpg2 = classGen.getConstantPool();
        InstructionList il3 = methodGen.getInstructionList();
        int index = cpg2.addMethodref(this._className, com.sun.org.apache.bcel.internal.Constants.CONSTRUCTOR_NAME, "(Lcom/sun/org/apache/xalan/internal/xsltc/Translet;Lcom/sun/org/apache/xalan/internal/xsltc/DOM;Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;Z)V");
        il3.append(new NEW(cpg2.addClass(this._className)));
        il3.append(DUP);
        il3.append(classGen.loadTranslet());
        il3.append(methodGen.loadDOM());
        il3.append(methodGen.loadIterator());
        il3.append(this._from != null ? ICONST_1 : ICONST_0);
        il3.append(new INVOKESPECIAL(index));
        for (int i3 = 0; i3 < closureLen; i3++) {
            VariableRefBase varRef = (VariableRefBase) this._closureVars.get(i3);
            VariableBase var2 = varRef.getVariable();
            Type varType = var2.getType();
            il3.append(DUP);
            il3.append(var2.loadInstruction());
            il3.append(new PUTFIELD(cpg2.addFieldref(this._className, var2.getEscapedName(), varType.toSignature())));
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Instruction, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        il.append(classGen.loadTranslet());
        if (hasValue()) {
            compileDefault(classGen, methodGen);
            this._value.translate(classGen, methodGen);
            il.append(new PUSH(cpg, 0.5d));
            il.append(DADD);
            int index = cpg.addMethodref(Constants.MATH_CLASS, Keywords.FUNC_FLOOR_STRING, "(D)D");
            il.append(new INVOKESTATIC(index));
            int index2 = cpg.addMethodref(Constants.NODE_COUNTER, "setValue", "(D)Lcom/sun/org/apache/xalan/internal/xsltc/dom/NodeCounter;");
            il.append(new INVOKEVIRTUAL(index2));
        } else if (isDefault()) {
            compileDefault(classGen, methodGen);
        } else {
            compilePatterns(classGen, methodGen);
        }
        if (!hasValue()) {
            il.append(methodGen.loadContextNode());
            int index3 = cpg.addMethodref(Constants.NODE_COUNTER, Constants.SET_START_NODE, "(I)Lcom/sun/org/apache/xalan/internal/xsltc/dom/NodeCounter;");
            il.append(new INVOKEVIRTUAL(index3));
        }
        if (this._formatNeeded) {
            if (this._format != null) {
                this._format.translate(classGen, methodGen);
            } else {
                il.append(new PUSH(cpg, "1"));
            }
            if (this._lang != null) {
                this._lang.translate(classGen, methodGen);
            } else {
                il.append(new PUSH(cpg, "en"));
            }
            if (this._letterValue != null) {
                this._letterValue.translate(classGen, methodGen);
            } else {
                il.append(new PUSH(cpg, ""));
            }
            if (this._groupingSeparator != null) {
                this._groupingSeparator.translate(classGen, methodGen);
            } else {
                il.append(new PUSH(cpg, ""));
            }
            if (this._groupingSize != null) {
                this._groupingSize.translate(classGen, methodGen);
            } else {
                il.append(new PUSH(cpg, "0"));
            }
            int index4 = cpg.addMethodref(Constants.NODE_COUNTER, "getCounter", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;");
            il.append(new INVOKEVIRTUAL(index4));
        } else {
            int index5 = cpg.addMethodref(Constants.NODE_COUNTER, "setDefaultFormatting", "()Lcom/sun/org/apache/xalan/internal/xsltc/dom/NodeCounter;");
            il.append(new INVOKEVIRTUAL(index5));
            int index6 = cpg.addMethodref(Constants.NODE_COUNTER, "getCounter", "()Ljava/lang/String;");
            il.append(new INVOKEVIRTUAL(index6));
        }
        il.append(methodGen.loadHandler());
        int index7 = cpg.addMethodref(Constants.TRANSLET_CLASS, "characters", Constants.CHARACTERSW_SIG);
        il.append(new INVOKEVIRTUAL(index7));
    }
}
